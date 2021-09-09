package me.lbuddyboy.core.rank.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankDeletePacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.listener.RankEditListener;
import me.lbuddyboy.core.util.Callback;
import me.lbuddyboy.libraries.menu.object.Button;
import me.lbuddyboy.libraries.menu.object.Menu;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:26 AM
 * lCore / me.lbuddyboy.core.rank.menu
 */

@AllArgsConstructor
public class RankEditMenu extends Menu {

	private final Rank rank;

	@Override
	public String getTitle(Player player) {
		return CC.translate(Settings.RANK_EDIT_MENU_TITLE.getMessage().replaceAll("%rank%", rank.getDisplayName()));
	}

	@Override
	public List<Button> buttons(Player player) {
		List<Button> buttons = new ArrayList<>();

		int i = 0;
		for (RankEditTypes type : RankEditTypes.values()) {
			buttons.add(new RankEditButton(rank, i, type));
			++i;
		}

		return buttons;
	}

	@Override
	public int size(Player player) {
		return 9;
	}

	@AllArgsConstructor
	public static class RankEditButton extends Button {

		private final Rank rank;
		private final int slot;
		private final RankEditTypes type;

		@Override
		public ItemStack stack(Player player) {
			return new ItemBuilder(type.displayMaterial).setDisplayName("&6" + type.display).addLore("&fClick to " + type.display.toLowerCase() + " of " + rank.getDisplayName()).create();
		}

		@Override
		public int slot() {
			return slot;
		}

		@Override
		public void action(Player player, int slot, InventoryClickEvent event) {
			if (type == RankEditTypes.DELETE) {
				rank.delete();

				new RankDeletePacket(rank).send();

				player.sendMessage(CC.translate(Settings.DELETED_RANK.getMessage()
						.replaceAll("%new%", rank.getDisplayName())
						.replaceAll("%rank%", rank.getName())));
				return;
			}
			type.callback.callback(player);
		}
	}

	@AllArgsConstructor
	public enum RankEditTypes {

		DELETE(Material.REDSTONE, "&cDelete", player -> {

		}),
		RENAME(Material.NAME_TAG, "Rename", p -> {
			RankEditListener.rename.add(p);
		}),
		SET_COLOR(Material.INK_SACK, "Set Color", p -> {
			RankEditListener.color.add(p);
		}),
		SET_PREFIX(Material.PAINTING, "Set Prefix", p -> {
			RankEditListener.prefix.add(p);
		}),
		SET_DISPLAY(Material.SIGN, "Set DisplayName", p -> {
			RankEditListener.display.add(p);
		}),
		SET_WEIGHT(Material.ANVIL, "Set Weight", p -> {
			RankEditListener.weight.add(p);
		});

		private final Material displayMaterial;
		private final String display;
		private final Callback<Player> callback;

	}

}
