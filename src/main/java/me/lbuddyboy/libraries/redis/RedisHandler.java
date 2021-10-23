package me.lbuddyboy.libraries.redis;

import com.google.gson.Gson;
import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.libraries.redis.sub.JedisSubscriber;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 2:44 PM
 * BuddyLibs / me.lbuddyboy.libraries.redis
 */

@Getter
public class RedisHandler {

	private final Core instance;
	private final Gson GSON;
	private JedisPool jedisPool;

	public RedisHandler(Core instance) {
		this.instance = instance;
		this.GSON = new Gson();
		int redisID = instance.getConfig().getInt("redis.channel-id");

		try {
			jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 20000, null, redisID);
		} catch (Exception var6) {
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
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "Packet Subscribe Thread");
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
