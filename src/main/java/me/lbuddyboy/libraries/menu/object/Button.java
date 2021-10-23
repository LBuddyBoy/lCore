package me.lbuddyboy.libraries.menu.object;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

	public abstract ItemStack stack(Player player);
	public abstract int slot();
	public void action(Player player, int slot, InventoryClickEvent event) {

	}

}
