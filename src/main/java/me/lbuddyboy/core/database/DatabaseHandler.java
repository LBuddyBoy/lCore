package me.lbuddyboy.core.database;

import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:39 PM
 * lCore / me.lbuddyboy.core.database
 */

@Getter
public class DatabaseHandler {

	private final MongoClient mongoClient;
	private final MongoDatabase mongoDatabase;

	private final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();
	private final Type LIST_UUID_TYPE = new TypeToken<List<UUID>>() {}.getType();
	private final Type LIST_GRANT_TYPE = new TypeToken<Grant>() {}.getType();
	private final Type LIST_PUNISHMENT_TYPE = new TypeToken<Punishment>() {}.getType();

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
		Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully connected the &6Mongo Database"));
	}

}
