package me.lbuddyboy.core.report;

import lombok.Data;
import me.lbuddyboy.core.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:13 PM
 * LBuddyBoy Development / me.lbuddyboy.core.report
 */

@Data
public class Report {

	private final int id;
	private final UUID sender;
	private final String reason;
	private final String server;
	private final long sentAt;

	private boolean report = false;
	private UUID target;

	private boolean resolved;
	private long resolvedAt;
	private UUID resolvedBy;

	public Report(int id, UUID sender, String server, String reason, long sentAt) {
		this.id = id;
		this.sender = sender;
		this.server = server;
		this.reason = reason;
		this.sentAt = sentAt;
	}

	public String getSentAtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(Configuration.SERVER_TIMEZONE.getMessage()));
		return sdf.format(new Date(this.sentAt));
	}

	public String getResolvedAtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(Configuration.SERVER_TIMEZONE.getMessage()));
		return sdf.format(new Date(this.resolvedAt));
	}
}
