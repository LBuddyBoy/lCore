package me.lbuddyboy.core;

import lombok.AllArgsConstructor;
import me.lbuddyboy.libraries.util.CC;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 3:09 PM
 * LBuddyBoy Development / me.lbuddyboy.core
 */

@AllArgsConstructor
public enum MenuConfiguration {

	GRANTS_BUTTON_NAME("menus.grants.button.name", "&6%rank%"),
	GRANTS_BUTTON_LORE("menus.grants.button.lore", Arrays.asList(
			"",
			"&6Added By&7: &f%addedBy%",
			"&6Added At&7: &f%addedAt%",
			"&6Duration&7: &f%duration%",
			"&6Reason&7: &f%reason%",
			"&6Time Left&7: &f%time-left%",
			"",
			"&fClick to remove this grant",
			""
	)),
	GRANTS_BUTTON_REMOVED_LORE("menus.grants.button.removed-lore", Arrays.asList(
			"",
			"&6Added By&7: &f%addedBy%",
			"&6Added At&7: &f%addedAt%",
			"&6Duration&7: &f%duration%",
			"&6Reason&7: &f%reason%",
			"&6Time Left&7: &f%time-left%",
			"",
			"&6Removed At&7: &f%removedAt%",
			"&6Removed By&7: &f%removedBy%",
			"&6Removed For&7: &f%removedFor%",
			""
	)),
	
	GRANTS_MENU_TITLE("menus.grants.menu-title", "&6Grants: %player%");
	
	private final String path;
	private final Object def;

	public String getMessage() {
		return Core.getInstance().getMenusYML().gc().getString(this.path, (String) def).replaceAll("%right_arrow%", CC.UNICODE_ARROWS_RIGHT);
	}

	public List<String> getList() {
		try {
			return Core.getInstance().getMenusYML().gc().getStringList(this.path);
		} catch (Exception ignored) {
		}
		return (List<String>) def;
	}

	public boolean getBoolean() {
		return Core.getInstance().getMenusYML().gc().getBoolean(this.path, (boolean) def);
	}

	public int getNumber() {
		return Core.getInstance().getMenusYML().gc().getInt(this.path, (Integer) def);
	}

}
