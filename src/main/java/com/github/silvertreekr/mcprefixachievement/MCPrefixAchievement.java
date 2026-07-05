package com.github.silvertreekr.mcprefixachievement;

import com.github.silvertreekr.mcprefixachievement.command.HammerOnCommand;
import com.github.silvertreekr.mcprefixachievement.command.PrefixCommand;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigLoader;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixesDAO;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsDAO;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.database.MysqlDatabase;
import com.github.silvertreekr.mcprefixachievement.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MCPrefixAchievement extends JavaPlugin {
    private static MCPrefixAchievement instance;
    private MysqlDatabase mysqlDatabase;
    private PrefixConfigLoader prefixConfigLoader;
    private PrefixConfigManager prefixConfigManager;
    private UserPrefixManager userPrefixManager;
    private UserStatsManager userStatsManager;

    public static @NotNull MCPrefixAchievement getInstance() {
        return instance;
    }

    public @NotNull MysqlDatabase getMysqlDatabase() {
        return mysqlDatabase;
    }

    public @NotNull PrefixConfigLoader getPrefixConfigLoader() {
        return prefixConfigLoader;
    }

    public @NotNull PrefixConfigManager getPrefixConfigManager() {
        return prefixConfigManager;
    }

    public @NotNull UserPrefixManager getUserPrefixManager() {
        return userPrefixManager;
    }

    public @NotNull UserStatsManager getUserStatsManager() {
        return userStatsManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Initialize the DefaultConfig
        saveDefaultConfig();
        reloadConfig();

        // Initialize the PrefixConfigLoader
        prefixConfigLoader = new PrefixConfigLoader(this);
        prefixConfigLoader.loadPrefixConfig();

        // Initialize the PrefixConfigManager
        prefixConfigManager = new PrefixConfigManager(this);
        prefixConfigManager.readConfig(prefixConfigLoader);

        // Initialize commands
        new PrefixCommand(this);
        new HammerOnCommand(this);

        // Initialize the MySQL Database
        try {
            mysqlDatabase = MysqlDatabase.initialize(this);

        } catch (Exception e) {
            getSLF4JLogger().error("Failed to initialize MySQL database", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        UserPrefixesDAO userPrefixesDAO = new UserPrefixesDAO(mysqlDatabase);
        userPrefixManager = new UserPrefixManager(userPrefixesDAO, prefixConfigManager);

        UserStatsDAO userStatsDAO  = new UserStatsDAO(mysqlDatabase);
        userStatsManager = new UserStatsManager(userStatsDAO);

        try {
            CompletableFuture.allOf(
                    userPrefixesDAO.initialize(),
                    userStatsDAO.initialize()
            ).join();
        } catch (Exception e) {
            getSLF4JLogger().error("Failed to initialize database tables", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register event listeners
        new BlockBreakEventListener(this);
        new BlockPlaceEventListener(this);
        new EntityDeathEventListener(this);
        new EntityPickupItemEventListener(this);
        new PlayerDeathEventListener(this);
        new PlayerGetDragonBreathEventListener(this);
        new PlayerJoinEventListener(this);
        new PlayerQuitEventListener(this);
    }

    @Override
    public void onDisable() {
        try {
            if (userStatsManager != null && userPrefixManager != null) {
                List<CompletableFuture<Void>> saveTasks = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    saveTasks.add(userStatsManager.savePlayerStatsData(uuid));
                    saveTasks.add(userPrefixManager.savePlayerPrefixData(uuid));
                }

                // 서버 종료 중에는 비동기 저장이 끝나기 전에 DB executor가 닫히면 데이터가 유실될 수 있다.
                CompletableFuture.allOf(saveTasks.toArray(CompletableFuture[]::new)).join();
            }
        } catch (Exception e) {
            getSLF4JLogger().error("Failed to save online player data while disabling plugin", e);
        } finally {
            if (mysqlDatabase != null) {
                mysqlDatabase.shutdown();
            }
        }
    }
}
