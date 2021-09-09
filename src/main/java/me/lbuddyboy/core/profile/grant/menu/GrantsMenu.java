package me.lbuddyboy.core.profile.grant.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.uuid.UniqueIDCache;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 8:31 PM
 * lCore / me.lbuddyboy.core.profile.grant.menu
 */

@AllArgsConstructor
public class GrantsMenu extends PaginatedMenu {

	private final Profile profile;

	@Override
	public String getPrePaginatedTitle(Player var1) {
		return CC.translate(Settings.MENU_GRANTS_TITLE.getMessage().replaceAll("%player%", profile.getName()));
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player var1) {

		Map<Integer, Button> buttons = new HashMap<>();

		int i = 0;
		for (Grant grant : profile.getGrants()) {
			buttons.put(i, new GrantButton(grant));
			++i;
		}

		return buttons;
	}

	@AllArgsConstructor
	public static class GrantButton extends Button {

		private final Grant grant;

		@Override
		public String getName(Player var1) {
			return CC.translate(Settings.MENU_GRANTS_NAME.getMessage().replaceAll("%rank%", grant.getRank().getDisplayName()));
		}

		@Override
		public List<String> getDescription(Player var1) {
			List<String> lore = Settings.MENU_GRANTS_LORE.getList();
			List<String> newLore = new ArrayList<>();

			for (String s : lore) {
				newLore.add(s
						.replaceAll("%addedBy%", UniqueIDCache.name(grant.getSender()))
						.replaceAll("%addedAt%", grant.getAddedAtDate())
						.replaceAll("%reason%", grant.getReason())
						.replaceAll("%time-left%", grant.getTimeRemaining())
						.replaceAll("%rank%", grant.getRank().getDisplayName()));
			}


			return newLore;
		}

		@Override
		public byte getDamageValue(Player player) {
			if (grant.isPermanent())
				return 5;
			if (!grant.isRemoved()) {
				return 5;
			}
			return 14;
		}

		@Override
		public Material getMaterial(Player var1) {
			return Material.WOOL;
		}
	}

}
