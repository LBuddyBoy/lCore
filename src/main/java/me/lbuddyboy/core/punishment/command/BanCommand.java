package me.lbuddyboy.core.punishment.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.punishments.BanPacket;
import me.lbuddyboy.core.database.packets.punishments.PunishmentBroadcastPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:54 AM
 * lCore / me.lbuddyboy.core.punishment.command
 */
public class BanCommand {

	@Command(labels = "ban")
	@Permissible("lcore.command.ban")
	public void rankAddPerm(CommandSender sender, @Param("target")UUID uuid, @Param("time") String time, @Param("reason {-p}") String reason) {

		boolean silent = (!reason.contains("-p"));

		UUID senderUUID = (sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		long duration = (time.equalsIgnoreCase("perm") ? Long.MAX_VALUE : JavaUtils.parse(time));
		lProfile senderProfile = Core.getInstance().getProfileHandler().getByUUID(senderUUID);
		lProfile targetProfile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		String senderDisplay = (senderUUID == null ? "&4Console" : (senderProfile == null ? sender.getName() : senderProfile.getNameWithColor()));
		String targetDisplay = (senderUUID == null ? "&4Console" : (targetProfile == null ? sender.getName() : targetProfile.getNameWithColor()));

		if (targetProfile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}

		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BAN, senderUUID, uuid, duration, System.currentTimeMillis(), reason, silent);

		new PunishmentBroadcastPacket(punishment, senderDisplay, targetDisplay).send();
		new BanPacket(uuid, punishment).send();

		targetProfile.getPunishments().add(punishment);
		targetProfile.save();

		Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			player.sendMessage(CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
			player.kickPlayer(CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		}
	}

}
