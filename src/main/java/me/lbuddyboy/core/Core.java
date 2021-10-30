package me.lbuddyboy.core;

import lombok.Getter;
import me.blazingtide.zetsu.Zetsu;
import me.lbuddyboy.core.commands.params.RankParameterType;
import me.lbuddyboy.core.commands.params.UUIDParameterType;
import me.lbuddyboy.core.database.DatabaseHandler;
import me.lbuddyboy.core.profile.command.UserCommand;
import me.lbuddyboy.core.profile.grant.command.GrantCommand;
import me.lbuddyboy.core.profile.grant.command.GrantsCommand;
import me.lbuddyboy.core.profile.grant.command.SetRankCommand;
import me.lbuddyboy.core.profile.lProfileHandler;
import me.lbuddyboy.core.punishment.command.BanCommand;
import me.lbuddyboy.core.punishment.command.CheckPunishmentsCommand;
import me.lbuddyboy.core.punishment.command.MuteCommand;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.RankHandler;
import me.lbuddyboy.core.rank.command.*;
import me.lbuddyboy.core.report.ReportHandler;
import me.lbuddyboy.core.util.YamlDoc;
import me.lbuddyboy.libraries.redis.RedisHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:16 PM
 * lCore / me.lbuddyboy.core
 */

@Getter
public class Core extends JavaPlugin {

	@Getter
	private static Core instance;

	private Zetsu zetsu;

	private YamlDoc profilesYML;
	private YamlDoc ranksYML;
	private YamlDoc commandsYML;
	private YamlDoc menusYML;

	private DatabaseHandler databaseHandler;
	private RedisHandler redisHandler;
	private RankHandler rankHandler;
	private lProfileHandler profileHandler;
	private ReportHandler reportHandler;

	@Override
	public void onEnable() {
		instance = this;

		this.saveDefaultConfig();

		this.loadYMLs();
		this.loadCommands();
		this.loadHandlers();
	}

	private void loadYMLs() {
		this.profilesYML = new YamlDoc(this.getDataFolder(), "profiles.yml");
		this.ranksYML = new YamlDoc(this.getDataFolder(), "ranks.yml");
		this.commandsYML = new YamlDoc(this.getDataFolder(), "commands.yml");
		this.menusYML = new YamlDoc(this.getDataFolder(), "menus.yml");
	}

	private void loadHandlers() {
		this.redisHandler = new RedisHandler(this);
		this.databaseHandler = new DatabaseHandler();
		this.rankHandler = new RankHandler();
		this.profileHandler = new lProfileHandler();
		this.reportHandler = new ReportHandler();
	}

	private void loadCommands() {
		this.zetsu = new Zetsu(this);

		this.zetsu.registerParameterAdapter(UUID.class, new UUIDParameterType());
		this.zetsu.registerParameterAdapter(Rank.class, new RankParameterType());

		Arrays.asList(
				new MuteCommand(),
				new BanCommand(),
				new CheckPunishmentsCommand(),
				new UserCommand(),
				new GrantCommand(),
				new GrantsCommand(),
				new SetRankCommand(),
				new RankAddPermissionCommand(),
				new RankCreateCommand(),
				new RankDeleteCommand(),
				new RankEditCommand(),
				new RankInfoCommand(),
				new RankListCommand(),
				new RankRemovePermissionCommand(),
				new RankRenameCommand(),
				new RankSetColorCommand(),
				new RankSetDisplayCommand(),
				new RankSetPrefixCommand(),
				new RankSetWeightCommand()
		).forEach(command -> this.zetsu.registerCommands(command));
	}
}
