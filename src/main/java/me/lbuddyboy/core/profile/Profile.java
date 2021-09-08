package me.lbuddyboy.core.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:52 PM
 * lCore / me.lbuddyboy.core.profile
 */

@Getter
public class Profile {

	@Getter public static Set<Profile> profiles = new HashSet<>();
	private final MongoCollection<Document> collection = Core.getInstance().getDatabaseHandler().getMongoDatabase().getCollection("profiles");

	private final UUID uniqueId;
	private final String name;
	private final String ip;

	private List<String> knownIPs;

	public Profile(UUID uniqueID, String name, String ip) {

		this.uniqueId = uniqueID;
		this.name = name;
		this.ip = ip;

		this.knownIPs = new ArrayList<>();

		load();
	}

	public void load() {

		Document document = collection.find(Filters.eq("uniqueId", this.uniqueId.toString())).first();

		if (document == null) return;

		this.knownIPs = document.getList("knownIPs", String.class);
	}

	public void save() {

		CompletableFuture.runAsync(() -> {

			Document document = new Document();

			document.put("uniqueId", this.uniqueId.toString());
			document.put("name", this.name);
			document.put("ip", this.ip);
			document.put("knownIPs", this.knownIPs);

			this.collection.replaceOne(Filters.eq("uniqueId", this.uniqueId.toString()), document, new ReplaceOptions().upsert(true));

		});

	}

	public static Profile getByUUID(UUID uuid) {
		for (Profile profile : profiles) {
			if (profile.getUniqueId() == uuid) {
				return profile;
			}
		}
		return null;
	}

}
