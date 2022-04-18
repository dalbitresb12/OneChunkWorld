package com.dalbitresb.OneChunkWorld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToggleSoundCommand extends BaseCommand {
    public ToggleSoundCommand(Main plugin) {
        super("togglesound", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) return false;
        Boolean status = plugin.getPlayerMoveListener().toggleSound();
        if (status) {
            sender.sendMessage("Teleport sound has been enabled");
            return true;
        }
        sender.sendMessage("Teleport sound has been disabled");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
