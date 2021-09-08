package me.lbuddyboy.core;

import lombok.Getter;
import me.lbuddyboy.core.database.DatabaseHandler;
import me.lbuddyboy.core.profile.ProfileListener;
import me.lbuddyboy.core.rank.Rank;
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
	private DatabaseHandler databaseHandler;

	@Override
	public void onEnable() {
		instance = this;

		this.saveDefaultConfig();

		this.handleDatabases();
		Rank.loadAllRanks();

		this.loadHandlers();
		this.loadListeners();

		FrozenCommandHandler.registerAll(this);
	}

	private void handleDatabases() {
		this.databaseHandler = new DatabaseHandler();
	}

	private void loadHandlers() {

	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new ProfileListener(), this);
	}

}
