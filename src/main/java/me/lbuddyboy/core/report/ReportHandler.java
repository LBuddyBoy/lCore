package me.lbuddyboy.core.report;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.report.command.HelpopCommand;
import me.lbuddyboy.core.report.command.ReportCommand;
import me.lbuddyboy.core.report.command.ReportsCommand;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:12 PM
 * LBuddyBoy Development / me.lbuddyboy.core.global
 */
public class ReportHandler {

	private MongoCollection<Document> collection = Core.getInstance().getDatabaseHandler().getMongoDatabase().getCollection("reports");
	@Getter private List<Report> reports;

	public ReportHandler() {
		this.reports = new ArrayList<>();

		for (Document document : collection.find()) {
			loadReport(document);
		}

		Core.getInstance().getZetsu().registerCommands(new ReportCommand());
		Core.getInstance().getZetsu().registerCommands(new ReportsCommand());
		Core.getInstance().getZetsu().registerCommands(new HelpopCommand());
	}

	public List<Report> getReportsBySender(UUID sender) {
		List<Report> reports = new ArrayList<>();
		for (Report report : this.reports) {
			if (report.getSender() == sender) {
				reports.add(report);
			}
		}
		return reports;
	}

	public Report getReportByID(int id) {
		for (Report report : this.reports) {
			if (report.getId() == id) {
				return report;
			}
		}
		return null;
	}

	public void loadReport(Document document) {

		Report report = new Report(document.getInteger("id"), UUID.fromString(document.getString("sender")), document.getString("server"), document.getString("reason"), document.getLong("sentAt"));

		report.setReport(document.getBoolean("report"));
		if (report.isReport()) {
			report.setTarget(UUID.fromString(document.getString("target")));
		}
		report.setResolved(document.getBoolean("resolved"));
		if (report.isResolved()) {
			report.setResolvedBy(UUID.fromString(document.getString("resolvedBy")));
			report.setResolvedAt(document.getLong("resolvedAt"));
		}
		this.reports.add(report);
	}

	public void deleteReport(Report report) {
		this.reports.remove(report);
	}

	public void deleteReportFromDB(Report report) {
		this.collection.deleteOne(Filters.eq("id", report.getId()));
	}

	public void saveReport(Report report) {

		Document document = new Document();

		document.put("id", report.getId());
		document.put("sender", report.getSender().toString());
		document.put("server", report.getServer());
		document.put("reason", report.getReason());
		document.put("sentAt", report.getSentAt());
		document.put("report", report.isReport());
		if (report.isReport()) {
			document.put("target", report.getTarget().toString());
		}
		document.put("resolved", report.isResolved());
		if (report.isResolved()) {
			document.put("resolvedBy", report.getResolvedBy().toString());
			document.put("resolvedAt", report.getResolvedAt());
		}

		this.collection.replaceOne(Filters.eq("id", report.getId()), document, new ReplaceOptions().upsert(true));
	}

}
