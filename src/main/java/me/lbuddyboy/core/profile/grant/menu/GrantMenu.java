package me.lbuddyboy.core.profile.grant.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.GrantBuild;
import me.lbuddyboy.core.profile.grant.listener.GrantListener;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.util.BukkitUtils;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:07 AM
 * lCore / me.lbuddyboy.core.profile.grant.menu
 */

@AllArgsConstructor
public class GrantMenu extends Menu {

	private final lProfile target;

	@Override
	public String getTitle(Player player) {
		return CC.translate("&6Granting: " + target.getName());
	}

	@Override
	public Map<Integer, Button> getButtons(Player var1) {
		Map<Integer, Button> buttons = new HashMap<>();

		int i = 0;
		for (Rank rank : Core.getInstance().getRankHandler().getRanks()) {
			buttons.put(i, new RankButton(rank, target));
			++i;
		}

		return buttons;
	}

	@AllArgsConstructor
	public static class RankButton extends Button {

		private final Rank rank;
		private final lProfile target;

		@Override
		public String getName(Player var1) {
			return CC.translate(rank.getDisplayName());
		}

		@Override
		public List<String> getDescription(Player var1) {
			return CC.translate(Arrays.asList(
					"",
					"&fClick to select the " + rank.getDisplayName() + "&f Rank",
					""
			));
		}

		@Override
		public byte getDamageValue(Player player) {
			return (byte) BukkitUtils.toDyeColor(rank.getColor());
		}

		@Override
		public Material getMaterial(Player var1) {
			return Material.WOOL;
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType) {
			player.closeInventory();

			GrantBuild grantBuild = new GrantBuild(player.getUniqueId(), target.getUniqueId(), rank, null, null);
			GrantListener.grantBuildMap.put(player, grantBuild);
			GrantListener.time.add(player);
			player.sendMessage(CC.translate("&aType the time you would like to grant this player the " + rank.getDisplayName() + " &arank for."));
			player.sendMessage(CC.translate("&aType 'cancel' to cancel the process"));
		}
	}

}
