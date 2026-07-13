package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.customItems.models.Reward;
import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

public class BlockBreakEventListener extends AbstractPrefixListener { ;
    private final int PRO_WORKER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.PRO_WORKER)
            .getRequiredStatValue();
    private final int WORKER_LEADER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.WORKER_LEADER)
            .getRequiredStatValue();
    private final int HUMAN_DIGDA_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.HUMAN_DIGDA)
            .getRequiredStatValue();
    private final int SHOVEL_DEVIL_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.SHOVEL_DEVIL)
            .getRequiredStatValue();
    private final int JEWELLERY_COLLECTOR_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.JEWELLERY_COLLECTOR)
            .getRequiredStatValue();


    public BlockBreakEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerBreakAnyBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        UserPrefixManager prefixManager = plugin.getUserPrefixManager();
        int count = increaseStatValue(uuid, PrefixStat.BREAK_BLOCK);

        // 전문 노가다꾼
        if (count >= PRO_WORKER_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.PRO_WORKER)) {
            player.give(Reward.PRO_WORKER_REWARD.create(1));
            PrefixGranter.grantPrefix(player, PrefixName.PRO_WORKER);
        }

        // 노가다 반장
        if (count >= WORKER_LEADER_REQUIRED_VALUE &&  !prefixManager.hasPrefix(uuid, PrefixName.WORKER_LEADER)) {
            player.give(Reward.WORKER_LEADER_REWARD.create(1));
            PrefixGranter.grantPrefix(player, PrefixName.WORKER_LEADER);
        }

        // 인간 디그다
        if (count >= HUMAN_DIGDA_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.HUMAN_DIGDA)) {
            PrefixGranter.grantPrefix(player, PrefixName.HUMAN_DIGDA);
            PrefixGranter.broadcastPrefix(player, PrefixName.HUMAN_DIGDA);
        }

        // 삽질의 악마
        if (count >= SHOVEL_DEVIL_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.SHOVEL_DEVIL)) {
            PrefixGranter.grantPrefix(player, PrefixName.SHOVEL_DEVIL);
            PrefixGranter.grantPrefix(player, PrefixName.SHOVEL_DEVIL);
        }
    }

    @EventHandler
    public void onPlayerBreakDiamondOre(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!isDiamondOre(event.getBlock().getType())) {
            return;
        }
        int count = increaseStatValue(uuid, PrefixStat.BREAK_DIAMOND_ORE);

        // 보석 수집가
        if (count == JEWELLERY_COLLECTOR_REQUIRED_VALUE) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20 * 60 * 5, 0));
            PrefixGranter.grantPrefix(player, PrefixName.JEWELLERY_COLLECTOR);
        }
    }

    private boolean isDiamondOre(Material material) {
        return material == Material.DIAMOND_ORE || material == Material.DEEPSLATE_DIAMOND_ORE;
    }
}
