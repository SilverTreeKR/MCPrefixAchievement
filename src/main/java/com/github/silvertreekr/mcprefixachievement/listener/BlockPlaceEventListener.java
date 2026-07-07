package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class BlockPlaceEventListener extends AbstractPrefixListener {
    private static final int BUILDER_REQUIRED_VALUE = 5000;
    private static final int GAUDI_REQUIRED_VALUE = 50000;

    public BlockPlaceEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerPlaceAnyBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        int count = increaseStatValue(uuid, PrefixStat.PLACE_BLOCK);

        if (count == BUILDER_REQUIRED_VALUE) {
            player.give(List.of(new ItemStack(Material.SCAFFOLDING, 64)));
            PrefixGranter.grantPrefix(player, PrefixName.BUILDER);
        } else if (count == GAUDI_REQUIRED_VALUE) {
            player.give(List.of(new ItemStack(Material.SPONGE, 5)));
            PrefixGranter.grantPrefix(player, PrefixName.I_AM_GAUDI);
        }
    }
}
