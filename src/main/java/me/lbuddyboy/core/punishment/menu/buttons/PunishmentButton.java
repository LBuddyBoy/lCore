package me.lbuddyboy.core.punishment.menu.buttons;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.listener.PunishmentListener;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.qlib.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 24/10/2021 / 11:46 AM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.menu.buttons
 */

@AllArgsConstructor
public class PunishmentButton extends Button {

	private Punishment punishment;

	private static Core inst = Core.getInstance();

	@Override
	public String getName(Player var1) {
		return CC.translate(punishment.getType().getDisplayName());
	}

	@Override
	public List<String> getDescription(Player var1) {
		if (punishment.isResolved()) {
			List<String> lore = new ArrayList<>();
			for (String s : inst.getMenusYML().gc().getStringList("menus.punishments-list.resolved-button.lore")) {
				lore.add(s
						.replaceAll("%reason%", punishment.getReason())
						.replaceAll("%sentAt%", punishment.getAddedAtDate())
						.replaceAll("%duration%", punishment.getDurationString())
						.replaceAll("%time-left%", punishment.getFormattedTimeLeft())
						.replaceAll("%resolvedAt%", punishment.getResolvedAtDate())
						.replaceAll("%resolvedFor%", punishment.getResolvedReason())
						.replaceAll("%resolvedBy%", UUIDCache.name(punishment.getResolvedBy()))
						.replaceAll("%sender%", UUIDCache.name(punishment.getSender())));
			}
			return CC.translate(lore);
		}
		List<String> lore = new ArrayList<>();
		for (String s : inst.getMenusYML().gc().getStringList("menus.punishments-list.default-button.lore")) {
			lore.add(s
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%sentAt%", punishment.getAddedAtDate())
					.replaceAll("%time-left%", punishment.getFormattedTimeLeft())
					.replaceAll("%duration%", punishment.getDurationString())
					.replaceAll("%sender%", UUIDCache.name(punishment.getSender())));
		}
		return CC.translate(lore);
	}

	@Override
	public Material getMaterial(Player var1) {
		if (punishment.isResolved()) {
			return Material.valueOf(inst.getMenusYML().gc().getString("menus.punishments-list.resolved-button.material"));
		}
		return Material.valueOf(inst.getMenusYML().gc().getString("menus.punishments-list.default-button.material"));
	}

	@Override
	public byte getDamageValue(Player player) {
		if (punishment.isResolved()) {
			return (byte) inst.getMenusYML().gc().getInt("menus.punishments-list.resolved-button.data");
		}
		return (byte) inst.getMenusYML().gc().getInt("menus.punishments-list.default-button.data");
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		if (!punishment.isResolved()) {
			PunishmentListener.reasons.add(player);
			PunishmentListener.pMap.put(player, punishment);
			player.closeInventory();

			player.sendMessage(CC.translate("&aType the reason for resolving this punishment."));
		}
	}
}
