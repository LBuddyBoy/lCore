package me.lbuddyboy.core.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:39 PM
 * lCore / me.lbuddyboy.core.database
 */

@Getter
public class DatabaseHandler {

	private final MongoClient mongoClient;
	private final MongoDatabase mongoDatabase;

	public DatabaseHandler() {
		FileConfiguration config = Core.getInstance().getConfig();
		mongoClient = new MongoClient(config.getString("mongo.host"), config.getInt("mongo.port"));
		mongoDatabase = this.mongoClient.getDatabase(config.getString("mongo.database"));
	}

}
