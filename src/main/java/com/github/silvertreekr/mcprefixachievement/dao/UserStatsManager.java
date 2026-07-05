package com.github.silvertreekr.mcprefixachievement.dao;

import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserStatsManager {
    private final UserStatsDAO userStatsDAO;
    private final HashMap<UUID, Map<PrefixStat, Integer>> userStats = new HashMap<>();

    public UserStatsManager(UserStatsDAO userStatsDAO) {
        this.userStatsDAO = userStatsDAO;
    }

    public CompletableFuture<Void> loadPlayerStatsData(UUID uuid) {
        return userStatsDAO.getPlayerStats(uuid).thenAccept(prefixStatIntegerMap -> {
            for (PrefixStat prefixStat: PrefixStat.values()) {
                if (prefixStatIntegerMap.containsKey(prefixStat)) {
                    continue;
                }
                prefixStatIntegerMap.put(prefixStat, 0);
            }
            userStats.put(uuid, prefixStatIntegerMap);
        });
    }

    public void unloadPlayerStatsData(UUID uuid) {
        userStats.remove(uuid);
    }

    public CompletableFuture<Void> savePlayerStatsData(UUID uuid) {
        Map<PrefixStat, Integer> stats = userStats.get(uuid);
        if (stats == null) {
            return CompletableFuture.completedFuture(null);
        }
        return userStatsDAO.setStats(uuid, stats);
    }

    public int getStatValue(UUID uuid, PrefixStat stat) {
        Map<PrefixStat, Integer> statIntegerMap = userStats.get(uuid);
        if (statIntegerMap == null) {
            return 0;
        }
        return statIntegerMap.getOrDefault(stat, 0);
    }

    public void setStatValue(UUID uuid, PrefixStat stat, int value) {
        HashMap<PrefixStat, Integer> stats = new HashMap<>(userStats.getOrDefault(uuid, Map.of()));
        stats.put(stat, value);
        userStats.put(uuid, stats);
    }
}
