package me.lbuddyboy.core.rank;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.rank.listener.RankEditListener;
import me.lbuddyboy.libraries.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:01 PM
 * LBuddyBoy Development / me.lbuddyboy.core.rank
 */
public class RankHandler {

	@Getter private final Set<Rank> ranks;
	@Getter private final MongoCollection<Document> collection = Core.getInstance().getDatabaseHandler().getMongoDatabase().getCollection("ranks");


	public RankHandler() {
		ranks = new HashSet<>();

		Core.getInstance().getServer().getPluginManager().registerEvents(new RankEditListener(), Core.getInstance());

		loadAllRanks();
	}

	public void loadAllRanks() {
		if (getByName("Default") == null) {
			Rank rank = new Rank("Default");
			ranks.add(rank);
		}
		if (Settings.STORAGE_MONGO.getBoolean()) {
			for (Document document : collection.find()) {
				Rank rank = new Rank(document.getString("name"));
				ranks.add(rank);

				Bukkit.getConsoleSender().sendMessage(CC.translate("&6&l[lCore] &fLoaded " + rank.getColor() + rank.getName()));
			}
		}
		if (Settings.STORAGE_YAML.getBoolean()) {
			YamlConfiguration config = Core.getInstance().getRanksYML().gc();
			for (String key : config.getConfigurationSection("ranks").getKeys(false)) {
				Rank rank = new Rank(key);
				ranks.add(rank);

				Bukkit.getConsoleSender().sendMessage(CC.translate("&6&l[lCore] &fLoaded " + rank.getColor() + rank.getName()));
			}
		}
	}

	public Rank defaultRank() {
		if (getByName("Default") != null)
			return  getByName("Default");

		return new Rank("Default");
	}

	public Rank getByName(String name) {
		for (Rank rank : getRanks()) {
			if (rank.getName().equals(name)) {
				return rank;
			}
		}
		return null;
	}

}
