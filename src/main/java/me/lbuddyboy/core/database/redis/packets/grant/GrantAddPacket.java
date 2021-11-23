package me.lbuddyboy.core.database.redis.packets.grant;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:39 PM
 * lCore / me.lbuddyboy.core.database.packets.grant
 */

@AllArgsConstructor
public class GrantAddPacket implements JedisPacket {

	private Grant grant;

	@Override
	public void onReceive() {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(grant.getTarget());
		if (profile != null) {
			profile.getGrants().add(grant);
			profile.grantNext();

			Player player = Bukkit.getPlayer(grant.getTarget());
			if (player != null) {
				player.sendMessage(CC.translate(Configuration.GRANTED_SENDER.getMessage()
						.replaceAll("%time%", grant.getTimeRemaining())
						.replaceAll("%rank%", grant.getRank().getDisplayName())
						.replaceAll("%player%", profile.getName())
				));

			}

		}

	}

	@Override
	public String getID() {
		return "Grant Add";
	}
}
