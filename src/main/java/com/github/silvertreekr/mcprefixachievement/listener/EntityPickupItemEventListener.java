package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class EntityPickupItemEventListener implements Listener {
    private final MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
    private final UserStatsManager statsManager = plugin.getUserStatsManager();

    public EntityPickupItemEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerPickUpDragonEgg(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player){
            UUID uuid = event.getEntity().getUniqueId();
            int prefixID = -1;

            if (event.getItem().getItemStack().getType().equals(Material.DRAGON_EGG)) {
                int count = statsManager.getStatValue(uuid, PrefixStat.GET_DRAGON_EGG);
                count++;
                statsManager.setStatValue(uuid, PrefixStat.GET_DRAGON_EGG, count);
                if (count == 1) {
                    prefixID = 12;
                    player.give(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
                }
            }
            PrefixGranter.grantPrefix(player, prefixID);
        }
    }
}
