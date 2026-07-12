package com.github.silvertreekr.mcprefixachievement.listener;

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
            player.give(createBuilderReward());
            PrefixGranter.grantPrefix(player, PrefixName.BUILDER);
        }

        // 건물주
        if (count >= LANDLORD_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.LANDLORD)) {
            player.give(createLandlordReward(player));
            PrefixGranter.grantPrefix(player, PrefixName.LANDLORD);
        }
        // 내가 바로 가우디
        if (count >= GAUDI_REQUIRED_VALUE && !prefixManager.hasPrefix(uuid, PrefixName.I_AM_GAUDI)) {
            player.give(createGaudiReward());
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
            player.give(createMatchGirlReward());
            PrefixGranter.grantPrefix(player,PrefixName.MATCH_GIRL);
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

    private List<ItemStack> createBuilderReward() {
        ItemStack item = new ItemStack(Material.SCAFFOLDING);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset>비계"
        ).decoration(TextDecoration.ITALIC, false));
        itemMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>사다리의 비교적 최신 버전 형태입니다."
        ).decoration(TextDecoration.ITALIC, false)));
        item.setItemMeta(itemMeta);
        item.setAmount(64);
        return List.of(item);
    }

    private List<ItemStack> createLandlordReward(Player player) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        List<Component> itemLore = new ArrayList<>();
        itemMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset><yellow>땅 문서"
        ).decoration(TextDecoration.ITALIC, false));
        itemLore.add(MiniMessage.miniMessage().deserialize(
                "<yellow>주인: <player>",
                Placeholder.component("player", Component.text(player.getName()))
        ).decoration(TextDecoration.ITALIC, false));
        itemLore.add(MiniMessage.miniMessage().deserialize(
                "<yellow>아쉽지만 이 문서의 효력은 없습니다 !"
        ).decoration(TextDecoration.ITALIC,false));
        itemMeta.lore(itemLore);
        item.setItemMeta(itemMeta);
        item.setAmount(1);
        return List.of(item);
    }

    private List<ItemStack> createGaudiReward() {
        ItemStack item = new ItemStack(Material.SPONGE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset>스펀지"
        ).decoration(TextDecoration.ITALIC, false));
        itemMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>흡수력이 매우 뛰어난 친구입니다 !"
        ).decoration(TextDecoration.ITALIC, false)));
        item.setItemMeta(itemMeta);
        item.setAmount(5);

        return List.of(item);
    }

    private List<ItemStack> createMatchGirlReward() {
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset>레드스톤 가루"
        ).decoration(TextDecoration.ITALIC, false));
        itemMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>절대 피가 아닙니다 !"
        ).decoration(TextDecoration.ITALIC, false)));
        item.setItemMeta(itemMeta);
        item.setAmount(64);

        return  List.of(item);
    }
}
