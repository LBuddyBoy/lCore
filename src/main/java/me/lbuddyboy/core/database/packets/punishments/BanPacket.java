package me.lbuddyboy.core.database.packets.punishments;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.libraries.redis.JedisPacket;
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
public class BanPacket implements JedisPacket {

	private UUID target;
	private Punishment punishment;

	@Override
	public void onReceive() {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(this.target);
		if (profile == null) return;

		profile.getPunishments().add(punishment);
		profile.save();

		Player player = Bukkit.getPlayer(target);
		if (player != null) {
			player.sendMessage(CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
			player.kickPlayer(CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		}

	}

	@Override
	public String getID() {
		return "Ban Packet";
	}
}
