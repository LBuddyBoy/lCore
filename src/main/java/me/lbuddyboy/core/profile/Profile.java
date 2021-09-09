package me.lbuddyboy.core.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.grant.GrantAddPacket;
import me.lbuddyboy.core.database.packets.grant.GrantRemovePacket;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.profile.grant.command.SetRankCommand;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:52 PM
 * lCore / me.lbuddyboy.core.profile
 */

@Getter
public class Profile {

	private final UUID uniqueId;
	private final String name;
	private final String ip;

	private List<Punishment> punishments;
	private List<Grant> grants;
	private List<String> knownIPs;
	private List<String> permissions;

	@Setter
	private Rank currentRank = Core.getInstance().getRankHandler().defaultRank();
	@Setter
	private Grant currentGrant;

	public Profile(UUID uniqueID, String name, String ip) {
		this.uniqueId = uniqueID;
		this.name = name;
		this.ip = ip;

		this.punishments = new ArrayList<>();
		this.grants = new ArrayList<>();
		this.knownIPs = new ArrayList<>();
		this.permissions = new ArrayList<>();

		load();
	}

	public void load() {

		if (Settings.STORAGE_YAML.getBoolean()) {
			YamlConfiguration config = Core.getInstance().getProfilesYML().gc();
			String absolute = "profiles." + this.uniqueId.toString() + ".";

			if (!config.contains("profiles." + this.uniqueId.toString())) return;

			this.knownIPs = config.getStringList(absolute + "knownIPs");
			this.grants = (List<Grant>) config.get(absolute + "grants", Collections.singletonList(new Grant(UUID.randomUUID(), Core.getInstance().getRankHandler().defaultRank(), null, this.uniqueId, "Default Grant", System.currentTimeMillis(), Long.MAX_VALUE)));
			this.punishments = (List<Punishment>) config.getList(absolute + "punishments");
			this.permissions = (List<String>) config.getList(absolute + "permissions");
		}

		if (Settings.STORAGE_MONGO.getBoolean()) {
			Document document = Core.getInstance().getProfileHandler().getCollection().find(Filters.eq("uniqueId", this.uniqueId.toString())).first();

			if (document == null) return;

			this.knownIPs = document.getList("knownIPs", String.class);
			this.knownIPs = document.getList("permissions", String.class);
			this.grants = document.getList("grants", Grant.class, Collections.singletonList(new Grant(UUID.randomUUID(), Core.getInstance().getRankHandler().defaultRank(), null, this.uniqueId, "Default Grant", System.currentTimeMillis(), Long.MAX_VALUE)));
			this.punishments = document.getList("punishments", Punishment.class);
		}

		boolean hasDefault = false;

		for (Grant grant : this.grants) {
			if (grant.getRank() == Core.getInstance().getRankHandler().defaultRank()) {
				hasDefault = true;
			}
		}

		if (!hasDefault) {
			SetRankCommand.setRank(Bukkit.getConsoleSender(), this.uniqueId, Core.getInstance().getRankHandler().defaultRank(), "perm", "Default Grant");
		}

		try {
			List<Grant> grants = new ArrayList<>(this.grants);
			grants.sort(Comparator.comparingInt((grant) -> grant.getRank().getWeight()));

			this.currentGrant = grants.get(0);
			this.currentRank = currentGrant.getRank();
		} catch (Exception ignored) {
		}
	}

	@SneakyThrows
	public void save() {
		CompletableFuture.runAsync(() -> {
			Document document = new Document();

			YamlConfiguration config = Core.getInstance().getProfilesYML().gc();

			if (Settings.STORAGE_YAML.getBoolean()) {
				String absolute = "profiles." + this.uniqueId.toString() + ".";
				config.set(absolute + ".uniqueId", this.uniqueId.toString());
				config.set(absolute + ".name", this.name);
				config.set(absolute + ".ip", this.ip);
				config.set(absolute + ".knownIPs", this.ip);
				config.set(absolute + ".grants", this.grants);
				config.set(absolute + ".punishments", this.punishments);
				config.set(absolute + ".permissions", this.permissions);

				try {
					Core.getInstance().getProfilesYML().save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (Settings.STORAGE_MONGO.getBoolean()) {
				document.put("uniqueId", this.uniqueId.toString());
				document.put("name", this.name);
				document.put("ip", this.ip);
				document.put("knownIPs", this.knownIPs);
				document.put("grants", this.grants);
				document.put("punishments", this.punishments);
				document.put("permissions", this.permissions);

				Core.getInstance().getProfileHandler().getCollection().replaceOne(Filters.eq("uniqueId", this.uniqueId.toString()), document, new ReplaceOptions().upsert(true));
			}
		});
	}

	public void recalculateGrants() {

		boolean updateRank = false;

		/*
		Remove any grant that's expired
		 */

		for (Grant grant : this.grants) {
			if (grant.getRemainingTime() <= 0) {
				if (!grant.isRemoved()) {
					grant.setRemoved(true);
					grant.setRemovedAt(System.currentTimeMillis());
					grant.setRemovedBy(null);
					grant.setRemovedReason("Expired");

					new GrantRemovePacket(grant).send();

					if (this.currentRank.getName().equalsIgnoreCase(grant.getRank().getName())) {
						updateRank = true;
					}
				}
			}
		}

		/*
		Updates the rank to the highest prioritied grant they have.
		 */

		List<Grant> grants = this.grants.stream().filter(grant -> !grant.isExpired() && !grant.isRemoved()).collect(Collectors.toList());

		Player player = Bukkit.getPlayer(this.uniqueId);

		if (updateRank) {
			grants.sort(Comparator.comparingInt((grant) -> grant.getRank().getWeight()));

			Grant grant = grants.get(0);
			this.currentGrant = grant;
			this.currentRank = grant.getRank();
			new GrantAddPacket(grant).send();
			if (player != null) {
				player.setDisplayName(getDisplayName());
			}
		}
		if (grants.size() <= 1) {
			if (this.currentRank.getName().equals("Default")) return;
			this.currentRank = Core.getInstance().getRankHandler().getByName("Default");
			this.currentGrant = grants.get(0);
			new GrantAddPacket(grants.get(0)).send();
			if (player != null) {
				player.setDisplayName(getDisplayName());
			}
		}

		setupPermissions();
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
			attachment.setPermission(permission, true);
		}
		for (String permission : this.permissions) {
			if (!attachment.getPermissions().containsKey(permission)) {
				attachment.setPermission(permission, true);
			}
		}
		player.recalculatePermissions();
	}

	public String getNameWithColor() {
		return CC.translate(this.currentRank.getColor() + this.name);
	}

	public String getDisplayName() {
		return CC.translate(this.currentRank.getPrefix() + this.name);
	}

}
