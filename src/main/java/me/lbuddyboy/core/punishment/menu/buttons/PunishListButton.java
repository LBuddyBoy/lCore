package me.lbuddyboy.core.punishment.menu.buttons;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.core.punishment.menu.PunishmentsMenu;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.qlib.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 24/10/2021 / 11:23 AM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.menu.buttons
 */

@AllArgsConstructor
public class PunishListButton extends Button {

	private lProfile target;
	private PunishmentType type;

	private static final Core inst = Core.getInstance();

	@Override
	public String getName(Player var1) {
		return CC.translate(inst.getMenusYML().gc().getString("menus.punishments-main." + type.name().toLowerCase() + ".name").replaceAll("%amount%", "" + target.getPunishmentsByType(type).size()));
	}

	@Override
	public List<String> getDescription(Player var1) {
		return null;
	}

	@Override
	public Material getMaterial(Player var1) {
		return Material.valueOf(inst.getMenusYML().gc().getString("menus.punishments-main." + type.name().toLowerCase() + ".material"));
	}

	@Override
	public byte getDamageValue(Player player) {
		return (byte) inst.getMenusYML().gc().getInt("menus.punishments-main." + type.name().toLowerCase() + ".data");
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		new PunishmentsMenu(target, type).openMenu(player);
	}
}
