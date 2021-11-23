package me.lbuddyboy.core.database.redis;

import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import org.bukkit.Bukkit;

public interface JedisPacket {

    default Core getPlugin() {
        return Core.getInstance();
    }

    void onReceive();

    String getID();

    default void send() {
        if (!Configuration.REDIS_SYNC.getBoolean()) return;
        getPlugin().getRedisHandler().sendToAll(this);
        Bukkit.getLogger().info("[Redis Packet Incoming] " + getID() + " successfully established!");
    }
}

