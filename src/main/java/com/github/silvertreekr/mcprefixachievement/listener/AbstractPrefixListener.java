package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

abstract class AbstractPrefixListener implements Listener {
    protected final MCPrefixAchievement plugin;
    protected final UserStatsManager statsManager;

    protected AbstractPrefixListener(@NotNull MCPrefixAchievement plugin) {
        this.plugin = plugin;
        this.statsManager = plugin.getUserStatsManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected int incrementStat(@NotNull UUID uuid, @NotNull PrefixStat stat) {
        int value = statsManager.getStatValue(uuid, stat) + 1;
        statsManager.setStatValue(uuid, stat, value);
        return value;
    }
}
