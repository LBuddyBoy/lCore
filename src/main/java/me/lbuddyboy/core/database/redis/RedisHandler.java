package me.lbuddyboy.core.database.redis;

import com.google.gson.Gson;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.sub.JedisSubscriber;
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

	@Getter
	private static final Gson GSON = new Gson();
	private final Core instance;
	private JedisPool jedisPool;

	public RedisHandler(Core instance) {
		this.instance = instance;

		try {
			if (!instance.getConfig().getBoolean("redis.auth.enabled")) {
				this.jedisPool = (new JedisPool(new JedisPoolConfig(), instance.getConfig().getString("redis.host"), instance.getConfig().getInt("redis.port"), 20000, null, instance.getConfig().getInt("redis.channel-id")));
			} else {
				this.jedisPool = (new JedisPool(new JedisPoolConfig(), instance.getConfig().getString("redis.host"), instance.getConfig().getInt("redis.port"), 20000, instance.getConfig().getString("redis.auth.password"), instance.getConfig().getInt("redis.channel-id")));
			}
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully connected to the &6Redis Server"));
		} catch (Exception var6) {
			var6.printStackTrace();
			this.instance.getLogger().warning("Couldn't connect to a Redis instance at localhost" + ".");
		}

		connect(this.jedisPool);
	}

	private static final String GLOBAL_MESSAGE_CHANNEL = "JedisPacket:All";
	static final String PACKET_MESSAGE_DIVIDER = "||";

	public static void connect(JedisPool connectTo) {

		Thread subscribeThread = new Thread(() -> {
			while (Core.getInstance().isEnabled()) {
				try {
					Jedis jedis = connectTo.getResource();
					try {
						JedisSubscriber pubSub = new JedisSubscriber();
						jedis.subscribe(pubSub, GLOBAL_MESSAGE_CHANNEL);
					} catch (Exception exception) {
						if (jedis != null) {
							jedis.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "lCore - Packet Subscribe Thread");
		subscribeThread.setDaemon(true);
		subscribeThread.start();
	}

	public void sendToAll(JedisPacket packet) {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			try (Jedis jedis = this.jedisPool.getResource()) {
				String encodedPacket = packet.getClass().getName() + PACKET_MESSAGE_DIVIDER + getGSON().toJson(packet);
				jedis.publish(GLOBAL_MESSAGE_CHANNEL, encodedPacket);
			}
		});
	}

}
