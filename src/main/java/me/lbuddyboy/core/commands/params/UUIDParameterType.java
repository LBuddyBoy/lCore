package me.lbuddyboy.core.commands.params;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.lbuddyboy.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 10:46 AM
 * LBuddyBoy Development / me.lbuddyboy.core.commands.params
 */
public class UUIDParameterType implements ParameterAdapter<UUID> {

	@Override
	public UUID process(String str) {
		return Core.getInstance().getServer().getOfflinePlayer(str).getUniqueId();
	}

	@Override
	public void processException(CommandSender sender, String given, Exception exception) {
		sender.sendMessage(ChatColor.RED + "'" + given + "' could not be found.");
	}

	@Override
	public @Nullable List<String> processTabComplete(@NotNull CommandSender sender, @NotNull CachedCommand command) {
		List<String> toReturn = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			toReturn.add(p.getName());
		}
		return toReturn;
	}
}
