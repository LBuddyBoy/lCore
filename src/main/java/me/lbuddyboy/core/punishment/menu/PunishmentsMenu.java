package me.lbuddyboy.core.punishment.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.core.punishment.menu.buttons.PunishmentButton;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.buttons.BackButton;
import me.lbuddyboy.libraries.util.qlib.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 24/10/2021 / 11:44 AM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.menu
 */

@AllArgsConstructor
public class PunishmentsMenu extends PaginatedMenu {

	private lProfile target;
	private PunishmentType type;

	@Override
	public String getPrePaginatedTitle(Player var1) {
		return CC.translate(Core.getInstance().getMenusYML().gc().getString("menus.punishments-list.title")
				.replaceAll("%type%", type.getName())
				.replaceAll("%target%", target.getName())
		);
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player var1) {
		Map<Integer, Button> buttons = new HashMap<>();

		int i = 0;
		for (Punishment punishment : target.getPunishmentsByType(type)) {
			buttons.put(i, new PunishmentButton(punishment));
			++i;
		}

		return buttons;
	}

	@Override
	public Map<Integer, Button> getGlobalButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		buttons.put(4, new BackButton(new PunishmentsMainMenu(this.target)));

		return buttons;

	}

	@Override
	public boolean isAutoUpdate() {
		return true;
	}
}
