package me.lbuddyboy.core.database.redis;

import com.google.gson.Gson;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.sub.JedisSubscriber;
import me.lbuddyboy.core.database.redis.uuid.UUIDListener;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 2:44 PM
 * BuddyLibs / me.lbuddyboy.core.database.redis
 */

@Getter
public class RedisHandler {

	@Getter private static final Gson GSON = new Gson();
	private final Core instance;
	private JedisPool jedisPool;

	public RedisHandler(Core instance) {
		this.instance = instance;
		int redisID = instance.getConfig().getInt("redis.channel-id");

		try {
			jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 20000, null, redisID);
		} catch (Exception var6) {
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fCould not find any &6Redis&f server to connect with the current credentials."));
			this.jedisPool = null;
			var6.printStackTrace();
		}

		connect(this.jedisPool);

		instance.getServer().getPluginManager().registerEvents(new UUIDListener(), instance);
	}

	public void connect(JedisPool jedisPool) {
		Thread subscribeThread = new Thread(() -> {
			while (Core.getInstance().isEnabled()) {
				try {
					try (Jedis jedis = jedisPool.getResource()) {
						JedisSubscriber pubSub = new JedisSubscriber();
						String channel = "RedisPacket:All";
						jedis.subscribe(pubSub, channel);
						Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully established a connection to the &6Redis&f server."));
					}
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(CC.translate("&fCould not establish a connection to &6Redis&f."));
					e.printStackTrace();
				}
			}
		}, "Subscriber Thread");
		subscribeThread.setDaemon(true);
		subscribeThread.start();
	}

	public void close() {
		this.jedisPool.close();
	}

	public void sendToAll(JedisPacket packet) {
		send(packet, this.jedisPool);
	}

	public void send(JedisPacket packet, JedisPool sendOn) {
		Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
			try (Jedis jedis = sendOn.getResource()) {
				String encodedPacket = packet.getClass().getName() + "||" + this.GSON.toJson(packet);
				jedis.publish("", encodedPacket);
			}
		});
	}

}
