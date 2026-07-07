package com.github.silvertreekr.mcprefixachievement.dao;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;

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
        if (prefixIDs == null || prefixIDs.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return userPrefixesDAO.addPrefixes(uuid, userPrefixes.get(uuid));
    }

    public boolean hasPrefix(UUID uuid, PrefixName id) {
        Set<Integer> prefixIDs = userPrefixes.get(uuid);
        int prefixIndex = id.getPrefixIndex();
        if (prefixIDs == null) {
            return false;
        }
        return prefixIDs.contains(prefixIndex);
    }

    public void addPrefix(UUID uuid, PrefixName id) {
        int prefixIndex = id.getPrefixIndex();
        if (!(MCPrefixAchievement.getInstance().getPrefixConfigManager().existsPrefix(prefixIndex))) {
            throw new IllegalArgumentException("Can't find for prefix by id: Invalid ID");
        }
        HashSet<Integer> prefixes = new HashSet<>(userPrefixes.getOrDefault(uuid, Set.of()));

        prefixes.add(prefixIndex);
        userPrefixes.put(uuid, prefixes);
    }
}
