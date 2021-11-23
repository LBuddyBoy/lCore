package me.lbuddyboy.core.punishment;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Setter;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.TimeUtils;
import me.lbuddyboy.libraries.util.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:33 AM
 * lCore / me.lbuddyboy.core.punishment
 */

@Data
public class Punishment {

	private UUID id;
	private final PunishmentType type;
	private UUID sender;
	private UUID target;
	private final long duration;
	private final long addedAt;
	private final String reason;
	@Setter private boolean silent;

	private boolean resolved;
	private String resolvedReason;
	private long resolvedAt;
	private UUID resolvedBy;

	public Punishment(UUID id, PunishmentType type, UUID sender, UUID target, long duration, long addedAt, String reason, boolean silent) {
		this.id = id;
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

			String msg = CC.translate(targetDisplay + "&a has just been &7silently " + type.getBroadcastText() + " &aby " + senderDisplay);
			if (this.isResolved()) {
				msg = CC.translate(targetDisplay + "&a has just been &7silently " + type.getBroadcastPardonText() + " &aby " + senderDisplay);
			}

			FancyMessage message = new FancyMessage();
			message.text(msg).tooltip(Arrays.asList(
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Reason&7: &f" + this.reason),
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Duration&7: &f" + TimeUtils.formatIntoDetailedString((int) (this.duration / 1000L)))
			));

			Bukkit.getOnlinePlayers().stream()
					.filter(player -> player.hasPermission("lcore.staff"))
					.forEach(message::send);

			Bukkit.getConsoleSender().sendMessage(msg);

		} else {
			String msg = CC.translate(targetDisplay + "&a has just been " + type.getBroadcastText() + " &aby " + senderDisplay);
			if (this.isResolved()) {
				msg = CC.translate(targetDisplay + "&a has just been " + type.getBroadcastPardonText() + " &aby " + senderDisplay);
			}

			FancyMessage message = new FancyMessage();
			message.text(msg).tooltip(Arrays.asList(
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Reason&7: &f" + this.reason),
					CC.GRAY + CC.UNICODE_ARROWS_RIGHT + CC.translate(" &6Duration&7: &f" + getDurationString())
			));

			Bukkit.broadcastMessage(msg);
		}
	}

	public long getTimeLeft() {
		return (this.duration + this.addedAt) - System.currentTimeMillis();
	}

	public String getFormattedTimeLeft() {
		if (getTimeLeft() < 0 && !isPermanent()) {
			return "Expired";
		}
		if (isPermanent() && getTimeLeft() < 0) {
			return "Removed";
		}
		if (isPermanent()) {
			return "Never";
		}
		return TimeUtils.formatIntoDetailedString((int) (this.getTimeLeft() / 1000L));
	}

	public String getAddedAtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(Configuration.SERVER_TIMEZONE.getMessage()));
		return sdf.format(new Date(this.addedAt));
	}

	public String getResolvedAtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(Configuration.SERVER_TIMEZONE.getMessage()));
		return sdf.format(new Date(this.resolvedAt));
	}

	public String getDurationString() {

		if (this.duration == Long.MAX_VALUE) {
			return "Permanent";
		}

		return TimeUtils.formatIntoDetailedString((int) (this.duration / 1000L));
	}

	public boolean isPermanent() {
		return !isResolved() && this.duration == Long.MAX_VALUE;
	}

	public static Punishment deserialize(JsonObject object) {
		Punishment punishment = new Punishment(
				UUID.fromString(object.get("id").getAsString()),
				PunishmentType.valueOf(object.get("type").getAsString()),
				null,
				null,
				object.get("duration").getAsLong(),
				object.get("addedAt").getAsLong(),
				object.get("reason").getAsString(),
				object.get("silent").getAsBoolean()
		);

		if (!object.get("target").isJsonNull()) {
			punishment.setTarget(UUID.fromString(object.get("target").getAsString()));
		}
		if (!object.get("sender").isJsonNull()) {
			punishment.setSender(UUID.fromString(object.get("sender").getAsString()));
		}

		if (!object.get("removedBy").isJsonNull()) {
			punishment.setResolvedBy(UUID.fromString(object.get("removedBy").getAsString()));
		}

		if (!object.get("removedAt").isJsonNull()) {
			punishment.setResolvedAt(object.get("removedAt").getAsLong());
		}

		if (!object.get("removedReason").isJsonNull()) {
			punishment.setResolvedReason(object.get("removedReason").getAsString());
		}

		if (!object.get("removed").isJsonNull()) {
			punishment.setResolved(object.get("removed").getAsBoolean());
		}

		return punishment;
	}

	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("id", getId().toString());
		object.addProperty("type", getType().name());
		object.addProperty("sender", getSender() == null ? null : getSender().toString());
		object.addProperty("target", getTarget() == null ? null : getTarget().toString());
		object.addProperty("addedAt", getAddedAt());
		object.addProperty("reason", getReason());
		object.addProperty("duration", getDuration());
		object.addProperty("silent", isSilent());
		object.addProperty("removedBy", getResolvedBy() == null ? null : getResolvedBy().toString());
		object.addProperty("removedAt", getResolvedAt());
		object.addProperty("removedReason", getResolvedReason());
		object.addProperty("removed", isResolved());
		return object;
	}

	public void sendPunishInfo(Player player) {
		if (getType() == PunishmentType.MUTE) {
			player.sendMessage(CC.translate(Configuration.MUTE_MESSAGE.getMessage()
					.replaceAll("%reason%", getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		} else if (getType() == PunishmentType.KICK) {
			player.sendMessage(CC.translate(Configuration.KICK_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		} else if (getType() == PunishmentType.WARN) {
			player.sendMessage(CC.translate(Configuration.WARN_MESSAGE.getMessage()
					.replaceAll("%reason%", getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		} else if (getType() == PunishmentType.BAN) {
			player.sendMessage(CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		} else if (getType() == PunishmentType.BLACKLIST) {
			player.sendMessage(CC.translate(Configuration.BLACKLIST_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", getReason())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage())));
		}
	}

}
