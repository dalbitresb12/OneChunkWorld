package com.dalbitresb.OneChunkWorld;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {
    protected final String command;
    protected final Main plugin;

    public BaseCommand(String command, Main plugin) {
        this.command = command;
        this.plugin = plugin;
    }

    public void register() {
        PluginCommand comm = plugin.getCommand(command);
        if (comm != null) {
            comm.setExecutor(this);
            comm.setTabCompleter(this);
        }
    }
}
