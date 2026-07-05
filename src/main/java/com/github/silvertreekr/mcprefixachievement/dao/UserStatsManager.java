package com.github.silvertreekr.mcprefixachievement.dao;

import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UserStatsManager {
    private final UserStatsDAO userStatsDAO;
    private final ConcurrentHashMap<UUID, Map<PrefixStat, Integer>> userStats = new ConcurrentHashMap<>();

    public UserStatsManager(UserStatsDAO userStatsDAO) {
        this.userStatsDAO = userStatsDAO;
    }

    public CompletableFuture<Void> loadPlayerStatsData(UUID uuid) {
        return userStatsDAO.getPlayerStats(uuid).thenAccept(prefixStatIntegerMap -> {
            EnumMap<PrefixStat, Integer> loadedStats = new EnumMap<>(PrefixStat.class);
            for (PrefixStat prefixStat: PrefixStat.values()) {
                loadedStats.put(prefixStat, prefixStatIntegerMap.getOrDefault(prefixStat, 0));
            }
            userStats.put(uuid, loadedStats);
        });
    }

    public void unloadPlayerStatsData(UUID uuid) {
        userStats.remove(uuid);
    }

    public boolean isPlayerLoaded(UUID uuid) {
        return userStats.containsKey(uuid);
    }

    public CompletableFuture<Void> savePlayerStatsData(UUID uuid) {
        Map<PrefixStat, Integer> stats = userStats.get(uuid);
        if (stats == null) {
            return CompletableFuture.completedFuture(null);
        }
        return userStatsDAO.setStats(uuid, new EnumMap<>(stats));
    }

    public int getStatValue(UUID uuid, PrefixStat stat) {
        Map<PrefixStat, Integer> statIntegerMap = userStats.get(uuid);
        if (statIntegerMap == null) {
            return 0;
        }
        return statIntegerMap.getOrDefault(stat, 0);
    }

    public void setStatValue(UUID uuid, PrefixStat stat, int value) {
        if (!isPlayerLoaded(uuid)) {
            return;
        }

        EnumMap<PrefixStat, Integer> stats = new EnumMap<>(PrefixStat.class);
        stats.putAll(userStats.get(uuid));
        stats.put(stat, value);
        userStats.put(uuid, stats);
    }
}
