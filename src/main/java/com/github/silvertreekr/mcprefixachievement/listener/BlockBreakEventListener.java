package com.github.silvertreekr.mcprefixachievement.listener;

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
            player.give(List.of(createProWorkerReward()));
            PrefixGranter.grantPrefix(player, PrefixName.PRO_WORKER);
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

    private ItemStack createProWorkerReward() {
        ItemStack diamondShovel = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta itemMeta = diamondShovel.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <white>다이아몬드 삽"
        ).decoration(TextDecoration.ITALIC, false));
        itemMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>장인은 도구를 가리지 않지만, 장인도 좋은 도구를 가지는건 좋아합니다."
        ).decoration(TextDecoration.ITALIC, false)));
        itemMeta.addEnchant(Enchantment.UNBREAKING, 3, false);
        diamondShovel.setItemMeta(itemMeta);
        diamondShovel.setAmount(1);
        return diamondShovel;
    }

    private boolean isDiamondOre(Material material) {
        return material == Material.DIAMOND_ORE || material == Material.DEEPSLATE_DIAMOND_ORE;
    }
}
