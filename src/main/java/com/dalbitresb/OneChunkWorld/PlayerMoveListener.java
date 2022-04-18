package com.dalbitresb.OneChunkWorld;

import com.google.common.collect.ImmutableList;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class PlayerMoveListener implements Listener {
    private final Main plugin;
    private Boolean isListenerEnabled;
    private Boolean isSoundEnabled;

    enum ChunkCorner {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
    }

    public PlayerMoveListener(Main plugin) {
        this.plugin = plugin;
        isListenerEnabled = plugin.getConfig().getBoolean("enabled");
        isSoundEnabled = plugin.getConfig().getBoolean("sound");
    }

    public Boolean getIsListenerEnabled() {
        return isListenerEnabled;
    }

    public Boolean getIsSoundEnabled() {
        return isSoundEnabled;
    }

    public Boolean toggleListener() {
        forcePlayerTeleport();
        isListenerEnabled = !isListenerEnabled;
        plugin.getConfig().set("enabled", isListenerEnabled);
        plugin.saveConfig();
        return isListenerEnabled;
    }

    public Boolean toggleSound() {
        isSoundEnabled = !isSoundEnabled;
        plugin.getConfig().set("sound", isSoundEnabled);
        plugin.saveConfig();
        return isSoundEnabled;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isListenerEnabled) return;
        Player player = event.getPlayer();
        Chunk chunk = plugin.getSelectedChunk(player.getWorld());
        if (chunk == null) return;
        if (!isInsideChunk(player.getLocation(), chunk)) {
            Location nearest = findNearestBlockInside(player.getLocation(), chunk);
            player.teleport(nearest);
            if (isSoundEnabled) {
                player.playSound(nearest, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 2.0F, 1.0F);
            }
        }
    }

    private Location findNearestBlockInside(Location loc, Chunk chunk) {
        Location nearest = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        Location topLeft = getChunkCorner(chunk, ChunkCorner.TopLeft);
        if (loc.getBlockX() < topLeft.getBlockX()) {
            nearest.setX(topLeft.getBlockX() + 0.5f);
        }
        if (loc.getBlockZ() < topLeft.getBlockZ()) {
            nearest.setZ(topLeft.getBlockZ() + 0.5f);
        }

        Location bottomRight = getChunkCorner(chunk, ChunkCorner.BottomRight);
        if (loc.getBlockX() > bottomRight.getBlockX()) {
            nearest.setX(bottomRight.getBlockX() + 0.5f);
        }
        if (loc.getBlockZ() > bottomRight.getBlockZ()) {
            nearest.setZ(bottomRight.getBlockZ() + 0.5f);
        }

        return nearest;
    }

    private Location getChunkCorner(Chunk chunk, ChunkCorner corner) {
        int x = chunk.getX() * 16;
        int z = chunk.getZ() * 16;
        if (corner == ChunkCorner.TopRight || corner == ChunkCorner.BottomRight) {
            x += 15;
        }
        if (corner == ChunkCorner.BottomLeft || corner == ChunkCorner.BottomRight) {
            z += 15;
        }
        return new Location(chunk.getWorld(), x, 0, z);
    }

    private Boolean isInsideChunk(Location loc, Chunk chunk) {
        Location topLeft = getChunkCorner(chunk, ChunkCorner.TopLeft);
        Location bottomRight = getChunkCorner(chunk, ChunkCorner.BottomRight);
        return loc.getBlockX() >= topLeft.getBlockX() &&
                loc.getBlockX() <= bottomRight.getBlockX() &&
                loc.getBlockZ() >= topLeft.getBlockZ() &&
                loc.getBlockZ() <= bottomRight.getBlockZ();
    }

    public Boolean isInsideChunk(Player player) {
        Location location = player.getLocation();
        Chunk chunk = plugin.getSelectedChunk(location.getWorld());
        if (chunk == null) return true;
        return isInsideChunk(location, chunk);
    }

    public void forcePlayerTeleport() {
        ImmutableList<Player> players = ImmutableList.copyOf(plugin.getServer().getOnlinePlayers());
        players.forEach(player -> {
            if (isInsideChunk(player)) return;
            player.teleport(plugin.findChunkCenter(plugin.getSelectedChunk(player.getWorld())));
        });
    }
}
