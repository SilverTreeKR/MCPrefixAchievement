package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class PlayerDeathEventListener extends AbstractPrefixListener {
    private final int DEATH_REJECTOR_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.DEATH_REJECTOR)
            .getRequiredStatValue();
    private final int LAVA_CHICKEN_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.LAVA_CHICKEN)
            .getRequiredStatValue();
    private final int WANT_TO_BE_KAISA_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.WANT_TO_BE_KAISA)
            .getRequiredStatValue();

    public PlayerDeathEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerAnyDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int count = increaseStatValue(uuid, PrefixStat.ANY_DEATH_COUNT);

        // 죽음을 거부하는 자
        if (count == DEATH_REJECTOR_REQUIRED_VALUE) {
            PrefixGranter.grantPrefix(player, PrefixName.DEATH_REJECTOR);
        }
    }

    @EventHandler
    public void onPlayerLavaDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getDamageSource().getDamageType() != DamageType.LAVA) {
            return;
        }

        int count = increaseStatValue(uuid, PrefixStat.LAVA_DEATH_COUNT);

        // 라바 치킨
        if (count == LAVA_CHICKEN_REQUIRED_VALUE) {
            giveIfOnlineNextTick(player, createFireResistancePotion());
            PrefixGranter.grantPrefix(player, PrefixName.LAVA_CHICKEN);
        }
    }

    @EventHandler
    public void onPlayerVoidDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getDamageSource().getDamageType() != DamageType.OUT_OF_WORLD) {
            return;
        }
        int count = increaseStatValue(uuid, PrefixStat.VOID_DEATH_COUNT);

        if (count == WANT_TO_BE_KAISA_REQUIRED_VALUE) {
            giveIfOnlineNextTick(player, createPlayerHead(player));
            PrefixGranter.grantPrefix(player, PrefixName.WANT_TO_BE_KAISA);
        }
    }

    private ItemStack createFireResistancePotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*60*3, 0), true);
        potionMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset>화염 저항의 물약"
        ).decoration(TextDecoration.ITALIC, false));
        potionMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>오렌지 맛이 날거 같지만, 사실 딸기 맛이 납니다 !"
        ).decoration(TextDecoration.ITALIC, false)));
        potion.setItemMeta(potionMeta);
        potion.setAmount(1);
        return potion;
    }
    private ItemStack createPlayerHead(Player player) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(player);
        }
        headMeta.customName(MiniMessage.miniMessage().deserialize(
                "<#B8860B><bold>【<gradient:#FFF9C4:#FFFFFF:#FFF9C4>보상</gradient>】</bold></#B8860B> <reset><player>의 머리"
                , Placeholder.parsed("player", player.getName())
        ).decoration(TextDecoration.ITALIC, false));
        headMeta.lore(List.of(MiniMessage.miniMessage().deserialize(
                "<yellow>본인 머리를 헌팅 트로피로 두기엔 너무 마니악하지 않나?"
        ).decoration(TextDecoration.ITALIC, false)));
        playerHead.setItemMeta(headMeta);
        playerHead.setAmount(1);
        return playerHead;
    }
    private void giveIfOnlineNextTick(Player player, ItemStack itemStack) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.isOnline()) {
                player.give(List.of(itemStack));
            }
        });
    }
}
