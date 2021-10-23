package me.lbuddyboy.core.report.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.global.FancyMessageStaffPacket;
import me.lbuddyboy.core.database.packets.global.MessageStaffPacket;
import me.lbuddyboy.core.report.Report;
import me.lbuddyboy.libraries.redis.RedisUUIDCache;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.fanciful.FancyMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:30 PM
 * LBuddyBoy Development / me.lbuddyboy.core.report.command
 */
public class ReportCommand {

	@Command(labels = "report", async = true)
	@Permissible("lcore.command.report")
	public void reports(Player sender, @Param("target") UUID target, @Param("reason") String reason) {

		if (target == null) {
			sender.sendMessage(Configuration.INVALID_PROFILE.getMessage());
			return;
		}

		sender.sendMessage(CC.translate(Configuration.REPORT_SENDER.getMessage()));

		int id = Core.getInstance().getReportHandler().getReports().size() + 1;
		Report report = new Report(id, sender.getUniqueId(), Configuration.SERVER_NAME.getMessage(), reason, System.currentTimeMillis());
		report.setReport(true);
		report.setTarget(target);

		Core.getInstance().getReportHandler().saveReport(report);

		Core.getInstance().getReportHandler().getReports().add(report);

		List<String> strings = new ArrayList<>();
		for (String s : Configuration.REPORT_STAFF_MESSAGE.getList()) {
			strings.add(s.replaceAll("%sender%", sender.getName())
					.replaceAll("%server%", report.getServer())
					.replaceAll("%target%", RedisUUIDCache.name(target))
					.replaceAll("%reason%", reason));
		}

		new MessageStaffPacket(strings).send();

		FancyMessage tpServer = new FancyMessage();
		FancyMessage tpSender = new FancyMessage();
		FancyMessage tpTarget = new FancyMessage();

		tpServer.text(CC.translate(Configuration.REPORT_TP_SERVER.getMessage())).tooltip(CC.translate(Configuration.REPORT_TP_SERVER.getMessage())).command("/server " + report.getServer());
		tpSender.text(CC.translate(Configuration.REPORT_TP_SENDER.getMessage())).tooltip(CC.translate(Configuration.REPORT_TP_SENDER.getMessage())).command("/tp " + sender.getName());
		tpTarget.text(CC.translate(Configuration.REPORT_TP_TARGET.getMessage())).tooltip(CC.translate(Configuration.REPORT_TP_TARGET.getMessage())).command("/tp " + RedisUUIDCache.name(target));

		new FancyMessageStaffPacket(tpServer).send();
		new FancyMessageStaffPacket(tpSender).send();
		new FancyMessageStaffPacket(tpTarget).send();

	}

}
