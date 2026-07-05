package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class EntityDeathEventListener implements Listener {
    private final MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
    private final UserStatsManager statsManager = plugin.getUserStatsManager();

    public EntityDeathEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent event) {
        int prefixID = -1;

        if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            if (event.getEntity().getKiller() != null) {
                Player player = event.getEntity().getKiller();
                UUID uuid = player.getUniqueId();
                int count = statsManager.getStatValue(uuid, PrefixStat.KILL_ENDER_DRAGON);
                count++;
                statsManager.setStatValue(uuid, PrefixStat.KILL_ENDER_DRAGON, count);
                if (count == 1) {
                    // 용살자
                    prefixID = 6;
                }
                PrefixGranter.grantPrefix(player, prefixID);
            }
        }
    }

    @EventHandler
    public void onEndermandDeath(EntityDeathEvent event) {
        int prefixID = -1;

        if (event.getEntityType().equals(EntityType.ENDERMAN)) {
            if (event.getEntity().getKiller() != null) {
                Player player = event.getEntity().getKiller();
                UUID uuid = player.getUniqueId();
                Player killer = event.getEntity().getKiller();
                boolean killedWithMace = killer.getInventory().getItemInMainHand().getType().equals(Material.MACE);
                if (killedWithMace) {
                    int count = statsManager.getStatValue(uuid, PrefixStat.KILL_ENDERMAN_BY_MACE);
                    count++;
                    statsManager.setStatValue(uuid, PrefixStat.KILL_ENDERMAN_BY_MACE, count);
                    if (count == 1) {
                        // 망치 나가신다
                        prefixID = 7;
                    }
                }
                PrefixGranter.grantPrefix(player, prefixID);
            }
        }
    }
}
