package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EntityPickupItemEventListener extends AbstractPrefixListener {
    private static final int HOME_WRECKER_REQUIRED_VALUE = 1;
    public EntityPickupItemEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerPickUpDragonEgg(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof  Player player)) {
            return;
        }
        if (event.getItem().getItemStack().getType() != Material.DRAGON_EGG) {
            return;
        }
        UUID uuid = event.getEntity().getUniqueId();
        int count = increaseStatValue(uuid, PrefixStat.GET_DRAGON_EGG);
        if (count == HOME_WRECKER_REQUIRED_VALUE) {
            player.give(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
            PrefixGranter.grantPrefix(player, PrefixName.HOME_WRECKER);
        }
    }
}
