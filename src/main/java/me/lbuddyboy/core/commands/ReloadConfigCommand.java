package me.lbuddyboy.core.commands;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 25/11/2021 / 3:30 PM
 * LBuddyBoy Development / me.lbuddyboy.core.commands
 */
public class ReloadConfigCommand {

	@Command(labels = {"reloadconfigs", "reloadconfig", "reloadconfigurations"})
	@Permissible("lcore.command.reloadconfig")
	public void reloadconfigs(CommandSender sender) {
		long startMillis = System.currentTimeMillis();
		try {
			Core.getInstance().getChatYML().reloadConfig();
			Core.getInstance().getRanksYML().reloadConfig();
			Core.getInstance().getMenusYML().reloadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sender.sendMessage(CC.translate("&aAll configuration files reloaded in " + (System.currentTimeMillis() - startMillis) + "ms"));
	}

}
