package me.lbuddyboy.core.database.redis.sub;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.database.redis.RedisHandler;
import org.bukkit.Bukkit;

@NoArgsConstructor
public class JedisSubscriber extends redis.clients.jedis.JedisPubSub {

	@SneakyThrows
	@Override
	public void onMessage(String channel, String message) {
		Class<?> packetClass;
		int packetMessageSplit = message.indexOf("||");
		String packetClassStr = message.substring(0, packetMessageSplit);
		String messageJson = message.substring(packetMessageSplit + "||".length());
		try {
			packetClass = Class.forName(packetClassStr);
		} catch (ClassNotFoundException ignored) {
			System.out.println("All your cores need to be on the same version for the packets to establish correctly");
			return;
		}
		JedisPacket packet = (JedisPacket) RedisHandler.getGSON().fromJson(messageJson, packetClass);
		Bukkit.getScheduler().runTask(Core.getInstance(), packet::onReceive);
	}
}

