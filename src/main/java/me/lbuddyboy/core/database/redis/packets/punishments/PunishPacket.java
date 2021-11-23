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
public class PunishPacket implements JedisPacket {

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
			punishment.sendPunishInfo(player);
			if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.BLACKLIST || punishment.getType() == PunishmentType.KICK) {
				player.kickPlayer(CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
						.replaceAll("%reason%", punishment.getReason())
						.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
			}
		}

	}

	@Override
	public String getID() {
		return "Punish Packet";
	}
}
