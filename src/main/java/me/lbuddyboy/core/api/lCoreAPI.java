package me.lbuddyboy.core.api;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.rank.Rank;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 4:14 PM
 * LBuddyBoy Development / me.lbuddyboy.api
 */
public class lCoreAPI {

	/**
	 * get a players profile
	 *
	 * @param toSearch the player's uuid
	 * @return the player's profile that was found under that UUID
	 */
	public static lProfile getProfileByUUID(UUID toSearch) {
		return Core.getInstance().getProfileHandler().getByUUID(toSearch);
	}

	/**
	 * get a rank by a name
	 *
	 * @param toSearch the rank name
	 * @return the rank found under that name.
	 */
	public static Rank getRankByName(String toSearch) {
		return Core.getInstance().getRankHandler().getByName(toSearch);
	}

}
