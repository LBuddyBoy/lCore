package me.lbuddyboy.core.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:33 AM
 * lCore / me.lbuddyboy.core.punishment
 */

@AllArgsConstructor
@Getter
public enum PunishmentType {

	BAN("Ban", "&c&lBan", "banned");

	private final String name;
	private final String displayName;
	private final String broadcastText;

}
