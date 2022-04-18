package com.dalbitresb.OneChunkWorld;

import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public abstract class WorldConfig {
    public static void save(FileConfiguration config, HashMap<World, Chunk> map) {
        if (map.isEmpty()) return;
        map.forEach((world, chunk) -> {
            String key = "worlds." + world.getUID();
            config.set(key + ".levelName", world.getName());
            config.set(key + ".chunkX", chunk.getX());
            config.set(key + ".chunkZ", chunk.getZ());
        });
    }

    public static HashMap<World, Chunk> read(Main plugin) {
        HashMap<World, Chunk> map = new HashMap<>();

        FileConfiguration config = plugin.getConfig();
        if (config.get("worlds") == null) return map;

        Server server = plugin.getServer();
        List<World> worlds = server.getWorlds();

        worlds.forEach(world -> {
            String key = "worlds." + world.getUID();
            if (config.get(key) != null) {
                int chunkX = config.getInt(key + ".chunkX");
                int chunkZ = config.getInt(key + ".chunkZ");
                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                map.put(world, chunk);
            }
        });

        return map;
    }
}
