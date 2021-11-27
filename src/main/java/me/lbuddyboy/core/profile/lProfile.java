package me.lbuddyboy.core.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.RedisHandler;
import me.lbuddyboy.core.profile.global.GlobalStatistic;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.JavaUtils;
import me.lbuddyboy.libraries.util.TimeUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.IOException;
import java.util.*;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:24 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */

@Getter
public class lProfile {

	private final UUID uniqueId;

	@Setter
	private String name;
	@Setter
	private String currentIP;
	private List<String> permissions;
	private final List<Grant> grants;
	private final List<Punishment> punishments;
	private final Map<GlobalStatistic, Integer> globalStatisticMap;

	@Setter
	private long lastReport;
	@Setter
	private Rank currentRank;
	@Setter
	private Grant currentGrant;
	private boolean loaded;

	public lProfile(UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.name = Bukkit.getOfflinePlayer(this.uniqueId).getName();
		this.permissions = new ArrayList<>();
		this.grants = new ArrayList<>();
		this.punishments = new ArrayList<>();
		this.globalStatisticMap = new HashMap<>();

		load();
	}

	public void load() {
		if (Configuration.STORAGE_MONGO.getBoolean()) {
			Document document = Core.getInstance().getMongoHandler().getMongoDatabase().getCollection("profiles").find(Filters.eq("uniqueId", this.uniqueId.toString())).first();

			if (document == null) {
				System.out.println("Detected a null profile.");
				Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
					if (Bukkit.getPlayer(this.uniqueId) != null) {
						Bukkit.getPlayer(this.uniqueId).kickPlayer(CC.translate(Configuration.FAILED_TO_LOAD_PROFILE.getMessage()));
					}
				});
				return;
			}

			this.lastReport = document.getLong("lastReport");
			this.currentIP = document.getString("currentIP");
			this.permissions.addAll(RedisHandler.getGSON().fromJson(document.getString("permissions"), Core.getInstance().getMongoHandler().getLIST_STRING_TYPE()));

			for (GlobalStatistic statistic : Core.getInstance().getProfileHandler().getGlobalStatistics()) {
				this.globalStatisticMap.put(statistic, document.getInteger(statistic.statisticName()));
			}

			for (JsonElement jsonElement : new JsonParser().parse(document.getString("grants")).getAsJsonArray()) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				if (Core.getInstance().getRankHandler().getByName(jsonObject.get("rank").getAsString()) == null)
					continue;
				this.grants.add(Grant.deserialize(jsonObject));
			}

			for (JsonElement jsonElement : new JsonParser().parse(document.getString("punishments")).getAsJsonArray()) {
				this.punishments.add(Punishment.deserialize(jsonElement.getAsJsonObject()));
			}
		} else {
			YamlConfiguration config = Core.getInstance().getProfilesYML().gc();

			if (config.getConfigurationSection("profiles." + uniqueId) == null) return;

			this.lastReport = config.getLong("profiles." + this.uniqueId + ".lastReport");
			this.currentIP = config.getString("profiles." + this.uniqueId + ".currentIP");
			this.permissions.addAll(config.getStringList("profiles." + this.uniqueId + ".permissions"));

			try {
				for (GlobalStatistic statistic : Core.getInstance().getProfileHandler().getGlobalStatistics()) {
					this.globalStatisticMap.put(statistic, config.getInt("profiles." + this.uniqueId + "." + statistic.statisticName()));
				}
			} catch (Exception ignored) {
			}

			for (JsonElement jsonElement : new JsonParser().parse(config.getString("profiles." + this.uniqueId + ".grants")).getAsJsonArray()) {
				if (Core.getInstance().getRankHandler().getByName(jsonElement.getAsJsonObject().get("rank").getAsString()) == null)
					continue;
				this.grants.add(Grant.deserialize(jsonElement.getAsJsonObject()));
			}

			for (JsonElement jsonElement : new JsonParser().parse(config.getString("profiles." + this.uniqueId + ".punishments")).getAsJsonArray()) {
				this.punishments.add(Punishment.deserialize(jsonElement.getAsJsonObject()));
			}
		}

		calculateGrants();
		loaded = true;
	}

	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			if (Configuration.STORAGE_MONGO.getBoolean()) {
				Document document = new Document();

				document.put("uniqueId", this.uniqueId.toString());
				document.put("lastReport", this.lastReport);
				document.put("currentIP", this.currentIP);
				document.put("name", this.name);

				document.put("permissions", RedisHandler.getGSON().toJson(this.permissions, Core.getInstance().getMongoHandler().getLIST_STRING_TYPE()));

				for (Map.Entry<GlobalStatistic, Integer> entry : this.globalStatisticMap.entrySet()) {
					document.put(entry.getKey().statisticName(), entry.getValue());
				}

				JsonArray punishments = new JsonArray();
				this.punishments.forEach(punishment -> punishments.add(punishment.serialize()));
				document.put("punishments", punishments.toString());

				JsonArray grants = new JsonArray();
				this.grants.forEach(grant -> grants.add(grant.serialize()));
				document.put("grants", grants.toString());

				Core.getInstance().getMongoHandler().getMongoDatabase().getCollection("profiles").replaceOne(Filters.eq("uniqueId", this.uniqueId.toString()), document, new ReplaceOptions().upsert(true));
				System.out.println("Saved a profile. (" + this.uniqueId + ")");
			} else {
				YamlConfiguration config = Core.getInstance().getProfilesYML().gc();

				config.set("profiles." + this.uniqueId + ".lastReport", this.lastReport);
				config.set("profiles." + this.uniqueId + ".currentIP", this.currentIP);
				config.set("profiles." + this.uniqueId + ".name", this.name);
				config.set("profiles." + this.uniqueId + ".permissions", this.permissions);

				for (Map.Entry<GlobalStatistic, Integer> entry : this.globalStatisticMap.entrySet()) {
					config.set("profiles." + this.uniqueId + "." + entry.getKey().statisticName(), entry.getValue());
				}

				JsonArray punishments = new JsonArray();
				this.punishments.forEach(punishment -> punishments.add(punishment.serialize()));
				config.set("profiles." + this.uniqueId + ".punishments", punishments.toString());

				JsonArray grants = new JsonArray();
				this.grants.forEach(grant -> grants.add(grant.serialize()));
				config.set("profiles." + this.uniqueId + ".grants", grants.toString());

				try {
					Core.getInstance().getProfilesYML().save();
					Core.getInstance().getProfilesYML().reloadConfig();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setupPermissions() {
		Player player = Bukkit.getPlayer(this.uniqueId);

		if (player == null) return;

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getAttachment() == null) {
				continue;
			}
			attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
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
		List<Punishment> sorted = new ArrayList<>();
		for (Punishment punishment : this.punishments) {
			if (punishment.getType() == type) {
				sorted.add(punishment);
			}
		}
		return sorted;
	}

	public List<Punishment> getPunishmentsByTypeSorted(PunishmentType type) {
		List<Punishment> sorted = new ArrayList<>();
		for (Punishment punishment : this.punishments) {
			if (punishment.isResolved()) continue;
			if (punishment.getType() == type) {
				sorted.add(punishment);
			}
		}
		return sorted;
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

	public void addGlobalStatistic(GlobalStatistic statistic, int toAdd) {
		this.globalStatisticMap.put(statistic, this.globalStatisticMap.getOrDefault(statistic, 0) + toAdd);
		save();
	}

	public void takeGlobalStatistic(GlobalStatistic statistic, int toTake) {
		this.globalStatisticMap.put(statistic, this.globalStatisticMap.getOrDefault(statistic, 0) - toTake);
		save();
	}

	public List<String> coloredAlts() {
		List<String> coloredAlts = new ArrayList<>();

		for (lProfile profile : Core.getInstance().getProfileHandler().getProfiles().values()) {
			if (Objects.equals(profile.getCurrentIP(), this.currentIP)) {
				Player player = Bukkit.getPlayer(profile.getUniqueId());
				if (player != null) {
					coloredAlts.add(CC.translate(Configuration.ALTS_ONLINE_COLOR.getMessage() + player.getName()));
				} else {
					if (profile.hasActivePunishment(PunishmentType.BLACKLIST)) {
						coloredAlts.add(CC.translate(Configuration.ALTS_BLACKLISTED_COLOR.getMessage() + profile.getName()));
					} else if (profile.hasActivePunishment(PunishmentType.BAN)) {
						coloredAlts.add(CC.translate(Configuration.ALTS_BANNED_COLOR.getMessage() + profile.getName()));
					} else if (profile.hasActivePunishment(PunishmentType.MUTE)) {
						coloredAlts.add(CC.translate(Configuration.ALTS_MUTED_COLOR.getMessage() + profile.getName()));
					} else {
						coloredAlts.add(CC.translate(Configuration.ALTS_OFFLINE_COLOR.getMessage() + profile.getName()));
					}
				}
			}
		}

		return coloredAlts;
	}

}
