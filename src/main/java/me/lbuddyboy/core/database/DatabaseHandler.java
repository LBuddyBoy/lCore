package me.lbuddyboy.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
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
		boolean auth = config.getBoolean("mongo.auth.enabled");
		String authDatabase = config.getString("mongo.auth.auth-db");
		String username = config.getString("mongo.auth.username");
		String password = config.getString("mongo.auth.password");

		String database = config.getString("mongo.database");
		String host = config.getString("mongo.host");
		int port = config.getInt("mongo.port");

		if (!auth) {
			mongoClient = new MongoClient(host, port);
		} else {
			mongoClient = new MongoClient(new ServerAddress(host, port), MongoCredential.createCredential(username, authDatabase, password.toCharArray()),
					MongoClientOptions.builder().build());

		}
		mongoDatabase = this.mongoClient.getDatabase(database);
	}

}
