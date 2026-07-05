package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixIds;
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
    private static final int FIRST_DEATH_COUNT = 1;

    public PlayerDeathEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerAnyDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        var anyDeathCount = incrementStat(uuid, PrefixStat.ANY_DEATH_COUNT);
        if (anyDeathCount.isPresent() && anyDeathCount.getAsInt() == FIRST_DEATH_COUNT) {
            PrefixGranter.grantPrefix(player, PrefixIds.FIRST_DEATH);
        }
    }

    @EventHandler
    public void onPlayerLavaDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getDamageSource().getDamageType() != DamageType.LAVA) {
            return;
        }

        var lavaDeathCount = incrementStat(uuid, PrefixStat.LAVA_DEATH_COUNT);
        if (lavaDeathCount.isPresent() && lavaDeathCount.getAsInt() == FIRST_DEATH_COUNT) {
            giveIfOnlineNextTick(player, createFireResistancePotion());
            PrefixGranter.grantPrefix(player, PrefixIds.LAVA_CHICKEN);
        }
    }

    @EventHandler
    public void onPlayerVoidDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getDamageSource().getDamageType() != DamageType.OUT_OF_WORLD) {
            return;
        }

        var voidDeathCount = incrementStat(uuid, PrefixStat.VOID_DEATH_COUNT);
        if (voidDeathCount.isPresent() && voidDeathCount.getAsInt() == FIRST_DEATH_COUNT) {
            giveIfOnlineNextTick(player, createPlayerHead(player));
            PrefixGranter.grantPrefix(player, PrefixIds.VOID_DEATH);
        }
    }

    private ItemStack createFireResistancePotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 3, 0), true);
        potionMeta.customName(Component.text("화염저항의 물약").decoration(TextDecoration.ITALIC, false));
        potion.setItemMeta(potionMeta);
        return potion;
    }

    private ItemStack createPlayerHead(Player player) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(player);
        }
        playerHead.setItemMeta(headMeta);
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
