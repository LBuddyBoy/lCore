package me.lbuddyboy.core.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.profile.grant.listener.GrantListener;
import me.lbuddyboy.libraries.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:03 PM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */
public class ProfileHandler {

	@Getter private final Set<Profile> profiles;
	@Getter private final MongoCollection<Document> collection = Core.getInstance().getDatabaseHandler().getMongoDatabase().getCollection("profiles");

	public ProfileHandler() {
		profiles = new HashSet<>();

		Core.getInstance().getServer().getPluginManager().registerEvents(new ProfileListener(), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(new GrantListener(), Core.getInstance());

		loadAllProfiles();

		Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
			for (Profile profile : getProfiles()) {
				profile.recalculateGrants();
			}
		}, 20 * 5, 20 * 5);
	}

	public void loadAllProfiles() {
		int i = 0;
		int iy = 0;
		if (Settings.STORAGE_MONGO.getBoolean()) {
			for (Document document : collection.find()) {
				Profile profile = new Profile(UUID.fromString(document.getString("uniqueId")), document.getString("name"), document.getString("ip"));
				profiles.add(profile);
				++i;
			}
		}
		if (Settings.STORAGE_YAML.getBoolean()) {
			YamlConfiguration config = Core.getInstance().getProfilesYML().gc();
			for (String key : config.getConfigurationSection("uuid").getKeys(false)) {
				UUID uuid = UUID.fromString(key);
				String absolute = "profiles." + key + ".";
				String name = config.getString(absolute + "name");
				String ip = config.getString(absolute + "ip");
				Profile profile = new Profile(uuid, name, ip);
				profiles.add(profile);
				++iy;
			}
		}
		Bukkit.getConsoleSender().sendMessage(CC.translate("&fLoaded &6" + i + " &fProfiles from Mongo"));
		Bukkit.getConsoleSender().sendMessage(CC.translate("&fLoaded &6" + i + " &fProfiles from Yaml"));
	}

	public Profile getByUUID(UUID uuid) {
		for (Profile profile : profiles) {
			if (profile.getUniqueId() == uuid) {
				return profile;
			}
		}
		return null;
	}

}
