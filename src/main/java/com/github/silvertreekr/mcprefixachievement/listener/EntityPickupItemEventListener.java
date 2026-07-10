package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class EntityPickupItemEventListener extends AbstractPrefixListener {
    private final int HOME_WRECKER_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.HOME_WRECKER)
            .getRequiredStatValue();
    private final int KOPI_LUWAK_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.KOPI_LUWAK)
            .getRequiredStatValue();
    public EntityPickupItemEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerPickUpDragonEgg(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof  Player player)) {
            return;
        }
        if (!isDragonEgg(event.getItem().getItemStack().getType())) {
            return;
        }
        UUID uuid = player.getUniqueId();
        int count = increaseStatValue(uuid, PrefixStat.GET_DRAGON_EGG);

        // 가정 파괴범
        if (count == HOME_WRECKER_REQUIRED_VALUE) {
            player.give(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
            PrefixGranter.grantPrefix(player, PrefixName.HOME_WRECKER);
        }
    }

    @EventHandler
    public void onPlayerPickupCocoaBenas(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!isCocoaBeans(event.getItem().getItemStack().getType())) {
            return;
        }
        UUID uuid = player.getUniqueId();
        int count = increaseStatValue(uuid, PrefixStat.GET_COCOA_BEANS);

        // 루왁커피
        if (count == KOPI_LUWAK_REQUIRED_VALUE) {
            player.give(createBadlyPouredCoffee());
            PrefixGranter.grantPrefix(player, PrefixName.KOPI_LUWAK);
        }
    }

    private boolean isDragonEgg(Material material) {
        return material == Material.DRAGON_EGG;
    }

    private boolean isCocoaBeans(Material material) {
        return material == Material.COCOA_BEANS;
    }

    private ItemStack createBadlyPouredCoffee() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 20, 0), true);
        potionMeta.customName(Component.text("잘못 내린 커피").decoration(TextDecoration.ITALIC, false));
        potionMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<white>어떤 놈이 내렸는지는 몰라도 매우 쓰다."
        ).decoration(TextDecoration.ITALIC, false)));
        potion.setItemMeta(potionMeta);
        potion.setAmount(1);
        return potion;
    }
}
