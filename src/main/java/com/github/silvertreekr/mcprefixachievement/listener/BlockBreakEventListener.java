package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixIds;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class BlockBreakEventListener extends AbstractPrefixListener {
    private static final int MINER_REQUIRED_BLOCKS = 10000;
    private static final int DIAMOND_ORE_REQUIRED_BLOCKS = 1;

    public BlockBreakEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerBreakAnyBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        int count = incrementStat(uuid, PrefixStat.BREAK_BLOCK);
        if (count == MINER_REQUIRED_BLOCKS) {
            player.give(List.of(createMinerReward()));
            PrefixGranter.grantPrefix(player, PrefixIds.MINER);
        }
    }

    @EventHandler
    public void onPlayerBreakDiamondOre(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!isDiamondOre(event.getBlock().getType())) {
            return;
        }

        int count = incrementStat(uuid, PrefixStat.BREAK_DIAMOND_ORE);
        if (count == DIAMOND_ORE_REQUIRED_BLOCKS) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20 * 60 * 5, 0));
            PrefixGranter.grantPrefix(player, PrefixIds.DIAMOND_COLLECTOR);
        }
    }

    private boolean isDiamondOre(Material material) {
        return material == Material.DIAMOND_ORE || material == Material.DEEPSLATE_DIAMOND_ORE;
    }

    private ItemStack createMinerReward() {
        ItemStack diamondShovel = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta itemMeta = diamondShovel.getItemMeta();
        itemMeta.addEnchant(Enchantment.UNBREAKING, 3, false);
        diamondShovel.setItemMeta(itemMeta);
        return diamondShovel;
    }
}
