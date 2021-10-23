package me.lbuddyboy.libraries.redis;

import me.lbuddyboy.core.Core;
import org.bukkit.Bukkit;

public interface JedisPacket {

    default Core getPlugin() {
        return Core.getInstance();
    }

    void onReceive();

    String getID();

    default void send() {
        getPlugin().getRedisHandler().sendToAll(this);
        Bukkit.getLogger().info("[Redis Packet Incoming] " + getID() + " successfully established!");
    }
}

