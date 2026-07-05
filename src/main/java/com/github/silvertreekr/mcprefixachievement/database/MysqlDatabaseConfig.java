package com.github.silvertreekr.mcprefixachievement.database;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MysqlDatabaseConfig {
    private final String address;
    private final String username;
    private final String password;
    private final String database;
    private final Map<String, Object> properties;

    private MysqlDatabaseConfig(@NotNull String address, @NotNull String username, @NotNull String password, @NotNull String database, @NotNull Map<String, Object> properties) {
        this.address = address;
        this.username = username;
        this.password = password;
        this.database = database;
        this.properties = properties;
    }

    public static @NotNull MysqlDatabaseConfig fromBukkitConfig(@NotNull ConfigurationSection config) throws IllegalArgumentException {
        String address = config.getString("address");
        if (address == null) {
            throw new IllegalArgumentException("MySQL address is null");
        }
        String username = config.getString("username");
        if (username == null) {
            throw new IllegalArgumentException("MySQL username is null");
        }
        String password = config.getString("password");
        if (password == null) {
            throw new IllegalArgumentException("MySQL password is null");
        }
        String database = config.getString("database");
        if (database == null) {
            throw new IllegalArgumentException("MySQL database is null");
        }

        ConfigurationSection propertiesSection = config.getConfigurationSection("properties");
        Map<String, Object> properties = propertiesSection != null ? propertiesSection.getValues(false) : Map.of();

        return new MysqlDatabaseConfig(address, username, password, database, properties);
    }

    public @NotNull String getAddress() {
        return address;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public @NotNull String getDatabase() {
        return database;
    }

    public @NotNull Map<String, Object> getProperties() {
        return properties;
    }
}
