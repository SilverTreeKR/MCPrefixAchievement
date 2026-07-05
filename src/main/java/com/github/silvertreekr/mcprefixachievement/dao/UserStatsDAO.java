package com.github.silvertreekr.mcprefixachievement.dao;

import com.github.silvertreekr.mcprefixachievement.database.MysqlDatabase;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserStatsDAO {
    private final MysqlDatabase database;

    public UserStatsDAO(MysqlDatabase database) {
        this.database = database;
    }

    public CompletableFuture<Void> initialize() {
        return database.runAsync(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS user_stats (
                        uuid VARCHAR(36) NOT NULL,
                        stat VARCHAR(64) NOT NULL,
                        value BIGINT NOT NULL DEFAULT 0,
                        PRIMARY KEY (uuid, stat),
                        INDEX idx_stat (stat)
                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                        """);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Map<PrefixStat, Integer>> getPlayerStats(UUID uuid) {
        return database.supplyAsync(connection -> {
            String sql = "SELECT stat, value FROM user_stats WHERE uuid = ?;";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Map<PrefixStat, Integer> playerStats = new HashMap<>();
                statement.setString(1, uuid.toString());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        try {
                            PrefixStat stat = PrefixStat.valueOf(resultSet.getString("stat").toUpperCase());
                            playerStats.put(stat, resultSet.getInt("value"));
                        } catch (IllegalArgumentException ignored) {
                            // DB에 남은 구버전 stat 값은 현재 enum과 매칭되지 않으므로 로드에서 제외한다.
                        }
                    }
                }
                return playerStats;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> setStats(UUID uuid, Map<PrefixStat, Integer> stats) {
        if (stats.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        return database.runAsync(connection -> {
            String sql = "INSERT INTO user_stats(uuid, stat, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Map.Entry<PrefixStat, Integer> entry : stats.entrySet()) {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, entry.getKey().name());
                    statement.setInt(3, entry.getValue());
                    statement.addBatch();
                }
                statement.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
