package me.lbuddyboy.libraries.redis.sub;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.libraries.redis.JedisPacket;
import org.bukkit.Bukkit;

@NoArgsConstructor
public class JedisSubscriber extends redis.clients.jedis.JedisPubSub {

    @SneakyThrows
    @Override
    public void onMessage(String channel, String message) {
        Class<?> packetClass;
        int packetMessageSplit=message.indexOf("||");
        String packetClassStr=message.substring(0, message.indexOf("||"));
        String messageJson=message.substring(packetMessageSplit + "||".length());

        packetClass=Class.forName(packetClassStr);

        JedisPacket packet=(JedisPacket) Core.getInstance().getRedisHandler().getGSON().fromJson(messageJson, packetClass);
        Bukkit.getScheduler().runTask(Core.getInstance(), packet::onReceive);
    }
}

