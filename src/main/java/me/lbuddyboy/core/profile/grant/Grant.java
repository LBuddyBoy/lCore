package me.lbuddyboy.core.profile.grant;

import lombok.Getter;
import lombok.Setter;
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
	private final UUID sender;
	private final UUID target;
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

}
