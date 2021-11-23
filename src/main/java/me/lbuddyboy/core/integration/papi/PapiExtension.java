package me.lbuddyboy.core.integration.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.global.GlobalStatistic;
import me.lbuddyboy.core.profile.lProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 2:41 PM
 * LBuddyBoy Development / me.lbuddyboy.core.integration.papi
 */
public class PapiExtension extends PlaceholderExpansion {

	@Override
	public @NotNull String getIdentifier() {
		return "lcore";
	}

	@Override
	public @NotNull String getAuthor() {
		return "LBuddyB0y";
	}

	@Override
	public @NotNull String getVersion() {
		return "0.1";
	}

	@Override
	public String onPlaceholderRequest(Player player, @NotNull String id) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(player.getUniqueId());
		if (id.equalsIgnoreCase("player_rank_color")) {
			return String.valueOf(profile.getCurrentRank().getColor().getChar());
		} else if (id.equalsIgnoreCase("player_rank_name")) {
			return profile.getCurrentRank().getName();
		} else if (id.equalsIgnoreCase("player_rank_prefix")) {
			return profile.getCurrentRank().getPrefix();
		} else if (id.equalsIgnoreCase("player_rank_duration")) {
			return profile.getCurrentGrant().getTimeRemaining();
		} else if (id.equalsIgnoreCase("player_rank_weight")) {
			return String.valueOf(profile.getCurrentRank().getWeight());
		} else if (id.equalsIgnoreCase("player_rank_permissions")) {
			return StringUtils.join(profile.getCurrentRank().getPermissions(), ", ");
		} else if (id.equalsIgnoreCase("player_permissions")) {
			return StringUtils.join(profile.getPermissions(), ", ");
		} else if (id.equalsIgnoreCase("player_ip")) {
			return profile.getCurrentIP();
		}
		for (GlobalStatistic stat : Core.getInstance().getProfileHandler().getGlobalStatistics()) {
			if (id.equalsIgnoreCase("player_" + stat.statisticName() + "_balance")) {
				return String.valueOf(profile.getGlobalStatisticMap().getOrDefault(stat, 0));
			}
		}
		return null;
	}
}
