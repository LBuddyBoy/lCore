package me.lbuddyboy.core.report.menu;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.global.ReportDeletePacket;
import me.lbuddyboy.core.report.Report;
import me.lbuddyboy.libraries.redis.RedisUUIDCache;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.ItemBuilder;
import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 2:02 PM
 * LBuddyBoy Development / me.lbuddyboy.core.report.menu
 */
public class ResolvedReportsMenu extends PaginatedMenu {
	@Override
	public String getPrePaginatedTitle(Player var1) {
		return CC.translate(Configuration.REPORTS_MENU_TITLE.getMessage());
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player var1) {
		Map<Integer, Button> buttons = new HashMap<>();

		int i = 0;
		for (Report report : Core.getInstance().getReportHandler().getReports()) {
			if (!report.isResolved()) continue;
			buttons.put(i, new ReportButton(report));
			++i;
		}

		return buttons;
	}

	@Override
	public Map<Integer, Button> getGlobalButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		buttons.put(3, new Button() {
			@Override
			public String getName(Player var1) {
				return CC.translate("&6Reports");
			}

			@Override
			public List<String> getDescription(Player var1) {
				return Collections.singletonList(CC.translate("&7Click to view all of the reports."));
			}

			@Override
			public Material getMaterial(Player var1) {
				return Material.BOOKSHELF;
			}
			@Override
			public void clicked(Player player, int slot, ClickType clickType) {
				new ReportsMenu().openMenu(player);
			}
		});
		buttons.put(5, new Button() {
			@Override
			public String getName(Player var1) {
				return CC.translate(Configuration.REPORTS_MENU_AMOUNT_REPORTS_BUTTON.getMessage().replaceAll("%reports%", "" + Core.getInstance().getReportHandler().getReports().size()));
			}

			@Override
			public List<String> getDescription(Player var1) {
				return null;
			}

			@Override
			public Material getMaterial(Player var1) {
				return Material.BOOK;
			}
		});

		return buttons;
	}

	@AllArgsConstructor
	public static class ReportButton extends Button {

		private Report report;

		@Override
		public String getName(Player var1) {
			return null;
		}

		@Override
		public List<String> getDescription(Player var1) {
			return null;
		}

		@Override
		public Material getMaterial(Player var1) {
			return null;
		}

		@Override
		public ItemStack getButtonItem(Player player) {
			Material material = Material.valueOf(Configuration.REPORTS_MENU_MAT.getMessage());
			int data = Configuration.REPORTS_MENU_MAT_DATA.getNumber();
			String name = CC.translate(Configuration.REPORTS_MENU_MAT_NAME.getMessage());
			ItemBuilder builder = new ItemBuilder(material).setData(data).setDisplayName(name.replaceAll("%id%", String.valueOf(report.getId())));
			if (report.isReport() && report.isResolved()) {
				for (String s : Configuration.REPORTS_MENU_LORE_REPORT_RESOLVED.getList()) {
					builder.addLore(s
							.replaceAll("%server%", report.getServer())
							.replaceAll("%reason%", report.getReason())
							.replaceAll("%sentAt%", report.getSentAtDate())
							.replaceAll("%resolvedAt%", report.getResolvedAtDate())
							.replaceAll("%resolvedBy%", RedisUUIDCache.name(report.getResolvedBy()))
							.replaceAll("%target%", RedisUUIDCache.name(report.getTarget()))
							.replaceAll("%sender%", RedisUUIDCache.name(report.getSender())));
				}
				return builder.create();
			}
			if (!report.isReport() && report.isResolved()) {
				for (String s : Configuration.REPORTS_MENU_LORE_HELPOP_RESOLVED.getList()) {
					builder.addLore(s
							.replaceAll("%server%", report.getServer())
							.replaceAll("%reason%", report.getReason())
							.replaceAll("%sentAt%", report.getSentAtDate())
							.replaceAll("%resolvedAt%", report.getResolvedAtDate())
							.replaceAll("%resolvedBy%", RedisUUIDCache.name(report.getResolvedBy()))
							.replaceAll("%sender%", RedisUUIDCache.name(report.getSender())));
				}
				return builder.create();
			}
			return builder.create();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType) {
			Core.getInstance().getReportHandler().deleteReport(report);
			Core.getInstance().getReportHandler().deleteReportFromDB(report);
			player.sendMessage(CC.translate(Configuration.REPORT_STAFF_DELETED.getMessage()));
			new ReportDeletePacket(report).send();
		}
	}

}
