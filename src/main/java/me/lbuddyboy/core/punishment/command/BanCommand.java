package me.lbuddyboy.core.punishment.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.punishments.BanPacket;
import me.lbuddyboy.core.database.packets.punishments.PunishmentBroadcastPacket;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:54 AM
 * lCore / me.lbuddyboy.core.punishment.command
 */
public class BanCommand {

	@Command(names = "ban", permission = "lcore.command.ban")
	public static void ban(CommandSender sender, @Param(name = "target")UUID uuid, @Param(name = "time") String time, @Param(name = "reason {-p}", wildcard = true) String reason) {

		boolean silent = (!reason.contains("-p"));

		UUID senderUUID = (sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		long duration = (time.equalsIgnoreCase("perm") ? Long.MAX_VALUE : JavaUtils.parse(time));
		Profile senderProfile = Core.getInstance().getProfileHandler().getByUUID(senderUUID);
		Profile targetProfile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		String senderDisplay = (senderUUID == null ? "&4Console" : (senderProfile == null ? sender.getName() : senderProfile.getNameWithColor()));
		String targetDisplay = (senderUUID == null ? "&4Console" : (targetProfile == null ? sender.getName() : targetProfile.getNameWithColor()));

		Punishment punishment = new Punishment(PunishmentType.BAN, senderUUID, uuid, duration, System.currentTimeMillis(), reason, silent);

		new PunishmentBroadcastPacket(punishment, senderDisplay, targetDisplay).send();
		new BanPacket(uuid, punishment).send();

		if (targetProfile != null) {
			targetProfile.getPunishments().add(punishment);
			targetProfile.save();
		}
	}

}
