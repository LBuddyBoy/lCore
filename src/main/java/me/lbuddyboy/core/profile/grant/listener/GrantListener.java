package me.lbuddyboy.core.profile.grant.listener;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.grant.GrantRemovePacket;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.profile.grant.GrantBuild;
import me.lbuddyboy.core.profile.grant.command.SetRankCommand;
import me.lbuddyboy.core.profile.grant.menu.GrantsMenu;
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
	public static List<Player> remove = new ArrayList<>();
	public static Map<Player, GrantBuild> grantBuildMap = new HashMap<>();
	public static Map<Player, Grant> grantMap = new HashMap<>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		Player p = event.getPlayer();

		if (time.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				time.remove(p);
				return;
			}
			GrantBuild grantBuild = grantBuildMap.get(p);
			grantBuild.setTime(event.getMessage());

			grantBuildMap.put(p, grantBuild);

			p.sendMessage(CC.translate("&aNow, type in the reason for granting " + UniqueIDCache.name(grantBuild.getTarget()) + "&a the " + grantBuild.getRank().getDisplayName() + "&a Rank"));
			time.remove(p);
			reason.add(p);
			event.setCancelled(true);
		} else if (reason.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				reason.remove(p);
				return;
			}
			GrantBuild grantBuild = grantBuildMap.get(p);
			grantBuild.setReason(event.getMessage());

			grantBuildMap.put(p, grantBuild);

			SetRankCommand.setRank(p, grantBuild.getTarget(), grantBuild.getRank(), grantBuild.getTime(), grantBuild.getReason());

			reason.remove(p);
		} else if (remove.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				remove.remove(p);
				return;
			}
			Grant grant = grantMap.get(p);
			Profile target = Core.getInstance().getProfileHandler().getByUUID(grant.getTarget());
			grant.setRemovedBy(p.getUniqueId());
			grant.setRemoved(true);
			grant.setRemovedAt(System.currentTimeMillis());
			grant.setRemovedReason(event.getMessage());

			new GrantRemovePacket(grant).send();

			new GrantsMenu(target).openMenu(p);

			remove.remove(p);
		}

	}

}
