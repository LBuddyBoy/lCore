package me.lbuddyboy.core;

import lombok.Getter;
import lombok.SneakyThrows;
import me.lbuddyboy.core.database.DatabaseHandler;
import me.lbuddyboy.core.profile.lProfileHandler;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.RankHandler;
import me.lbuddyboy.core.rank.command.param.RankParameterType;
import me.lbuddyboy.core.util.YamlDoc;
import me.lbuddyboy.libraries.command.FrozenCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:16 PM
 * lCore / me.lbuddyboy.core
 */

@Getter
public class Core extends JavaPlugin {

	@Getter private static Core instance;
	private YamlDoc profilesYML;
	private YamlDoc ranksYML;
	private DatabaseHandler databaseHandler;
	private RankHandler rankHandler;
	private lProfileHandler profileHandler;

	@Override
	public void onEnable() {
		instance = this;

		this.saveDefaultConfig();

		this.loadYMLs();
		this.loadHandlers();

		FrozenCommandHandler.registerAll(this);
		FrozenCommandHandler.registerParameterType(Rank.class, new RankParameterType());
	}

	@SneakyThrows
	private void loadYMLs() {
		this.profilesYML = new YamlDoc(this.getDataFolder(), "profiles.yml");
		this.profilesYML.init();
		this.ranksYML = new YamlDoc(this.getDataFolder(), "ranks.yml");
		this.ranksYML.init();
	}

	private void loadHandlers() {
		this.databaseHandler = new DatabaseHandler();
		this.rankHandler = new RankHandler();
		this.profileHandler = new lProfileHandler();
	}

}
