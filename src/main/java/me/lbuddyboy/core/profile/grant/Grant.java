package me.lbuddyboy.core.profile.grant;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 6:59 PM
 * lCore / me.lbuddyboy.core.profile.grant
 */

@Getter
public class Grant {

	private final UUID id;
	private final Rank rank;
	@Setter private UUID sender;
	@Setter private UUID target;
	private final String reason;
	private final long addedAt;
	private final long duration;

	@Setter private boolean removed;
	@Setter private long removedAt;
	@Setter private UUID removedBy;
	@Setter private String removedReason;

	public Grant(UUID id, Rank rank, UUID sender, UUID target, String reason, long addedAt, long duration) {
		this.id = id;
		this.rank = rank;
		this.sender = sender;
		this.target = target;
		this.reason = reason;
		this.addedAt = addedAt;
		this.duration = duration;
	}

	public long getRemainingTime() {
		return (addedAt + duration) - System.currentTimeMillis();
	}
	public String getRemovedAtDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
		return simpleDateFormat.format(new Date(removedAt));
	}
	public String getAddedAtDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
		return simpleDateFormat.format(new Date(addedAt));
	}

	public String getTimeRemaining() {
		if (isRemoved())
			return getRemovedReason();

		if (isPermanent())
			return "Never";

		return TimeUtils.formatIntoDetailedString((int) (getRemainingTime() / 1000L));
	}

	public boolean isExpired() {
		return !isPermanent() && getRemainingTime() <= 0;
	}

	public boolean isPermanent() {
		return duration == Long.MAX_VALUE;
	}

	public static Grant deserialize(JsonObject object) {
		Rank rank = Core.getInstance().getRankHandler().getByName(object.get("rank").getAsString());

		Grant grant = new Grant(
				UUID.fromString(object.get("id").getAsString()),
				rank,
				null,
				null,
				object.get("addedReason").getAsString(),
				object.get("addedAt").getAsLong(),
				object.get("duration").getAsLong()
		);

		if (!object.get("target").isJsonNull()) {
			grant.setTarget(UUID.fromString(object.get("target").getAsString()));
		}
		if (!object.get("addedBy").isJsonNull()) {
			grant.setSender(UUID.fromString(object.get("addedBy").getAsString()));
		}

		if (!object.get("removedBy").isJsonNull()) {
			grant.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
		}

		if (!object.get("removedAt").isJsonNull()) {
			grant.setRemovedAt(object.get("removedAt").getAsLong());
		}

		if (!object.get("removedReason").isJsonNull()) {
			grant.setRemovedReason(object.get("removedReason").getAsString());
		}

		if (!object.get("removed").isJsonNull()) {
			grant.setRemoved(object.get("removed").getAsBoolean());
		}

		return grant;
	}

	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("id", getId().toString());
		object.addProperty("rank", getRank().getName());
		object.addProperty("addedBy", getSender() == null ? null : getSender().toString());
		object.addProperty("target", getTarget() == null ? null : getTarget().toString());
		object.addProperty("addedAt", getAddedAt());
		object.addProperty("addedReason", getReason());
		object.addProperty("duration", getDuration());
		object.addProperty("removedBy", getRemovedBy() == null ? null : getRemovedBy().toString());
		object.addProperty("removedAt", getRemovedAt());
		object.addProperty("removedReason", getRemovedReason());
		object.addProperty("removed", isRemoved());
		return object;
	}
}
