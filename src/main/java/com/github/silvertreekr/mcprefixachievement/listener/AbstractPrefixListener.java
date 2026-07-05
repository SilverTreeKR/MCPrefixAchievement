package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;
import java.util.UUID;

abstract class AbstractPrefixListener implements Listener {
    protected final MCPrefixAchievement plugin;
    protected final UserStatsManager statsManager;

    protected AbstractPrefixListener(@NotNull MCPrefixAchievement plugin) {
        this.plugin = plugin;
        this.statsManager = plugin.getUserStatsManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected OptionalInt incrementStat(@NotNull UUID uuid, @NotNull PrefixStat stat) {
        if (!statsManager.isPlayerLoaded(uuid)) {
            // DB 로드 전 이벤트가 0 기반 캐시를 만들면, 나중에 로드된 값과 충돌할 수 있어 무시한다.
            return OptionalInt.empty();
        }

        int value = statsManager.getStatValue(uuid, stat) + 1;
        statsManager.setStatValue(uuid, stat, value);
        return OptionalInt.of(value);
    }
}
