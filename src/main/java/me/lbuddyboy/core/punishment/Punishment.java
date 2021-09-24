package me.lbuddyboy.core.punishment;

import com.google.gson.JsonObject;
import lombok.Data;
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

@Data
public class Punishment {

	private final PunishmentType type;
	private UUID sender;
	private UUID target;
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

	public static Punishment deserialize(JsonObject object) {
		Punishment punishment = new Punishment(
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
		object.addProperty("type", getType().name());
		object.addProperty("sender", getSender() == null ? null : getSender().toString());
		object.addProperty("target", getTarget() == null ? null : getTarget().toString());
		object.addProperty("addedAt", getAddedAt());
		object.addProperty("addedReason", getReason());
		object.addProperty("duration", getDuration());
		object.addProperty("removedBy", getResolvedBy() == null ? null : getResolvedBy().toString());
		object.addProperty("removedAt", getResolvedAt());
		object.addProperty("removedReason", getResolvedReason());
		object.addProperty("removed", isResolved());
		return object;
	}
}
