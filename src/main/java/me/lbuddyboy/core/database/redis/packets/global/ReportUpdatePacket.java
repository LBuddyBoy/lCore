package me.lbuddyboy.core.database.redis.packets.global;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.report.Report;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:11 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.global
 */

@AllArgsConstructor
public class ReportUpdatePacket implements JedisPacket {

	private Report report;

	@Override
	public void onReceive() {
		for (Report search : Core.getInstance().getReportHandler().getReports()) {
			if (search.getId() == report.getId()) {
				search.setResolved(report.isResolved());
				if (search.isResolved()) {
					search.setResolvedBy(report.getResolvedBy());
					search.setResolvedAt(report.getResolvedAt());
				}
			}
		}
	}

	@Override
	public String getID() {
		return "Report Update";
	}
}
