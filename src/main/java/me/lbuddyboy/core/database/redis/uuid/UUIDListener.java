package me.lbuddyboy.core.database.redis.uuid;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class UUIDListener implements Listener {
    public UUIDListener() {
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUIDCache.update(event.getUniqueId(), event.getName());
    }
}

