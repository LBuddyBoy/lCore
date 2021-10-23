package me.lbuddyboy.core.database.packets.global;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.report.Report;
import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:11 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.global
 */

@AllArgsConstructor
public class ReportDeletePacket implements JedisPacket {

	private Report report;

	@Override
	public void onReceive() {
		Core.getInstance().getReportHandler().getReports().remove(report);
	}

	@Override
	public String getID() {
		return "Report Delete";
	}
}
