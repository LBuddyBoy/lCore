package me.lbuddyboy.core.punishment.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.packets.punishments.PunishPacket;
import me.lbuddyboy.core.database.redis.packets.punishments.PunishmentBroadcastPacket;
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
public class WarnCommand {

	@Command(labels = "warn")
	@Permissible("lcore.command.warn")
	public void rankAddPerm(CommandSender sender, @Param("target") UUID uuid, @Param("reason {-p}") String reason) {

		boolean silent = (!reason.contains("-p"));

		UUID senderUUID = (sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		long duration = (JavaUtils.parse(Configuration.WARN_TIME_UNTIL_REMOVE.getMessage()));
		lProfile targetProfile = Core.getInstance().getProfileHandler().getByUUID(uuid);

		if (targetProfile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}

		if (senderUUID != null) {
			if (Core.getInstance().getProfileHandler().getByUUID(senderUUID).getCurrentRank().getWeight() < targetProfile.getCurrentRank().getWeight()) {
				sender.sendMessage(CC.translate("&cYou cannot punish people with a higher rank than you."));
				return;
			}
		}

		String senderDisplay = (senderUUID == null ? "&4Console" : (Core.getInstance().getProfileHandler().getByUUID(senderUUID) == null ? sender.getName() : Core.getInstance().getProfileHandler().getByUUID(senderUUID).getNameWithColor()));
		String targetDisplay = targetProfile.getNameWithColor();

		int warns = targetProfile.getPunishmentsByTypeSorted(PunishmentType.WARN).size();

		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, senderUUID, uuid, duration, System.currentTimeMillis(), reason, silent);

		punishment.alert(senderDisplay, targetDisplay);

		targetProfile.getPunishments().removeIf(punishment1 -> punishment1.getId() == punishment.getId());
		targetProfile.getPunishments().add(punishment);
		targetProfile.save();

		new PunishmentBroadcastPacket(punishment, senderDisplay, targetDisplay).send();
		new PunishPacket(uuid, punishment).send();

		if (warns >= Configuration.MAX_WARNS.getNumber()) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Configuration.MAX_WARNS_COMMAND.getMessage()
					.replaceAll("%player%", Bukkit.getOfflinePlayer(uuid).getName())
			);
		}

		Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			player.sendMessage(CC.translate(Configuration.WARN_MESSAGE.getMessage()
					.replaceAll("%text%", punishment.getType().getBroadcastText())
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%time%", punishment.getFormattedTimeLeft())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage().replaceAll("%time%", punishment.getFormattedTimeLeft())
					)));
		}
	}

}
