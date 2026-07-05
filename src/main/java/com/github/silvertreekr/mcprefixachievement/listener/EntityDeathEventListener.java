package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixIds;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EntityDeathEventListener extends AbstractPrefixListener {
    private static final int FIRST_KILL = 1;

    public EntityDeathEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.ENDER_DRAGON || event.getEntity().getKiller() == null) {
            return;
        }

        Player player = event.getEntity().getKiller();
        UUID uuid = player.getUniqueId();
        int count = incrementStat(uuid, PrefixStat.KILL_ENDER_DRAGON);
        if (count == FIRST_KILL) {
            if (PrefixGranter.grantPrefix(player, PrefixIds.DRAGON_SLAYER)) {
                PrefixGranter.broadcastPrefix(player, PrefixIds.DRAGON_SLAYER);
            }
        }
    }

    @EventHandler
    public void onEndermanDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.ENDERMAN || event.getEntity().getKiller() == null) {
            return;
        }

        Player player = event.getEntity().getKiller();
        boolean killedWithMace = player.getInventory().getItemInMainHand().getType() == Material.MACE;
        if (!killedWithMace) {
            return;
        }

        UUID uuid = player.getUniqueId();
        int count = incrementStat(uuid, PrefixStat.KILL_ENDERMAN_BY_MACE);
        if (count == FIRST_KILL) {
            PrefixGranter.grantPrefix(player, PrefixIds.HAMMER_ON);
        }
    }
}
