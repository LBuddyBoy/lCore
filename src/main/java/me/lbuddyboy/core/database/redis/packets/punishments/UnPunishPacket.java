package me.lbuddyboy.core.database.redis.packets.punishments;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 7:02 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.punishments
 */

@AllArgsConstructor
public class UnPunishPacket implements JedisPacket {

	private UUID target;
	private Punishment punishment;

	@Override
	public void onReceive() {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(this.target);
		if (profile == null) return;

		profile.getPunishments().removeIf(punishment1 -> punishment1.getId() == punishment.getId());
		profile.getPunishments().add(punishment);

		Player player = Bukkit.getPlayer(target);
		if (player != null) {
			if (punishment.getType() == PunishmentType.MUTE) {
				player.sendMessage(CC.translate(Configuration.MUTE_RESOLVED_MESSAGE.getMessage()
						.replaceAll("%text%", punishment.getType().getBroadcastPardonText())
						.replaceAll("%reason%", punishment.getReason())
						.replaceAll("%time%", punishment.getFormattedTimeLeft())
						.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage().replaceAll("%time%", punishment.getFormattedTimeLeft())
						)));
			}
		}

	}

	@Override
	public String getID() {
		return "UnPunish Packet";
	}
}
