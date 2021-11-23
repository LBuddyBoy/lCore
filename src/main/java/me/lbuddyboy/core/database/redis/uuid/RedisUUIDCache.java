package me.lbuddyboy.core.database.redis.uuid;

import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RedisUUIDCache {
    private static final Map<UUID, String> uuidToName=new ConcurrentHashMap<>();
    private static final Map<String, UUID> nameToUuid=new ConcurrentHashMap<>();

    public RedisUUIDCache() {
        Jedis redis = Core.getInstance().getRedisHandler().getJedisPool().getResource();
        Map<String, String> cache=redis.hgetAll("UUIDCache");
        for ( Map.Entry<String, String> cacheEntry : cache.entrySet() ) {
            UUID uuid=UUID.fromString(cacheEntry.getKey());
            String name=cacheEntry.getValue();
            uuidToName.put(uuid, name);
            nameToUuid.put(name.toLowerCase(), uuid);
        }
    }

    public static UUID uuid(String name) {
        return nameToUuid.get(name.toLowerCase());
    }

    public static String name(UUID uuid) {
        if (Configuration.REDIS_SYNC.getBoolean()) {
            try {
                return uuidToName.get(uuid);
            } catch (Exception ignored) {
                return "Console";
            }
        } else {
            try {
                return Bukkit.getOfflinePlayer(uuid).getName();
            } catch (Exception ignored) {
                return "Console";
            }
        }
    }

    public static boolean ensure(UUID uuid) {
        if (name(uuid).equals("null")) {
            Bukkit.getLogger().warning(uuid + " didn't have a cached name.");
            return false;
        }
        return true;
    }

    public static void update(final UUID uuid, final String name) {
        uuidToName.put(uuid, name);
        for ( Map.Entry<String, UUID> entry : new HashMap<>(nameToUuid).entrySet() ) {
            if (!entry.getValue().equals(uuid)) continue;
            nameToUuid.remove(entry.getKey());
        }
        nameToUuid.put(name.toLowerCase(), uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                Jedis redis = Core.getInstance().getRedisHandler().getJedisPool().getResource();
                redis.hset("UUIDCache", uuid.toString(), name);
            }
        }.runTaskAsynchronously(Core.getInstance());
    }
}

