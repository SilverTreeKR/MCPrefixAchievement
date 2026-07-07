package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
    private static final int DEATH_REJECTOR_REQUIRED_VALUE = 1;
    private static final int LAVA_CHICKEN_REQUIRED_VALUE = 1;
    private static final int WANT_TO_BE_KAISA_REQUIRED_VALUE = 1;

    public PlayerDeathEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerAnyDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int count = increaseStatValue(uuid, PrefixStat.ANY_DEATH_COUNT);

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
        potionMeta.customName(Component.text("화염 저항의 물약").decoration(TextDecoration.ITALIC, false));
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
