package me.lbuddyboy.core.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Data;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.libraries.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:55 PM
 * lCore / me.lbuddyboy.core.rank
 */

@Data
public class Rank {

	@Getter public static Set<Rank> ranks = new HashSet<>();
	public static final MongoCollection<Document> collection = Core.getInstance().getDatabaseHandler().getMongoDatabase().getCollection("ranks");

	private final String name;

	private String displayName;
	private ChatColor color = ChatColor.WHITE;
	private String prefix = "";
	private int weight = 0;
	private List<String> permissions;

	public Rank(String name) {
		this.name = name;
		this.permissions = new ArrayList<>();

		load();
	}

	public static void loadAllRanks() {
		for (Document document : collection.find()) {
			Rank rank = new Rank(document.getString("name"));
			ranks.add(rank);

			Bukkit.getConsoleSender().sendMessage(CC.translate("&6&l[lCore] &fLoaded " + rank.getColor() + rank.getName()));
		}
	}

	public void load() {

		Document document = collection.find(Filters.eq("name", this.name)).first();

		if (document == null) return;

		this.permissions = document.getList("permissions", String.class);
		this.prefix = document.getString("prefix");
		this.displayName = document.getString("displayName");
		this.weight = document.getInteger("weight");
		this.color = ChatColor.valueOf(document.getString("color"));

	}

	public void save() {

		CompletableFuture.runAsync(() -> {

			Document document = new Document();

			document.put("name", this.name);
			document.put("displayName", this.displayName);
			document.put("prefix", this.prefix);
			document.put("weight", this.weight);
			document.put("color", this.color.name());
			document.put("permissions", this.permissions);

			collection.replaceOne(Filters.eq("name", this.name), document, new ReplaceOptions().upsert(true));

		});

	}

	public static Rank getByName(String name) {
		for (Rank rank : getRanks()) {
			if (rank.getName().equals(name)) {
				return rank;
			}
		}
		return null;
	}

}
