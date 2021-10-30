package me.lbuddyboy.core.profile.grant.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.profile.grant.listener.GrantListener;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.redis.RedisUUIDCache;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.TimeUtils;
import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

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

	private final lProfile profile;

	@Override
	public String getPrePaginatedTitle(Player var1) {
		return CC.translate(Configuration.MENU_GRANTS_TITLE.getMessage().replaceAll("%player%", profile.getName()));
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

	@Override
	public boolean isAutoUpdate() {
		return true;
	}

	@AllArgsConstructor
	public static class GrantButton extends Button {

		private final Grant grant;

		@Override
		public String getName(Player var1) {
			return CC.translate(Configuration.MENU_GRANTS_NAME.getMessage()
					.replaceAll("%status%", (grant.getId() == Core.getInstance().getProfileHandler().getByUUID(grant.getTarget()).getCurrentGrant().getId() ? Configuration.GRANT_ACTIVE.getMessage() : ""))
					.replaceAll("%rank%", grant.getRank().getDisplayName()));
		}

		@Override
		public List<String> getDescription(Player var1) {
			List<String> newLore = new ArrayList<>();

			if (grant.isRemoved()) {
				List<String> lore = Configuration.MENU_GRANTS_LORE_REMOVED.getList();
				for (String s : lore) {
					newLore.add(s
							.replaceAll("%addedBy%", RedisUUIDCache.name(grant.getSender()))
							.replaceAll("%addedAt%", grant.getAddedAtDate())
							.replaceAll("%reason%", grant.getReason())
							.replaceAll("%removedBy%", RedisUUIDCache.name(grant.getRemovedBy()))
							.replaceAll("%removedAt%", grant.getRemovedAtDate())
							.replaceAll("%removedFor%", grant.getRemovedReason())
							.replaceAll("%reason%", grant.getReason())
							.replaceAll("%duration%", TimeUtils.formatIntoDetailedString((int) (grant.getDuration() / 1000)))
							.replaceAll("%time-left%", grant.getTimeRemaining())
							.replaceAll("%rank%", grant.getRank().getDisplayName()));
				}
				return CC.translate(newLore);
			}

			if (grant.isPermanent()) {
				List<String> lore = Configuration.MENU_GRANTS_LORE.getList();
				for (String s : lore) {
					newLore.add(s
							.replaceAll("%addedBy%", RedisUUIDCache.name(grant.getSender()))
							.replaceAll("%addedAt%", grant.getAddedAtDate())
							.replaceAll("%reason%", grant.getReason())
							.replaceAll("%duration%", "Permanent")
							.replaceAll("%time-left%", grant.getTimeRemaining())
							.replaceAll("%rank%", grant.getRank().getDisplayName()));
				}
				return CC.translate(newLore);
			}

			List<String> lore = Configuration.MENU_GRANTS_LORE.getList();
			for (String s : lore) {
				newLore.add(s
						.replaceAll("%addedBy%", RedisUUIDCache.name(grant.getSender()))
						.replaceAll("%addedAt%", grant.getAddedAtDate())
						.replaceAll("%reason%", grant.getReason())
						.replaceAll("%duration%", TimeUtils.formatIntoDetailedString((int) (grant.getDuration() / 1000)))
						.replaceAll("%time-left%", grant.getTimeRemaining())
						.replaceAll("%rank%", grant.getRank().getDisplayName()));
			}

			return CC.translate(newLore);
		}

		@Override
		public byte getDamageValue(Player player) {
			if (grant.isPermanent())
				return 5;
			if (grant.isExpired()) {
				return 4;
			}
			if (!grant.isRemoved()) {
				return 5;
			}
			return 14;
		}

		@Override
		public Material getMaterial(Player var1) {
			return Material.WOOL;
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType) {
			if (grant.isRemoved())
				return;
			player.closeInventory();
			player.sendMessage(CC.translate("&aType a reason for removing this players grant."));
			GrantListener.remove.add(player);
			GrantListener.grantMap.put(player, grant);
		}
	}

}
