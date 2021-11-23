package me.lbuddyboy.core.rank;

import lombok.Data;
import lombok.SneakyThrows;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.packets.rank.RankDeletePacket;
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
	private String suffix = "";
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
		YamlConfiguration config = Core.getInstance().getRanksYML().gc();
		String absolute = "ranks." + this.name + ".";

		if (!config.contains("ranks." + this.name)) return;

		this.permissions = config.getStringList(absolute + "permissions");
		this.prefix = config.getString(absolute + "prefix");
		this.suffix = config.getString(absolute + "suffix");
		this.displayName = config.getString(absolute + "displayName");
		this.weight = config.getInt(absolute + "weight");
		this.color = ChatColor.valueOf(config.getString(absolute + "color"));
	}

	@SneakyThrows
	public void save() {
		CompletableFuture.runAsync(() -> {
			YamlConfiguration config = Core.getInstance().getRanksYML().gc();

			String absolute = "ranks." + this.name + ".";

			config.set(absolute + "name", this.name);
			config.set(absolute + "displayName", this.displayName);
			config.set(absolute + "prefix", this.prefix);
			config.set(absolute + "suffix", this.suffix);
			config.set(absolute + "weight", this.weight);
			config.set(absolute + "color", this.color.name());
			config.set(absolute + "permissions", this.permissions);

			try {
				Core.getInstance().getRanksYML().save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@SneakyThrows
	public void delete() {
		try {
			new RankDeletePacket(this).send();
			rankHandler.getRanks().remove(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
