package com.dalbitresb.OneChunkWorld;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetStatusCommand extends BaseCommand {
    public GetStatusCommand(Main plugin) {
        super("getmonitoringstatus", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) return false;
        final ArrayList<String> msg = new ArrayList<>();
        HashMap<World, Chunk> map = plugin.getChunksMap();
        Boolean listenerEnabled = plugin.getPlayerMoveListener().getIsListenerEnabled();
        Boolean soundEnabled = plugin.getPlayerMoveListener().getIsSoundEnabled();
        msg.add(String.format("Monitoring is currently %s", listenerEnabled ? "enabled" : "disabled"));
        msg.add(String.format("Sound is currently %s", soundEnabled ? "enabled" : "disabled"));
        msg.add(String.format("Monitored chunks: %d", map.size()));
        map.forEach((world, chunk) -> msg.add(String.format("- Level: %s, Chunk: [%d, %d]", world.getName(), chunk.getX(), chunk.getZ())));
        sender.sendMessage(String.join("\n", msg));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
