package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Debug extends BukkitCommand {
    public Debug(@NotNull JavaPlugin plugin) {
        super("debug");
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
        UserStatsManager statsManager = plugin.getUserStatsManager();
        UserPrefixManager prefixManager = plugin.getUserPrefixManager();
        if (sender instanceof Player player) {
            for (PrefixStat prefixStat : PrefixStat.values()) {
                sender.sendMessage(prefixStat.toString() + statsManager.getStatValue(player.getUniqueId(), prefixStat));
            }

        }
        return true;
    }
}
