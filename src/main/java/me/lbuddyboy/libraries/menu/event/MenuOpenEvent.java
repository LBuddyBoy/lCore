package me.lbuddyboy.libraries.menu.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/07/2021 / 9:18 AM
 * GKits / me.lbuddyboy.gkits.util.menu.event
 */
public class MenuOpenEvent extends Event {

	@Getter private static final HandlerList handlerList = new HandlerList();

	@Getter private final Player viewer;

	public MenuOpenEvent(Player viewer) {
		this.viewer = viewer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
