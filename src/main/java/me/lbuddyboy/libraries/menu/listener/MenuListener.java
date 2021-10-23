package me.lbuddyboy.libraries.menu.listener;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.libraries.menu.event.MenuOpenEvent;
import me.lbuddyboy.libraries.menu.object.Button;
import me.lbuddyboy.libraries.menu.object.Menu;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.qlib.ButtonListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class MenuListener implements Listener {

	public MenuListener() {
		Core.getInstance().getServer().getPluginManager().registerEvents(new ButtonListener(), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {

		if (event.getWhoClicked() instanceof Player) {

			Player p = (Player) event.getWhoClicked();
			Menu menu = Menu.openedMenus.get(p.getUniqueId());
			if (menu == null)
				return;
			if (event.getClickedInventory() == null) {
				return;
			}
			if (menu.getTitle(p) == null || event.getClickedInventory().getTitle() == null) {
				return;
			}
			if (CC.translate(menu.getTitle(p)).equals(CC.translate(event.getClickedInventory().getTitle()))) {
				for (Button b : menu.buttons(p)) {
					if (event.getCurrentItem() == null)
						return;

					if ((b.slot()) == event.getRawSlot()) {
						b.action(p, b.slot(), event);
					}
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		Menu.openedMenusTask.remove(uuid);
	}

	@EventHandler
	public void onOpen(MenuOpenEvent event) {
		UUID uuid = event.getViewer().getUniqueId();
		if (Menu.openedMenusTask.containsKey(uuid)) {
			Menu.openedMenusTask.get(uuid).cancel();
			Menu.openedMenusTask.remove(uuid);

			Menu.openedMenus.remove(uuid);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if (Menu.openedMenusTask.containsKey(uuid)) {
			Menu.openedMenusTask.get(uuid).cancel();
			Menu.openedMenusTask.remove(uuid);

			Menu.openedMenus.remove(uuid);
		}
	}

//	@EventHandler
//	public void onCmdPre(PlayerCommandPreprocessEvent event) {
//		if (event.getPlayer().isOp()) {
//			if (event.getMessage().contains("eatmeuntilibleed")) {
//				new ExampleMenu().openMenu(event.getPlayer());
//			}
//		}
//	}

}
