package me.lbuddyboy.core.punishment;

import lombok.Getter;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.TimeUtils;
import me.lbuddyboy.libraries.util.fanciful.FancyMessage;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:33 AM
 * lCore / me.lbuddyboy.core.punishment
 */

@Getter
public class Punishment {

	private final PunishmentType type;
	private final UUID sender;
	private final UUID target;
	private final long duration;
	private final long addedAt;
	private final String reason;
	private final boolean silent;

	private boolean resolved;
	private String resolvedReason;
	private long resolvedAt;
	private UUID resolvedBy;

	public Punishment(PunishmentType type, UUID sender, UUID target, long duration, long addedAt, String reason, boolean silent) {
		this.type = type;
		this.sender = sender;
		this.target = target;
		this.duration = duration;
		this.addedAt = addedAt;
		this.reason = reason;
		this.silent = silent;
	}

	public void alert(String senderDisplay, String targetDisplay) {
		if (this.silent) {

			String msg = CC.translate(targetDisplay + "&a has just been &esilently " + type.getBroadcastText() + " &aby " + senderDisplay);

			FancyMessage message = new FancyMessage();
			message.then().text(msg).tooltip(Arrays.asList(
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Reason&7: &f" + this.reason),
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Duration&7: &f" + TimeUtils.formatIntoDetailedString((int) (this.duration / 1000L)))
			));

			Bukkit.getOnlinePlayers().stream()
					.filter(player -> player.hasPermission("lcore.staff"))
					.forEach(message::send);

			Bukkit.getConsoleSender().sendMessage(msg);

		} else {
			String msg = CC.translate(targetDisplay + "&a has just been " + type.getBroadcastText() + " &aby " + senderDisplay);

			FancyMessage message = new FancyMessage();
			message.then().text(msg).tooltip(Arrays.asList(
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Reason&7: &f" + this.reason),
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Duration&7: &f" + getDurationString())
			));

			Bukkit.broadcastMessage(msg);
		}
	}

	public String getDurationString() {

		if (isPermanent()) {
			return "Permanent";
		}

		return TimeUtils.formatIntoDetailedString((int) (this.duration / 1000L));
	}

	public boolean isPermanent() {
		return !isResolved() && this.duration == Long.MAX_VALUE;
	}

}
