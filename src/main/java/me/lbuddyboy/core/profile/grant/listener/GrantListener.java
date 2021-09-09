package me.lbuddyboy.core.profile.grant.listener;

import me.lbuddyboy.core.profile.grant.GrantBuild;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.uuid.UniqueIDCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:06 AM
 * lCore / me.lbuddyboy.core.profile.grant.listener
 */
public class GrantListener implements Listener {

	public static List<Player> time = new ArrayList<>();
	public static List<Player> reason = new ArrayList<>();
	public static Map<Player, GrantBuild> grantBuildMap = new HashMap<>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		Player p = event.getPlayer();

		if (time.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				time.remove(p);
				return;
			}
			GrantBuild grantBuild = grantBuildMap.get(p);
			grantBuild.setTime(event.getMessage());

			grantBuildMap.put(p, grantBuild);

			p.sendMessage(CC.translate("&aNow, type in the reason for granting " + UniqueIDCache.name(grantBuild.getTarget()) + "&a the " + grantBuild.getRank().getDisplayName() + "&a Rank"));
			time.remove(p);
			reason.add(p);
		} else if (reason.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				reason.remove(p);
				return;
			}
			GrantBuild grantBuild = grantBuildMap.get(p);
			grantBuild.setReason(event.getMessage());

			grantBuildMap.put(p, grantBuild);

			p.chat("/setrank " + UniqueIDCache.name(grantBuild.getTarget()) + " " + grantBuild.getRank().getName() + " " + event.getMessage() + " " + grantBuild.getReason());

			reason.remove(p);
		}

	}

}
