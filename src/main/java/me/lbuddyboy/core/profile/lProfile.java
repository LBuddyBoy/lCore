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
import me.lbuddyboy.core.database.packets.grant.GrantAddPacket;
import me.lbuddyboy.core.database.packets.grant.GrantRemovePacket;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.Lib;
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

	@Setter private Rank currentRank;
	@Setter private Grant currentGrant;
	@Setter private boolean loaded = false;

	public lProfile(UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.permissions = new ArrayList<>();
		this.grants = new ArrayList<>();
		this.punishments = new ArrayList<>();

		this.currentRank = Core.getInstance().getRankHandler().defaultRank();
		this.currentGrant = Core.getInstance().getRankHandler().defaultGrant(this.uniqueId);
	}

	public void load() {
		Document document = collection.find(Filters.eq("uniqueId", this.uniqueId.toString())).first();

		if (document == null) {
			save();
			return;
		}

		this.currentIP = document.getString("currentIP");
		this.name = document.getString("name");
		this.permissions = Lib.getInstance().getRedisHandler().getGSON().fromJson(document.getString("permissions"), Core.getInstance().getDatabaseHandler().getLIST_STRING_TYPE());
		for (JsonElement jsonElement : new JsonParser().parse(document.getString("grants")).getAsJsonArray()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (Core.getInstance().getRankHandler().getByName(jsonObject.get("rank").getAsString()) == null) continue;
			this.grants.add(Grant.deserialize(jsonObject));
		}
		for (JsonElement jsonElement : new JsonParser().parse(document.getString("punishments")).getAsJsonArray()) {
			this.punishments.add(Punishment.deserialize(jsonElement.getAsJsonObject()));
		}

		calculateGrants();
		setLoaded(true);
	}

	public void save() {
		Document document = new Document();

		document.put("uniqueId", this.uniqueId.toString());
		document.put("currentIP", this.currentIP);
		document.put("name", this.name);
		document.put("permissions", Lib.getInstance().getRedisHandler().getGSON().toJson(this.permissions, Core.getInstance().getDatabaseHandler().getLIST_STRING_TYPE()));

		JsonArray punishments = new JsonArray();
		this.punishments.forEach(punishment -> punishments.add(punishment.serialize()));
		document.put("punishments", punishments.toString());

		JsonArray grants = new JsonArray();
		this.grants.forEach(grant -> grants.add(grant.serialize()));
		document.put("grants", grants.toString());

		collection.replaceOne(Filters.eq("uniqueId", this.uniqueId.toString()), document, new ReplaceOptions().upsert(true));
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
			if (grant.isExpired() && !grant.isRemoved()) {
				grant.setRemoved(true);
				grant.setRemovedAt(System.currentTimeMillis());
				grant.setRemovedBy(null);
				grant.setRemovedReason("Grant Expired");

				this.currentRank = null;
				this.currentGrant = null;

				new GrantRemovePacket(grant).send();
			}
		}
		if (this.currentGrant == null) {
			grantNext();
			new GrantAddPacket(this.currentGrant).send();
			if (this.currentGrant == null) {
				this.currentGrant = Core.getInstance().getRankHandler().defaultGrant(this.uniqueId);
			}
		}
	}

	public void grantNext() {
		List<Grant> grants = new ArrayList<>(this.grants);
		grants.sort(Comparator.comparingInt((grant) -> grant.getRank().getWeight()));
		for (Grant grant : grants) {
			this.currentGrant = grant;
			this.currentRank = grant.getRank();
		}
	}

	public String getNameWithColor() {
		return this.currentRank.getColor() + this.name;
	}

}
