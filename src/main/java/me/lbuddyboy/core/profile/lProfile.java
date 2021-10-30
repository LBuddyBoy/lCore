package me.lbuddyboy.core.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.JavaUtils;
import me.lbuddyboy.libraries.util.TimeUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:24 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */

@Getter
public class lProfile {

	private final MongoCollection<Document> collection = Core.getInstance().getDatabaseHandler().getMongoDatabase().getCollection("profiles");

	private final UUID uniqueId;

	@Setter private String name;
	@Setter private String currentIP;
	private List<String> permissions;
	private List<Grant> grants;
	private List<Punishment> punishments;

	@Setter private long lastReport;
	@Setter private Rank currentRank;
	@Setter private Grant currentGrant;
	private boolean loaded;

	public lProfile(UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.name = Bukkit.getOfflinePlayer(this.uniqueId).getName();
		this.permissions = new ArrayList<>();
		this.grants = new ArrayList<>();
		this.punishments = new ArrayList<>();

		load();
	}

	public void load() {
		Document document = collection.find(Filters.eq("uniqueId", this.uniqueId.toString())).first();

		if (document == null) {
			return;
		}

		this.lastReport = document.getLong("lastReport");
		this.currentIP = document.getString("currentIP");
		this.permissions = Core.getInstance().getRedisHandler().getGSON().fromJson(document.getString("permissions"), Core.getInstance().getDatabaseHandler().getLIST_STRING_TYPE());

		for (JsonElement jsonElement : new JsonParser().parse(document.getString("grants")).getAsJsonArray()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (Core.getInstance().getRankHandler().getByName(jsonObject.get("rank").getAsString()) == null) continue;
			this.grants.add(Grant.deserialize(jsonObject));
		}

		for (JsonElement jsonElement : new JsonParser().parse(document.getString("punishments")).getAsJsonArray()) {
			this.punishments.add(Punishment.deserialize(jsonElement.getAsJsonObject()));
		}

		calculateGrants();
		loaded = true;
	}

	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			Document document = new Document();

			document.put("uniqueId", this.uniqueId.toString());
			document.put("lastReport", this.lastReport);
			document.put("currentIP", this.currentIP);
			document.put("name", this.name);
			document.put("permissions", Core.getInstance().getRedisHandler().getGSON().toJson(this.permissions, Core.getInstance().getDatabaseHandler().getLIST_STRING_TYPE()));

			JsonArray punishments = new JsonArray();
			this.punishments.forEach(punishment -> punishments.add(punishment.serialize()));
			document.put("punishments", punishments.toString());

			JsonArray grants = new JsonArray();
			this.grants.forEach(grant -> grants.add(grant.serialize()));
			document.put("grants", grants.toString());

			collection.replaceOne(Filters.eq("uniqueId", this.uniqueId.toString()), document, new ReplaceOptions().upsert(true));
		});
	}

	public void setupPermissions() {
		Player player = Bukkit.getPlayer(this.uniqueId);

		if (player == null) return;

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getAttachment() == null) {
				continue;
			}
			attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
				attachmentInfo.getAttachment().unsetPermission(permission);
			});
		}
		PermissionAttachment attachment = player.addAttachment(Core.getInstance());
		for (String permission : getCurrentRank().getPermissions()) {
			if (!attachment.getPermissions().containsKey(permission)) {
				attachment.setPermission(permission, true);
			}
		}
		for (String permission : this.permissions) {
			if (!attachment.getPermissions().containsKey(permission)) {
				attachment.setPermission(permission, true);
			}
		}
		player.recalculatePermissions();
	}

	public void calculateGrants() {
		for (Grant grant : this.grants) {
			if (!grant.isRemoved() && grant.isExpired()) {
				grant.setRemovedAt(System.currentTimeMillis());
				grant.setRemovedReason("Expired");
				grant.setRemovedBy(null);
				grant.setRemoved(true);
				if (this.currentGrant != null && this.currentGrant.equals(grant)) {
					this.currentGrant = null;
				}
			}
		}

		if (this.currentGrant == null) {
			this.grantNext();
			if (this.currentGrant != null) {
				return;
			}

			Grant grant = new Grant(UUID.randomUUID(), Core.getInstance().getRankHandler().defaultRank(), null, this.uniqueId, "Default", System.currentTimeMillis(), Long.MAX_VALUE);
			this.grants.add(grant);
			this.setGrant(grant);
		}
	}

	public void setGrant(Grant grant) {
		this.currentGrant = grant;
		this.currentRank = grant.getRank();
		Player player = Bukkit.getPlayer(this.uniqueId);
		if (player != null) {
			player.setDisplayName(CC.translate(grant.getRank().getPrefix() + player.getName()));
			setupPermissions();
		}

	}

	public void grantNext() {
		List<Grant> grants = new ArrayList<>(this.grants);
		grants.sort(Comparator.comparingInt((grant1) -> grant1.getRank().getWeight()));

		for (Grant grant : grants) {
			if (!grant.isRemoved() && !grant.isExpired()) {
				this.setGrant(grant);
			}
		}
	}

	public boolean canSendReport() {
		return ((this.lastReport + JavaUtils.parse(Core.getInstance().getConfig().getString("report-cooldown"))) - System.currentTimeMillis()) <= 0;
	}

	public String getRemainingReportTime() {
		return TimeUtils.formatIntoDetailedString((int) (((JavaUtils.parse(Core.getInstance().getConfig().getString("report-cooldown")) + this.lastReport)) - System.currentTimeMillis()) / 1000);
	}

	public List<Punishment> getPunishmentsByType(PunishmentType type) {
		List<Punishment> punishments = new ArrayList<>();
		for (Punishment punishment : this.punishments) {
			if (punishment.getType() == type) {
				punishments.add(punishment);
			}
		}
		return punishments;
	}

	public boolean hasActivePunishment(PunishmentType type) {
		for (Punishment punishment : this.punishments) {
			if (punishment.getType() == type) {
				if (punishment.isPermanent() && !punishment.isResolved()) {
					return true;
				}
				if (punishment.getTimeLeft() > 0 && !punishment.isResolved()) {
					return true;
				}
			}
		}
		return false;
	}

	public Punishment getActivePunishment(PunishmentType type) {
		for (Punishment punishment : this.punishments) {
			if (punishment.getType() == type) {
				if (punishment.isPermanent() && !punishment.isResolved()) {
					return punishment;
				}
				if (punishment.getTimeLeft() > 0 && !punishment.isResolved()) {
					return punishment;
				}
			}
		}
		return null;
	}

	public String getNameWithColor() {
		return this.currentRank.getColor() + this.name;
	}

	public boolean isLoaded() {
		if (this.name == null || this.name.equals(""))
			return false;
		if (this.uniqueId == null)
			return false;

		return this.loaded;
	}
}
