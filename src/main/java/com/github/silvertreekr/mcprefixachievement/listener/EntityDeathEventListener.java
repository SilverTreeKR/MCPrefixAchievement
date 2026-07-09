package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EntityDeathEventListener extends AbstractPrefixListener {
    private final int DRAGON_SLAYER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.DRAGON_SLAYER)
            .getRequiredStatValue();
    private final int HAMMER_ON_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.HAMMER_ON)
            .getRequiredStatValue();

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
        int count = increaseStatValue(uuid, PrefixStat.KILL_ENDER_DRAGON);

        if (count == DRAGON_SLAYER_REQUIRED_VALUE) {
            PrefixGranter.grantPrefix(player, PrefixName.DRAGON_SLAYER);
            PrefixGranter.broadcastPrefix(player, PrefixName.DRAGON_SLAYER);
        }
    }

    @EventHandler
    public void onEndermandDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.ENDERMAN || event.getEntity().getKiller() == null) {
            return;
        }

        Player player = event.getEntity().getKiller();
        UUID uuid = player.getUniqueId();
        boolean killedWithMace = player.getInventory().getItemInMainHand().getType().equals(Material.MACE);

        if (!killedWithMace) {
            return;
        }

        int count = increaseStatValue(uuid, PrefixStat.KILL_ENDERMAN_BY_MACE);
        if (count == HAMMER_ON_REQUIRED_VALUE) {
            PrefixGranter.grantPrefix(player, PrefixName.HAMMER_ON);
        }
    }
}
