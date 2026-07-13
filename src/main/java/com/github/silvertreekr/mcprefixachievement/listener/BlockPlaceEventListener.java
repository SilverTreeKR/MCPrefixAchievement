package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.customItems.models.Reward;
import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockPlaceEventListener extends AbstractPrefixListener {
    private final int BUILDER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.BUILDER)
            .getRequiredStatValue();
    private final int LANDLORD_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.LANDLORD)
            .getRequiredStatValue();
    private final int GAUDI_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.I_AM_GAUDI)
            .getRequiredStatValue();
    private final int BLOCK_MASTER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.BLOCK_MASTER)
            .getRequiredStatValue();
    private final int MATCH_GIRL_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.MATCH_GIRL)
            .getRequiredStatValue();
    private final int BOMBER_MAN_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.BOMBER_MAN)
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
            player.give(Reward.BUILDER_REWARD.create(64));
            PrefixGranter.grantPrefix(player, PrefixName.BUILDER);
        }

        // 건물주
        if (count >= LANDLORD_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.LANDLORD)) {
            player.give(Reward.LANDLORD_REWARD.create(1, player));
            PrefixGranter.grantPrefix(player, PrefixName.LANDLORD);
        }
        // 내가 바로 가우디
        if (count >= GAUDI_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.I_AM_GAUDI)) {
            player.give(Reward.GAUDI_REWARD.create(5));
            PrefixGranter.grantPrefix(player, PrefixName.I_AM_GAUDI);
            PrefixGranter.broadcastPrefix(player, PrefixName.I_AM_GAUDI);
        }

        // 블록 마스터
        if (count >= BLOCK_MASTER_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.BLOCK_MASTER)) {
            PrefixGranter.grantPrefix(player, PrefixName.BLOCK_MASTER);
            PrefixGranter.broadcastPrefix(player, PrefixName.BLOCK_MASTER);
        }
    }

    @EventHandler
    public void onPlayerPlaceRedstoneTorch(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!isRedStoneTorch(event.getBlock().getType())) {
            return;
        }

        int count = increaseStatValue(uuid, PrefixStat.PLACE_REDSTONE_TORCH);

        // 성냥팔이 소녀
        if (count == MATCH_GIRL_REQUIRED_VALUE) {
            player.give(Reward.MATCH_GRIL_REWARD.create(64));
            PrefixGranter.grantPrefix(player, PrefixName.MATCH_GIRL);
        }
    }

    @EventHandler
    public void onPlayerPlaceTnt(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!isTnt(event.getBlock().getType())) {
            return;
        }

        int count = increaseStatValue(uuid, PrefixStat.PLACE_TNT);

        // 봄버맨
        if (count == BOMBER_MAN_REQUIRED_VALUE) {
            PrefixGranter.grantPrefix(player, PrefixName.BOMBER_MAN);
        }
    }

    private boolean isRedStoneTorch(Material material) {
        return material == Material.REDSTONE_TORCH;
    }

    private boolean isTnt(Material material) {
        return material == Material.TNT;
    }
}
