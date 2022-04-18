package com.dalbitresb.OneChunkWorld;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    private HashMap<World, Chunk> selectedChunks = null;
    private PlayerMoveListener playerMoveListener = null;

    @Override
    public void onEnable() {
        selectedChunks = WorldConfig.read(this);
        registerConfig();
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        FileConfiguration config = getConfig();
        config.set("enabled", playerMoveListener.getIsListenerEnabled());
        config.set("sound", playerMoveListener.getIsSoundEnabled());
        WorldConfig.save(config, selectedChunks);
        saveConfig();
    }

    private void registerConfig() {
        FileConfiguration config = getConfig();
        config.addDefault("version", getDescription().getVersion());
        config.addDefault("enabled", false);
        config.addDefault("sound", true);
        config.addDefault("worlds", null);
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void registerEvents() {
        playerMoveListener = new PlayerMoveListener(this);
        Bukkit.getPluginManager().registerEvents(getPlayerMoveListener(), this);
    }

    private void registerCommands() {
        new GetStatusCommand(this).register();
        new SetChunkCommand(this).register();
        new ToggleChunkCommand(this).register();
        new ToggleSoundCommand(this).register();
    }

    public void setSelectedChunk(Location location) {
        Chunk chunk = location.getChunk();
        World world = chunk.getWorld();
        world.setSpawnLocation(location);
        selectedChunks.put(world, chunk);
        if (playerMoveListener.getIsListenerEnabled()) {
            playerMoveListener.forcePlayerTeleport();
        }
        WorldConfig.save(getConfig(), selectedChunks);
        saveConfig();
    }

    public void setSelectedChunk(Chunk chunk) {
        Location center = findChunkCenter(chunk);
        setSelectedChunk(center);
    }

    public void setSelectedChunk(Chunk chunk, int y) {
        Location center = findChunkCenter(chunk);
        center.setY(y);
        setSelectedChunk(center);
    }

    public Location findChunkCenter(Chunk chunk) {
        World world = chunk.getWorld();
        int chunkCenterX = chunk.getX() * 16 + 8;
        int chunkCenterZ = chunk.getZ() * 16 + 8;
        int highestY = world.getHighestBlockYAt(chunkCenterX, chunkCenterZ) + 2;
        return new Location(world, chunkCenterX, highestY, chunkCenterZ);
    }

    public HashMap<World, Chunk> getChunksMap() {
        return selectedChunks;
    }

    public Chunk getSelectedChunk(World world) {
        return selectedChunks.get(world);
    }

    public PlayerMoveListener getPlayerMoveListener() {
        return playerMoveListener;
    }
}
