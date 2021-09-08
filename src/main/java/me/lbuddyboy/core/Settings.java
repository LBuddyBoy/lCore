package me.lbuddyboy.core;

import lombok.AllArgsConstructor;
import me.lbuddyboy.libraries.util.CC;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:10 PM
 * lCore / me.lbuddyboy.core
 */

@AllArgsConstructor
public enum Settings {

	FAILED_TO_LOAD_PROFILE("failed-to-load-profile", "&cFailed to load your profile. Contact an admin."),

	// Ranks

	LIST_RANKS_HEADER("rank.list.header", Arrays.asList(
			" ",
			"&6&lRank Info",
			" "
	)),
	RANK_NONEXISTANT("rank.nonexistant", "&cThat rank does not exist."),
	RANK_EXISTS("rank.exists", "&cThat rank already exists"),
	LIST_RANKS_FORMAT("rank.list.format", "&7%right_arrow%&r %rank%"),
	CREATED_RANK("rank.created", "&aSuccessfully created the %rank%&a."),
	DELETED_RANK("rank.deleted", "&aSuccessfully deleted the %rank%&a.");

	private final String path;
	private final Object def;

	public String getMessage() {
		return Core.getInstance().getConfig().getString(this.path, (String) def).replaceAll("%right_arrow%", CC.UNICODE_ARROWS_RIGHT);
	}

	public List<String> getList() {
		try {
			return Core.getInstance().getConfig().getStringList(this.path);
		} catch (Exception ignored) {
		}
		return (List<String>) def;
	}

	public int getNumber() {
		return Core.getInstance().getConfig().getInt(this.path, (Integer) def);
	}

}
