package me.lbuddyboy.core.rank;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.rank.listener.RankEditListener;
import me.lbuddyboy.libraries.menu.listener.MenuListener;
import me.lbuddyboy.libraries.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
		new MenuListener();

		loadAllRanks();
	}

	public void loadAllRanks() {
		if (getByName("Default") == null) {
			try {
				Rank rank = new Rank("Default", true);
				ranks.add(rank);
			} catch (Exception e) {
				Rank rank = new Rank("Default", false);
				rank.save();
				ranks.add(rank);
			}
		}
		YamlConfiguration config = Core.getInstance().getRanksYML().gc();
		try { // this is here incase the ranks: section is empty
			for (String key : config.getConfigurationSection("ranks").getKeys(false)) {
				Rank rank = new Rank(key);
				ranks.add(rank);

				Bukkit.getConsoleSender().sendMessage(CC.translate("&6&l[lCore] &fLoaded " + rank.getColor() + rank.getName()));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public Grant defaultGrant(UUID target) {
		return new Grant(UUID.randomUUID(), defaultRank(), null, target, "Default Grant", System.currentTimeMillis(), Long.MAX_VALUE);
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
