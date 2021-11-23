package me.lbuddyboy.core.integration;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 2:20 PM
 * LBuddyBoy Development / me.lbuddyboy.core.integration
 */
public class VaultChatImpl extends Chat {
	public VaultChatImpl(Permission perms) {
		super(perms);
	}

	@Override
	public String getName() {
		return "lCore";
	}

	@Override
	public boolean isEnabled() {
		return Core.getInstance().isEnabled();
	}

	@Override
	public String getPlayerPrefix(String s, String s1) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		return CC.translate(profile.getCurrentRank().getPrefix());
	}

	@Override
	public void setPlayerPrefix(String s, String s1, String s2) {

	}

	@Override
	public String getPlayerSuffix(String s, String s1) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		return CC.translate(profile.getCurrentRank().getSuffix());
	}

	@Override
	public void setPlayerSuffix(String s, String s1, String s2) {

	}

	@Override
	public String getGroupPrefix(String s, String s1) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		return CC.translate(profile.getCurrentRank().getPrefix());
	}

	@Override
	public void setGroupPrefix(String s, String s1, String s2) {

	}

	@Override
	public String getGroupSuffix(String s, String s1) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(Bukkit.getOfflinePlayer(s1).getUniqueId());
		return CC.translate(profile.getCurrentRank().getSuffix());
	}

	@Override
	public void setGroupSuffix(String s, String s1, String s2) {

	}

	@Override
	public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
		return 0;
	}

	@Override
	public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

	}

	@Override
	public int getGroupInfoInteger(String s, String s1, String s2, int i) {
		return 0;
	}

	@Override
	public void setGroupInfoInteger(String s, String s1, String s2, int i) {

	}

	@Override
	public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
		return 0;
	}

	@Override
	public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

	}

	@Override
	public double getGroupInfoDouble(String s, String s1, String s2, double v) {
		return 0;
	}

	@Override
	public void setGroupInfoDouble(String s, String s1, String s2, double v) {

	}

	@Override
	public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
		return false;
	}

	@Override
	public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

	}

	@Override
	public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
		return false;
	}

	@Override
	public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

	}

	@Override
	public String getPlayerInfoString(String s, String s1, String s2, String s3) {
		return null;
	}

	@Override
	public void setPlayerInfoString(String s, String s1, String s2, String s3) {

	}

	@Override
	public String getGroupInfoString(String s, String s1, String s2, String s3) {
		return null;
	}

	@Override
	public void setGroupInfoString(String s, String s1, String s2, String s3) {

	}
}
