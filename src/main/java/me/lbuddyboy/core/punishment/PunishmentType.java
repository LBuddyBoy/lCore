package me.lbuddyboy.core.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lbuddyboy.core.Configuration;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:33 AM
 * lCore / me.lbuddyboy.core.punishment
 */

@AllArgsConstructor
@Getter
public enum PunishmentType {

	WARN("Warn", Configuration.WARN_DISPLAY.getMessage(), "warned", "unwarned"),
	KICK("Kick", Configuration.KICK_DISPLAY.getMessage(), "kicked", ""),
	MUTE("Mute", Configuration.MUTE_DISPLAY.getMessage(), "muted", "unmuted"),
	BAN("Ban", Configuration.BAN_DISPLAY.getMessage(), "banned", "unbanned"),
	BLACKLIST("Blacklist", Configuration.BLACKLIST_DISPLAY.getMessage(), "blacklisted", "unblacklisted");

	private final String name;
	private final String displayName;
	private final String broadcastText;
	private final String broadcastPardonText;

}
