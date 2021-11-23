package me.lbuddyboy.core.punishment.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 1:40 PM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.command
 */
public class AltsCommand {

	@Command(labels = {"alts", "dupeip"})
	@Permissible("lcore.command.alts")
	public void alts(CommandSender sender, @Param("target")UUID uuid) {
		lProfile targetProfile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		if (targetProfile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}
		Configuration.ALTS_HEADER.getList().forEach(s -> sender.sendMessage(CC.translate(s)));
		sender.sendMessage(CC.translate(Configuration.ALTS_INFO.getMessage()));
		sender.sendMessage(CC.translate(StringUtils.join(targetProfile.coloredAlts(), "&f, ")));

	}

}
