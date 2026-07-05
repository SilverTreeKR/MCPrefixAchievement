package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class BlockPlaceEventListener implements Listener {
    private final MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
    private final UserStatsManager statsManager = plugin.getUserStatsManager();

    public BlockPlaceEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerPlaceAnyBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int prefixID = -1;

        int anyBlockPlaceCount = statsManager.getStatValue(uuid, PrefixStat.PLACE_BLOCK);
        anyBlockPlaceCount++;
        statsManager.setStatValue(uuid, PrefixStat.PLACE_BLOCK, anyBlockPlaceCount);
        if (anyBlockPlaceCount == 5000) {
            // 건축가
            prefixID = 8;
            List<ItemStack> items = List.of(new ItemStack(Material.SCAFFOLDING, 64));
            event.getPlayer().give(items);
        } else if (anyBlockPlaceCount == 50000) {
            // 내가 바로 가우디
            prefixID = 10;
            List<ItemStack> items = List.of(new ItemStack(Material.SPONGE, 5));
            event.getPlayer().give(items);
        }
        PrefixGranter.grantPrefix(player, prefixID);
    }
}
