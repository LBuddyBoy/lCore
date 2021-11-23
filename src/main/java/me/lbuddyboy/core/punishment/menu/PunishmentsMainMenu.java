package me.lbuddyboy.core.punishment.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.core.punishment.menu.buttons.PunishListButton;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 24/10/2021 / 11:10 AM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.menu
 */

@AllArgsConstructor
public class PunishmentsMainMenu extends Menu {

	private lProfile target;

	@Override
	public String getTitle(Player player) {
		return CC.translate(Core.getInstance().getMenusYML().gc().getString("menus.punishments-main.title"));
	}

	@Override
	public Map<Integer, Button> getButtons(Player var1) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (PunishmentType punish : PunishmentType.values()) {
			buttons.put(Core.getInstance().getMenusYML().gc().getInt("menus.punishments-main." + punish.name().toLowerCase() + ".slot") - 1, new PunishListButton(target, punish));
		}

		return buttons;
	}

	@Override
	public int size(Map<Integer, Button> buttons) {
		return Core.getInstance().getMenusYML().gc().getInt("menus.punishments-main.size");
	}
}
