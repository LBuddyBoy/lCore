package me.lbuddyboy.core.punishment.command.resolve;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.packets.punishments.PunishmentBroadcastPacket;
import me.lbuddyboy.core.database.redis.packets.punishments.UnPunishPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:54 AM
 * lCore / me.lbuddyboy.core.punishment.command
 */
public class UnMuteCommand {

	@Command(labels = "unmute")
	@Permissible("lcore.command.unmute")
	public void rankAddPerm(CommandSender sender, @Param("target") UUID uuid, @Param("reason {-p}") String reason) {

		boolean silent = (!reason.contains("-p"));

		UUID senderUUID = (sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		lProfile targetProfile = Core.getInstance().getProfileHandler().getByUUID(uuid);

		if (targetProfile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}

		String senderDisplay = (senderUUID == null ? "&4Console" : (Core.getInstance().getProfileHandler().getByUUID(senderUUID) == null ? sender.getName() : Core.getInstance().getProfileHandler().getByUUID(senderUUID).getNameWithColor()));
		String targetDisplay = targetProfile.getNameWithColor();

		if (!targetProfile.hasActivePunishment(PunishmentType.MUTE)) {
			sender.sendMessage(CC.translate("&cThat player is not muted."));
			return;
		}

		Punishment punishment = targetProfile.getActivePunishment(PunishmentType.MUTE);

		punishment.setSilent(silent);
		punishment.setResolved(true);
		punishment.setResolvedBy(senderUUID);
		punishment.setResolvedAt(System.currentTimeMillis());
		punishment.setResolvedReason(reason);

		targetProfile.save();

//		punishment.alert(senderDisplay, targetDisplay);

		new PunishmentBroadcastPacket(punishment, senderDisplay, targetDisplay).send();
		new UnPunishPacket(uuid, punishment).send();

		Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			player.sendMessage(CC.translate(Configuration.MUTE_RESOLVED_MESSAGE.getMessage()
					.replaceAll("%text%", punishment.getType().getBroadcastPardonText())
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%time%", punishment.getFormattedTimeLeft())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage().replaceAll("%time%", punishment.getFormattedTimeLeft())
					)));
		}
	}

}
