package com.dalbitresb.OneChunkWorld;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToggleChunkCommand extends BaseCommand {
    public ToggleChunkCommand(Main plugin) {
        super("togglechunk", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) return false;
        Boolean status = plugin.getPlayerMoveListener().toggleListener();
        if (status) {
            sender.sendMessage("Enforcing player location to selected chunks");
            return true;
        }
        sender.sendMessage("Disabled player location monitoring");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
