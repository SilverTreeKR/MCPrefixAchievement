package com.github.silvertreekr.mcprefixachievement;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.silvertreekr.mcprefixachievement.database.MysqlDatabase;
import org.jetbrains.annotations.NotNull;

public final class MCPrefixAchievement extends JavaPlugin {
    private static @NotNull MysqlDatabase mysqlDatabase;

    public static @NotNull MysqlDatabase getMysqlDatabase() {
        return mysqlDatabase;
    }

    @Override
    public void onEnable() {
        new PrefixCommand(this);


        // Initialize the DefaultConfig
        saveDefaultConfig();
        reloadConfig();
        try {
            mysqlDatabase = MysqlDatabase.initialize(this);
        } catch (Exception e) {
            getSLF4JLogger().error("Failed to initialize MySQL database", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
