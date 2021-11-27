package me.lbuddyboy.core;

import lombok.Getter;
import me.blazingtide.zetsu.Zetsu;
import me.lbuddyboy.core.commands.*;
import me.lbuddyboy.core.commands.params.GamemodeParameterType;
import me.lbuddyboy.core.commands.params.RankParameterType;
import me.lbuddyboy.core.commands.params.UUIDParameterType;
import me.lbuddyboy.core.database.mongo.MongoHandler;
import me.lbuddyboy.core.database.redis.RedisHandler;
import me.lbuddyboy.core.integration.VaultChatImpl;
import me.lbuddyboy.core.integration.VaultPermImpl;
import me.lbuddyboy.core.integration.papi.PapiExtension;
import me.lbuddyboy.core.profile.command.UserCommand;
import me.lbuddyboy.core.profile.grant.command.GrantCommand;
import me.lbuddyboy.core.profile.grant.command.GrantsCommand;
import me.lbuddyboy.core.profile.grant.command.SetRankCommand;
import me.lbuddyboy.core.profile.lProfileHandler;
import me.lbuddyboy.core.punishment.command.*;
import me.lbuddyboy.core.punishment.command.resolve.UnBanCommand;
import me.lbuddyboy.core.punishment.command.resolve.UnBlacklistCommand;
import me.lbuddyboy.core.punishment.command.resolve.UnMuteCommand;
import me.lbuddyboy.core.punishment.command.resolve.UnWarnCommand;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.RankHandler;
import me.lbuddyboy.core.rank.command.*;
import me.lbuddyboy.core.util.YamlDoc;
import me.lbuddyboy.libraries.util.CC;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.ServicePriority;
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
	private YamlDoc menusYML;
	private YamlDoc chatYML;

	private MongoHandler mongoHandler;
	private RedisHandler redisHandler;
	private RankHandler rankHandler;
	private lProfileHandler profileHandler;

	@Override
	public void onEnable() {
		instance = this;

		this.saveDefaultConfig();

		this.loadYMLs();
		this.loadCommands();
		this.loadHandlers();
		this.loadIntegrations();
		Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
		Bukkit.getConsoleSender().sendMessage(CC.translate("&6&llCore&f has &aenabled&f successfully."));
		Bukkit.getConsoleSender().sendMessage(CC.translate(" &7- &6Author&f: LBuddyB0y/LBuddyBoy"));
		Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
	}

	@Override
	public void onDisable() {
		if (Configuration.REDIS_SYNC.getBoolean()) {
			this.redisHandler.close();
		}
	}

	private void loadIntegrations() {
		if (Configuration.VAULT_INTEGRATION.getBoolean()) {
			getServer().getServicesManager().register(Permission.class, new VaultPermImpl(), this, ServicePriority.Highest);
			getServer().getServicesManager().register(Chat.class, new VaultChatImpl(new VaultPermImpl()), this, ServicePriority.Highest);
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully integrated with the &6Vault&f plugin."));
		}
		if (Configuration.PAPI_INTEGRATION.getBoolean()) {
			new PapiExtension().register();
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully integrated with the &6PlaceholderAPI&f plugin."));
		}
	}

	private void loadYMLs() {
		this.profilesYML = new YamlDoc(this.getDataFolder(), "profiles.yml");
		this.ranksYML = new YamlDoc(this.getDataFolder(), "ranks.yml");
		this.chatYML = new YamlDoc(this.getDataFolder(), "chat.yml");
		this.menusYML = new YamlDoc(this.getDataFolder(), "menus.yml");
	}

	private void loadHandlers() {
		if (Configuration.REDIS_SYNC.getBoolean()) {
			this.redisHandler = new RedisHandler(this);
		}
		if (Configuration.STORAGE_MONGO.getBoolean()) {
			this.mongoHandler = new MongoHandler();
		}
		this.rankHandler = new RankHandler();
		this.profileHandler = new lProfileHandler();
	}

	private void loadCommands() {
		this.zetsu = new Zetsu(this);

		this.zetsu.registerParameterAdapter(UUID.class, new UUIDParameterType());
		this.zetsu.registerParameterAdapter(Rank.class, new RankParameterType());
		this.zetsu.registerParameterAdapter(GameMode.class, new GamemodeParameterType());

		Arrays.asList(
				new FeedCommand(),
				new HealCommand(),
				new GamemodeCommand(),
				new ReloadConfigCommand(),
				new AltsCommand(),
				new UnMuteCommand(),
				new UnBanCommand(),
				new UnWarnCommand(),
				new UnBlacklistCommand(),
				new BlacklistCommand(),
				new KickCommand(),
				new MuteCommand(),
				new BanCommand(),
				new WarnCommand(),
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
				new ReportCommand(),
				new HelpopCommand(),
				new RankSetWeightCommand()
		).forEach(command -> this.zetsu.registerCommands(command));
	}
}
