package me.lbuddyboy.core.rank;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Data;
import lombok.SneakyThrows;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankDeletePacket;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:55 PM
 * lCore / me.lbuddyboy.core.rank
 */

@Data
public class Rank {

	private static Core plugin = Core.getInstance();
	private static RankHandler rankHandler = plugin.getRankHandler();

	private String name = "";

	private int weight = 0;
	private String prefix = "";
	private String displayName;
	private List<String> permissions;
	private ChatColor color = ChatColor.WHITE;

	public Rank(String name) {
		this.name = name;
		this.permissions = new ArrayList<>();
		this.displayName = name;

		load();
	}

	public Rank(String name, boolean load) {
		this.name = name;
		this.permissions = new ArrayList<>();
		this.displayName = name;

		if (load) {
			load();
		}
	}

	@SneakyThrows
	public void load() {
		if (Settings.STORAGE_MONGO.getBoolean()) {
			Document document = rankHandler.getCollection().find(Filters.eq("name", this.name)).first();

			if (document == null) return;

			this.permissions = document.getList("permissions", String.class);
			this.prefix = document.getString("prefix");
			this.displayName = document.getString("displayName");
			this.weight = document.getInteger("weight");
			this.color = ChatColor.valueOf(document.getString("color"));
		}
		if (Settings.STORAGE_YAML.getBoolean()) {
			YamlConfiguration config = Core.getInstance().getRanksYML().gc();
			String absolute = "ranks." + this.name + ".";

			if (!config.contains("ranks." + this.name)) return;

			this.permissions = config.getStringList(absolute + "permissions");
			this.prefix = config.getString(absolute + "prefix");
			this.displayName = config.getString(absolute + "displayName");
			this.weight = config.getInt(absolute + "weight");
			this.color = ChatColor.valueOf(config.getString(absolute + "color"));
		}
	}

	@SneakyThrows
	public void save() {
		CompletableFuture.runAsync(() -> {
			if (Settings.STORAGE_MONGO.getBoolean()) {
				Document document = new Document();


				document.put("name", this.name);
				document.put("displayName", this.displayName);
				document.put("prefix", this.prefix);
				document.put("weight", this.weight);
				document.put("color", this.color.name());
				document.put("permissions", this.permissions);

				rankHandler.getCollection().replaceOne(Filters.eq("name", this.name), document, new ReplaceOptions().upsert(true));
			}
			if (Settings.STORAGE_YAML.getBoolean()) {
				YamlConfiguration config = Core.getInstance().getRanksYML().gc();

				config.set("name", this.name);
				config.set("displayName", this.displayName);
				config.set("prefix", this.displayName);
				config.set("weight", this.weight);
				config.set("color", this.color.toString());
				config.set("permissions", this.permissions);

				try {
					Core.getInstance().getRanksYML().save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SneakyThrows
	public void delete() {
		rankHandler.getRanks().remove(this);

		new RankDeletePacket(this).send();

		if (Settings.STORAGE_MONGO.getBoolean()) {
			rankHandler.getCollection().deleteOne(Filters.eq("name", name));
		}
		if (Settings.STORAGE_YAML.getBoolean()) {
			YamlConfiguration config = Core.getInstance().getRanksYML().gc();
			config.getConfigurationSection("ranks." + this.name).getKeys(false).clear();
			config.set("ranks." + this.name, null);
			try {
				Core.getInstance().getRanksYML().save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
