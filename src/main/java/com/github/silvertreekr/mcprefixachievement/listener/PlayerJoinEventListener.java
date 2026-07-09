package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerJoinEventListener extends AbstractPrefixListener {
    private final int FIRST_STEP_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.FIRST_STEP)
            .getRequiredStatValue();
    public PlayerJoinEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UserPrefixManager prefixManager = plugin.getUserPrefixManager();
        UserStatsManager statsManager = plugin.getUserStatsManager();

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();


        // load PlayerPrefixData & PlayerStatsData to each Cache
        CompletableFuture<Void> prefixLoad = prefixManager.loadPlayerPrefixData(uuid);
        CompletableFuture<Void> statsLoad = statsManager.loadPlayerStatsData(uuid);

        CompletableFuture.allOf(prefixLoad, statsLoad).thenRun(() -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!player.isOnline()) {
                    return; // 로드되는 사이 플레이어가 이미 퇴장했으면 아무것도 하지 않음
                }

                int count = increaseStatValue(uuid, PrefixStat.FIRST_JOIN);
                if (count == FIRST_STEP_REQUIRED_VALUE) {
                    player.give(createFirstStepReward());
                    PrefixGranter.grantPrefix(player, PrefixName.FIRST_STEP);
                }
            });
        });
    }

    private List<ItemStack> createFirstStepReward() {
        return List.of(
                new ItemStack(Material.BREAD, 16),
                new ItemStack(Material.TORCH, 32)
        );
    }
}