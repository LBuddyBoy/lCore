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
public enum Configuration {

	SERVER_NAME("server-name", "lCore"),
	VAULT_INTEGRATION("vault-integration", false),
	PAPI_INTEGRATION("papi-integration", false),
	SERVER_TIMEZONE("timezone", "EST"),
	STORAGE_MONGO("mongo.enabled", false),
	REDIS_SYNC("redis.enabled", false),

	// Punishments

	ALTS_OFFLINE_COLOR("punish.alts.offline", "&7"),
	ALTS_ONLINE_COLOR("punish.alts.online", "&a"),
	ALTS_MUTED_COLOR("punish.alts.muted", "&e"),
	ALTS_BANNED_COLOR("punish.alts.banned", "&c"),
	ALTS_BLACKLISTED_COLOR("punish.alts.blacklisted", "&4"),
	ALTS_HEADER("punish.alts.header", "\\n&6&lAlts\\n"),
	ALTS_INFO("punish.alts.info-line", "&7Offline &f- &aOnline &f- &eMuted &f- &cBanned &f- &4Blacklisted"),

	WARN_DISPLAY("punish.warn.display", "&a&lWarn"),
	KICK_DISPLAY("punish.kick.display", "&e&lKick"),
	MUTE_DISPLAY("punish.mute.display", "&6&lMute"),
	BAN_DISPLAY("punish.ban.display", "&c&lBan"),
	BLACKLIST_DISPLAY("punish.blacklist.display", "&0&lBlacklist"),
	MAX_WARNS_COMMAND("punish.warn.warn-command", "ban %player% 3d Reached maximum amount of warns"),
	MAX_WARNS("punish.warn.warns-before-command", 3),
	WARN_TIME_UNTIL_REMOVE("punish.warn.warns-last-time", "7d"),

	MUTE_MESSAGE("punish.mute.message", "&cYou have been muted for %reason%\n&cExpires: %time%"),
	MUTE_RESOLVED_MESSAGE("punish.mute.resolved-message", "&aYour mute has been lifted. You may now chat."),
	WARN_MESSAGE("punish.warn.message", "&cYou have been muted for %reason%\n&cExpires: 7 days"),
	BLACKLIST_KICK_MESSAGE("punish.blacklist.kick-message", "&cYour account is blacklisted from the Example Network.\n&cYour account is blacklisted from the Example Network\\n&cThis punishment cannot be appealed\\n"),
	BAN_KICK_MESSAGE("punish.ban.kick-message", "&cYour account is banned from the Example Network.\n%temp-format%\n\n&cIf you feel this punishment is unjust, you may appeal at:\n&ehttps://www.lbuddyboy.me"),
	BAN_TEMPORARY_FORMAT("punish.ban.temporary-format", "&cPunishment expires in &e%time%"),
	KICK_KICK_MESSAGE("punish.kick.kick-message", "&cYou have been kicked from the server.\n&cReason: %reason%"),

	// Profiles

	PROFILE_HASHED_IPS("use-protected-ip-storage", true),
	FAILED_TO_LOAD_PROFILE("failed-to-load-profile", "&cFailed to load your profile. Contact an admin."),
	INVALID_PROFILE("invalid-profile", "&cCould not find a profile with that name."),

	// Reports

	REPORT_SENDER("reports.sent-sender", "&aWe have received your request for help and we will assist you shortly."),
	REPORT_TP_SERVER("reports.go-to-server-message", "&7(( Click here to go to their server ))"),
	REPORT_TP_SENDER("reports.tp-sender-message", "&7(( Click here to go to teleport to the sender ))"),
	REPORT_TP_TARGET("reports.tp-target-message", "&7(( Click here to go to teleport to the target ))"),
	REPORT_STAFF_RESOLVED("reports.staff-resolved-message", "&aSuccessfully resolved that report. It has now been moved in to the resolved reports section."),
	REPORT_STAFF_DELETED("reports.staff-deleted-message", "&aSuccessfully deleted that report. It can no longer be recovered."),
	REPORT_STAFF_MESSAGE("reports.report-staff-message", Arrays.asList(
			"&6&lREPORT &f- &e%sender%&f needs assistance on &e%server%&f.",
			"&6Target&f: %target%",
			"&6Reason&f: %reason%"
	)),
	HELPOP_STAFF_MESSAGE("reports.helpop-staff-message", Arrays.asList(
			"&6&lHELPOP &f- &e%sender%&f needs assistance on &e%server%&f.",
			"&6Target&f: %target%",
			"&6Reason&f: %reason%"
	)),

	// Grants

	GRANT_ACTIVE("grant.active-display", "&a&lACTIVE"),
	GRANT_INACTIVE("grant.inactive-display", "&c&lINACTIVE"),
	GRANT_EXPIRED("grant.granted.expired", "&aYour %rank% has just expired&a."),
	GRANTED_SENDER("grant.granted.sender", "&aYou have just granted %player% &athe %rank%&a for &e%time%&a."),
	GRANTED_TARGET("grant.granted.target", "&aYou have just been granted &athe %rank%&a for &e%time%&a."),

	// Essentials

	ESSENTIALS_GAMEMODE_SWITCH_MESSAGE("essentials.gamemode.message", "&fSwitched to &6%mode%&f mode."),
	ESSENTIALS_FEED("essentials.feed.message", "&aYou are now full hunger."),
	ESSENTIALS_HEAL("essentials.heal.message", "&aYou are now full health."),

	// Ranks

	LIST_RANKS_HEADER("rank.list.header", Arrays.asList(
			" ",
			"&6&lRank Info",
			" "
	)),
	RANK_EDIT_MENU_TITLE("rank.edit.menu-title", "&6Editing: &r%rank%"),
	RANK_NONEXISTANT("rank.nonexistant", "&cThat rank does not exist."),
	RANK_EXISTS("rank.exists", "&cThat rank already exists"),
	LIST_RANKS_FORMAT("rank.list.format", "&7%right_arrow%&r %rank% &7(Weight: %weight%)"),
	CREATED_RANK("rank.created", "&aSuccessfully created the %rank%&a."),
	SET_RANK_PREFIX("rank.setPrefix", "&aSucessfully set the prefix of %rank% to %new%"),
	SET_RANK_DISPLAY("rank.setDisplay", "&aSucessfully set the displayname of %rank% to %new%"),
	RANK_RENAME("rank.rename", "&aSucessfully renamed the %rank% to %new%"),
	SET_RANK_WEIGHT("rank.setWeight", "&aSucessfully set the weight of %rank% to %new%"),
	SET_RANK_COLOR("rank.setColor", "&aSucessfully set the color of %rank% to %new%"),
	RANK_ADDED_PERM("rank.addPerm", "&aSucessfully added the %perm% permission to %rank%"),
	RANK_REMOVED_PERM("rank.removePerm", "&aSucessfully removed the %perm% permission from %rank%"),
	RANK_ALREADY_HAS_PERM("rank.hasPerm", "&cThat rank already has that permission."),
	RANK_DOES_NOT_HAVE_PERM("rank.noPerm", "&cThat rank does not have that permission."),
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

	public boolean getBoolean() {
		return Core.getInstance().getConfig().getBoolean(this.path, (boolean) def);
	}

	public int getNumber() {
		return Core.getInstance().getConfig().getInt(this.path, (Integer) def);
	}

}
