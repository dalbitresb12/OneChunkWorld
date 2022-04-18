package com.dalbitresb.OneChunkWorld;

import com.google.common.collect.ImmutableList;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SetChunkCommand extends BaseCommand {
    public SetChunkCommand(Main plugin) {
        super("setchunk", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3 || args.length == 4) {
            World world = plugin.getServer().getWorld(args[0]);
            if (world == null) {
                sender.sendMessage(String.format("Unable to find level: %s", args[0]));
                return true;
            }
            try {
                int chunkX = Integer.parseInt(args[1]);
                int chunkZ = Integer.parseInt(args[2]);
                Integer height = null;
                if (args.length == 4) {
                    height = Integer.parseInt(args[3]);
                }
                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                String msg;
                if (height != null) {
                    plugin.setSelectedChunk(chunk, height);
                    msg = String.format("Selected chunk for level %s set to [%d, %d] at height %d", args[0], chunkX, chunkZ, height);
                } else {
                    plugin.setSelectedChunk(chunk);
                    msg = String.format("Selected chunk for level %s set to [%d, %d]", args[0], chunkX, chunkZ);
                }
                sender.sendMessage(msg);
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(String.format("Unable to parse chunk coordinates: [%s, %s]", args[1], args[2]));
                return true;
            }
        }
        if (args.length == 1) {
            Player player = plugin.getServer().getPlayerExact(args[0]);
            if (player == null || !player.isOnline()) {
                if (plugin.getServer().getWorlds().stream().anyMatch(w -> w.getName().equals(args[0]))) {
                    return false;
                }
                sender.sendMessage(String.format("Unable to find player: %s", args[0]));
                return true;
            }
            Location loc = player.getLocation();
            plugin.setSelectedChunk(loc);
            String msg = String.format("Selected chunk for level %s set to [%d, %d]", Objects.requireNonNull(loc.getWorld()).getName(), loc.getChunk().getX(), loc.getChunk().getZ());
            sender.sendMessage(msg);
            return true;
        }
        if (sender instanceof Player player) {
            String levelName = Objects.requireNonNull(player.getLocation().getWorld()).getName();
            Chunk chunk = player.getLocation().getChunk();
            plugin.setSelectedChunk(player.getLocation());
            String msg = String.format("Selected chunk for level %s set to [%d, %d]", levelName, chunk.getX(), chunk.getZ());
            player.sendMessage(msg);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            List<World> worlds = plugin.getServer().getWorlds();
            worlds.forEach(world -> commands.add(world.getName()));
            ImmutableList<Player> players = ImmutableList.copyOf(plugin.getServer().getOnlinePlayers());
            players.forEach(player -> commands.add(player.getPlayerListName()));
            StringUtil.copyPartialMatches(args[0], commands, completions);
            Collections.sort(completions);
        } else if (args.length >= 2 && args.length <= 4 && sender instanceof Player player) {
            ImmutableList<Player> players = ImmutableList.copyOf(plugin.getServer().getOnlinePlayers());
            if (players.stream().anyMatch(p -> p.getPlayerListName().equals(args[0]))) {
                return completions;
            }

            Chunk chunk = player.getLocation().getChunk();
            int playerHeight = player.getLocation().getBlockY();
            if (args.length == 2) {
                completions.add(String.valueOf(chunk.getX()));
                completions.add(String.format("%d %d", chunk.getX(), chunk.getZ()));
                completions.add(String.format("%d %d %d", chunk.getX(), chunk.getZ(), playerHeight));
            } else if (args.length == 3) {
                completions.add(String.valueOf(chunk.getZ()));
                completions.add(String.format("%d %d", chunk.getZ(), playerHeight));
            } else {
                completions.add(String.valueOf(playerHeight));
            }
        }
        return completions;
    }
}
