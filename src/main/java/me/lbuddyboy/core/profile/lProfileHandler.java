package me.lbuddyboy.core.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.global.GlobalStatistic;
import me.lbuddyboy.core.profile.grant.listener.GrantListener;
import me.lbuddyboy.core.punishment.listener.PunishmentListener;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:31 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */

@Getter
public class lProfileHandler {

	private final Map<UUID, lProfile> profiles;
	private final List<GlobalStatistic> globalStatistics;
	private ConcurrentHashMap<UUID, String> allProfiles = new ConcurrentHashMap<>();
	private final MongoCollection<Document> collection = Core.getInstance().getMongoHandler().getMongoDatabase().getCollection("profiles");

	public lProfileHandler() {
		this.profiles = new HashMap<>();
		this.globalStatistics = new ArrayList<>();

		Bukkit.getPluginManager().registerEvents(new lProfileListener(), Core.getInstance());
		Bukkit.getPluginManager().registerEvents(new PunishmentListener(), Core.getInstance());
		Bukkit.getPluginManager().registerEvents(new GrantListener(), Core.getInstance());

		Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
			for (lProfile profile : getProfiles().values()) {
				profile.calculateGrants();
				profile.setupPermissions();
			}
		}, 20 * 30, 20 * 30);
	}

	public ConcurrentHashMap<UUID, String> getAllCachedPlayers() {
		if (Configuration.STORAGE_MONGO.getBoolean()) {
			for (Document document : collection.find()) {
				allProfiles.put(UUID.fromString(document.getString("uniqueId")), document.getString("name"));
			}
		} else {
			for (String key : Core.getInstance().getProfilesYML().gc().getConfigurationSection("profiles").getKeys(false)) {
				allProfiles.put(UUID.fromString(key), Core.getInstance().getProfilesYML().gc().getString("profiles." + key + ".name"));
			}
		}
		return allProfiles;
	}

	public lProfile getByUUID(UUID toLook) {
		lProfile profile = new lProfile(toLook);
		if (!profile.isLoaded()) {
			return null;
		}
		return profile;
	}

}
