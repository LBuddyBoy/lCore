package me.lbuddyboy.libraries.redis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

final class UUIDListener
        implements Listener {
    UUIDListener() {
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        RedisUUIDCache.update(event.getUniqueId(), event.getName());
    }
}

