package com.github.silvertreekr.mcprefixachievement.dao;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserPrefixManager {
    private final UserPrefixesDAO userPrefixesDAO;
    private final HashMap<UUID, Set<Integer>> userPrefixes = new HashMap<>();

    public UserPrefixManager(UserPrefixesDAO userPrefixesDAO) {
        this.userPrefixesDAO = userPrefixesDAO;
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
        Set<Integer> prefixIDs = userPrefixes.get(uuid);
        if (prefixIDs == null) {
            return CompletableFuture.completedFuture(null);
        }
        return userPrefixesDAO.addPrefixes(uuid, prefixIDs);
    }

    public boolean hasPrefix(UUID uuid, int id) {
        Set<Integer> prefixIDs = userPrefixes.get(uuid);
        if (prefixIDs == null) {
            return false;
        }
        return prefixIDs.contains(id);
    }

    public void addPrefix(UUID uuid, int id) {
        if (!(MCPrefixAchievement.getInstance().getPrefixConfigManager().existsPrefix(id))) {
            throw new IllegalArgumentException("Can't find for prefix by id: Invalid ID");
        }
        HashSet<Integer> prefixes = new HashSet<>(userPrefixes.getOrDefault(uuid, Set.of()));

        prefixes.add(id);
        userPrefixes.put(uuid, prefixes);
    }
}
