package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixIds;
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
    private static final int BUILDER_REQUIRED_BLOCKS = 5000;
    private static final int GAUDI_REQUIRED_BLOCKS = 50000;

    public BlockPlaceEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerPlaceAnyBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        int placedBlocks = incrementStat(uuid, PrefixStat.PLACE_BLOCK);
        if (placedBlocks == BUILDER_REQUIRED_BLOCKS) {
            player.give(List.of(new ItemStack(Material.SCAFFOLDING, 64)));
            PrefixGranter.grantPrefix(player, PrefixIds.BUILDER);
        } else if (placedBlocks == GAUDI_REQUIRED_BLOCKS) {
            player.give(List.of(new ItemStack(Material.SPONGE, 5)));
            PrefixGranter.grantPrefix(player, PrefixIds.GAUDI);
        }
    }
}
