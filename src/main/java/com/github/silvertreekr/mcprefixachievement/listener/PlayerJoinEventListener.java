package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

                // 첫걸음
                if (count == FIRST_STEP_REQUIRED_VALUE) {
                    player.give(createFirstStepReward());
                    PrefixGranter.grantPrefix(player, PrefixName.FIRST_STEP);
                }
            });
        });
    }

    private List<ItemStack> createFirstStepReward() {
        ItemStack bread = new ItemStack(Material.BREAD);
        ItemMeta breadMeta = bread.getItemMeta();

        breadMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset>빵"
        ).decoration(TextDecoration.ITALIC, false));
        breadMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>조금 딱딱하지만 포근함이 느껴지는 빵이다."
        ).decoration(TextDecoration.ITALIC, false)));
        bread.setItemMeta(breadMeta);
        bread.setAmount(16);

        ItemStack torch = new ItemStack(Material.TORCH);
        ItemMeta torchMeta = torch.getItemMeta();

        torchMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset>횃불"
        ).decoration(TextDecoration.ITALIC, false));
        torchMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>동굴을 탐험할 때 유용할 것 같다."
        ).decoration(TextDecoration.ITALIC, false)));
        torch.setItemMeta(torchMeta);
        torch.setAmount(32);

        return List.of(
                bread,
                torch
        );
    }
}