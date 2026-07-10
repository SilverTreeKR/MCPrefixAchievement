package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
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
    private final int BUILDER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.BUILDER)
            .getRequiredStatValue();
    private final int GAUDI_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.I_AM_GAUDI)
            .getRequiredStatValue();
    private final int MATCH_GIRL_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.MATCH_GIRL)
            .getRequiredStatValue();

    public BlockPlaceEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerPlaceAnyBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        UserPrefixManager prefixManager = plugin.getUserPrefixManager();

        int count = increaseStatValue(uuid, PrefixStat.PLACE_BLOCK);

        // 건축가
        if (count >= BUILDER_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.BUILDER)) {
            player.give(List.of(new ItemStack(Material.SCAFFOLDING, 64)));
            PrefixGranter.grantPrefix(player, PrefixName.BUILDER);
        }

        // 내가 바로 가우디
        if (count >= GAUDI_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.I_AM_GAUDI)) {
            player.give(List.of(new ItemStack(Material.SPONGE, 5)));
            PrefixGranter.grantPrefix(player, PrefixName.I_AM_GAUDI);
        }
    }

    @EventHandler
    public void onPlayerPlaceRedstoneTorch(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        int count = increaseStatValue(uuid, PrefixStat.PLACE_REDSTONE_TORCH);

        // 성냥팔이 소녀
        if (count == MATCH_GIRL_REQUIRED_VALUE) {
            player.give(List.of(new ItemStack(Material.REDSTONE, 64)));
            PrefixGranter.grantPrefix(player,PrefixName.MATCH_GIRL);
        }
    }
}
