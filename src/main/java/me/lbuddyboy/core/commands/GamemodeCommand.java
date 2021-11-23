package me.lbuddyboy.core.commands;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.libraries.util.CC;
import org.apache.commons.lang.WordUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 10:45 AM
 * LBuddyBoy Development / me.lbuddyboy.core.commands
 */
public class GamemodeCommand {

	@Command(labels = {"gamemode", "gm"})
	@Permissible("lcore.command.gamemode")
	public void gamemode(Player sender, @Param("mode") GameMode mode) {
		if (mode == null) {
			sender.sendMessage(CC.translate("&cCould not find a gamemode with that name."));
			return;
		}
		sender.setGameMode(mode);
		sender.sendMessage(CC.translate(Configuration.ESSENTIALS_GAMEMODE_SWITCH_MESSAGE.getMessage().replaceAll("%mode%", WordUtils.capitalize(mode.name().toLowerCase()))));
	}
	@Command(labels = "gmc")
	@Permissible("lcore.command.gamemode")
	public void gmc(Player sender) {
		sender.setGameMode(GameMode.CREATIVE);
		sender.sendMessage(CC.translate(Configuration.ESSENTIALS_GAMEMODE_SWITCH_MESSAGE.getMessage().replaceAll("%mode%", "Creative")));
	}
	@Command(labels = "gms")
	@Permissible("lcore.command.gamemode")
	public void gms(Player sender) {
		sender.setGameMode(GameMode.SURVIVAL);
		sender.sendMessage(CC.translate(Configuration.ESSENTIALS_GAMEMODE_SWITCH_MESSAGE.getMessage().replaceAll("%mode%", "Survival")));
	}
}
