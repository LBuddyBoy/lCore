package me.lbuddyboy.core.commands;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.api.lCoreAPI;
import me.lbuddyboy.core.database.redis.packets.global.FancyMessageStaffPacket;
import me.lbuddyboy.core.database.redis.packets.global.MessageStaffPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:30 PM
 * LBuddyBoy Development / me.lbuddyboy.core.report.command
 */
public class HelpopCommand {

	@Command(labels = {"helpop", "request"}, async = true)
	@Permissible("lcore.command.request")
	public void reports(Player sender, @Param("reason") String reason) {

		lProfile senderProfile = lCoreAPI.getProfileByUUID(sender.getUniqueId());
		if (!senderProfile.canSendReport()) {
			sender.sendMessage(CC.translate(Core.getInstance().getConfig().getString("report-on-cooldown").replaceAll("%time%", senderProfile.getRemainingReportTime())));
			return;
		}

		senderProfile.setLastReport(System.currentTimeMillis());
		senderProfile.save();

		sender.sendMessage(CC.translate(Configuration.REPORT_SENDER.getMessage()));

		List<String> strings = new ArrayList<>();
		for (String s : Configuration.HELPOP_STAFF_MESSAGE.getList()) {
			strings.add(s.replaceAll("%sender%", sender.getName())
					.replaceAll("%server%", Configuration.SERVER_NAME.getMessage())
					.replaceAll("%reason%", reason));
		}

		new MessageStaffPacket(strings).send();

		FancyMessage tpServer = new FancyMessage();
		FancyMessage tpSender = new FancyMessage();

		tpServer.text(CC.translate(Configuration.REPORT_TP_SERVER.getMessage())).tooltip(CC.translate(Configuration.REPORT_TP_SERVER.getMessage())).command("/server " + Configuration.SERVER_NAME.getMessage());
		tpSender.text(CC.translate(Configuration.REPORT_TP_SENDER.getMessage())).tooltip(CC.translate(Configuration.REPORT_TP_SENDER.getMessage())).command("/tp " + sender.getName());

		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("lcore.staff") || staff.isOp()) {
				for (String string : strings) {
					staff.sendMessage(CC.translate(string));
				}
				tpServer.send(staff);
			}
		}

		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("lcore.staff") || staff.isOp()) {
				tpSender.send(staff);
			}
		}

		new FancyMessageStaffPacket(tpServer).send();
		new FancyMessageStaffPacket(tpSender).send();

	}

}
