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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:39 PM
 * lCore / me.lbuddyboy.core.database.packets.grant
 */

@AllArgsConstructor
public class GrantRemovePacket implements JedisPacket {

	private Grant grant;

	@Override
	public void onReceive() {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(grant.getTarget());
		if (profile != null) {

			Player player = Bukkit.getPlayer(grant.getTarget());
			if (player != null) {
				player.sendMessage(CC.translate(Configuration.GRANT_EXPIRED.getMessage()
						.replaceAll("%time%", grant.getTimeRemaining())
						.replaceAll("%rank%", grant.getRank().getDisplayName())
						.replaceAll("%player%", profile.getName())
				));

			}

			List<Grant> toSearch = new ArrayList<>();
			profile.getGrants().stream().filter(Grant::isRemoved).forEach(toSearch::add);

			profile.getGrants().removeIf(grant1 -> grant1.getId().equals(grant.getId()));
			profile.getGrants().add(grant);

			if (toSearch.size() == 0) {
				profile.getGrants().add(new Grant(UUID.randomUUID(), Core.getInstance().getRankHandler().defaultRank(), null, profile.getUniqueId(), "Default Grant", System.currentTimeMillis(), Long.MAX_VALUE));
				profile.grantNext();
			}

		}

	}

	@Override
	public String getID() {
		return "Grant Remove";
	}
}
