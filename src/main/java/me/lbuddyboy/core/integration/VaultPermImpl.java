package me.lbuddyboy.core.integration;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.rank.Rank;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 2:20 PM
 * LBuddyBoy Development / me.lbuddyboy.core.integration
 */
public class VaultPermImpl extends Permission {
	@Override
	public String getName() {
		return "lCore";
	}

	@Override
	public boolean isEnabled() {
		return Core.getInstance().isEnabled();
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return false;
	}

	@Override
	public boolean playerHas(String s, String s1, String s2) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		return profile.getPermissions().contains(s2);
	}

	@Override
	public boolean playerAdd(String s, String s1, String s2) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		if (profile.getPermissions().contains(s2)) {
			return false;
		}
		profile.getPermissions().add(s2);
		return true;
	}

	@Override
	public boolean playerRemove(String s, OfflinePlayer op, String s2) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(op.getUniqueId());
		if (!profile.getPermissions().contains(s2)) {
			return false;
		}
		profile.getPermissions().remove(s2);
		return true;
	}

	@Override
	public boolean playerRemove(String s, String s1, String s2) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		if (!profile.getPermissions().contains(s2)) {
			return false;
		}
		profile.getPermissions().remove(s2);
		return true;
	}

	@Override
	public boolean groupHas(String s, String s1, String s2) {
		Rank rank = Core.getInstance().getRankHandler().getByName(s1);
		return rank.getPermissions().contains(s2);
	}

	@Override
	public boolean groupAdd(String s, String s1, String s2) {
		Rank rank = Core.getInstance().getRankHandler().getByName(s1);
		if (rank.getPermissions().contains(s2)) {
			return false;
		}
		rank.getPermissions().add(s2);
		return true;
	}

	@Override
	public boolean groupRemove(String s, String s1, String s2) {
		Rank rank = Core.getInstance().getRankHandler().getByName(s1);
		if (!rank.getPermissions().contains(s2)) {
			return false;
		}
		rank.getPermissions().remove(s2);
		return true;
	}

	@Override
	public boolean playerInGroup(String s, String s1, String s2) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		Rank rank = Core.getInstance().getRankHandler().getByName(s2);
		return profile.getCurrentRank().getName().equals(rank.getName());
	}

	@Override
	public boolean playerAddGroup(String s, String s1, String s2) {
		return false;
	}

	@Override
	public boolean playerRemoveGroup(String s, String s1, String s2) {
		return false;
	}

	@Override
	public String[] getPlayerGroups(String s, String s1) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());

		return new String[0];
	}

	@Override
	public String getPrimaryGroup(String s, String s1) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		return profile.getCurrentRank().getName();
	}

	@Override
	public String[] getGroups() {
		return new String[0];
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}
}
