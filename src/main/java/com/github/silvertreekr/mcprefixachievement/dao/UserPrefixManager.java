package com.github.silvertreekr.mcprefixachievement.dao;

import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class UserPrefixManager {
    private final UserPrefixesDAO userPrefixesDAO;
    private final PrefixConfigManager prefixConfigManager;
    private final ConcurrentHashMap<UUID, Set<Integer>> userPrefixes = new ConcurrentHashMap<>();

    public UserPrefixManager(UserPrefixesDAO userPrefixesDAO, PrefixConfigManager prefixConfigManager) {
        this.userPrefixesDAO = userPrefixesDAO;
        this.prefixConfigManager = prefixConfigManager;
    }

    public CompletableFuture<Void> loadPlayerPrefixData(UUID uuid) {
        return userPrefixesDAO.getPrefixIDs(uuid).thenAccept(integers -> {
            userPrefixes.put(uuid, integers);
        });
    }

    public void unloadPlayerPrefixData(UUID uuid) {
        userPrefixes.remove(uuid);
    }

    public CompletableFuture<Void> savePlayerPrefixData(UUID uuid) {
        Set<Integer> prefixes = userPrefixes.get(uuid);
        if (prefixes == null) {
            return CompletableFuture.completedFuture(null);
        }
        return userPrefixesDAO.addPrefixes(uuid, Set.copyOf(prefixes));
    }

    public boolean hasPrefix(UUID uuid, int id) {
        Set<Integer> prefixIDs = userPrefixes.get(uuid);
        if (prefixIDs == null) {
            return false;
        }
        return prefixIDs.contains(id);
    }

    public void addPrefix(UUID uuid, int id) {
        if (!prefixConfigManager.existsPrefix(id)) {
            throw new IllegalArgumentException("Can't find for prefix by id: Invalid ID");
        }
        HashSet<Integer> prefixes = new HashSet<>(userPrefixes.getOrDefault(uuid, Set.of()));

        prefixes.add(id);
        userPrefixes.put(uuid, prefixes);
    }
}
