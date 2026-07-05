package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerQuitEventListener implements Listener {
    private final MCPrefixAchievement plugin;

    public PlayerQuitEventListener(MCPrefixAchievement plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        CompletableFuture<Void> prefixSave = plugin.getUserPrefixManager().savePlayerPrefixData(uuid);
        CompletableFuture<Void> statsSave = plugin.getUserStatsManager().savePlayerStatsData(uuid);

        CompletableFuture.allOf(prefixSave, statsSave).whenComplete((ignored, throwable) -> {
            if (throwable != null) {
                plugin.getSLF4JLogger().error("Failed to save player prefix achievement data: {}", event.getPlayer().getName(), throwable);
            }

            // 저장 Future가 각 manager에서 스냅샷을 잡은 뒤라, 완료 시점에 캐시를 내려도 저장 데이터가 변하지 않는다.
            plugin.getUserPrefixManager().unloadPlayerPrefixData(uuid);
            plugin.getUserStatsManager().unloadPlayerStatsData(uuid);
        });
    }
}
