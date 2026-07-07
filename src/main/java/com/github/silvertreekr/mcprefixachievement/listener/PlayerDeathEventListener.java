package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class PlayerDeathEventListener implements Listener {
    private final MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
    private final UserStatsManager statsManager = plugin.getUserStatsManager();

    public PlayerDeathEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerAnyDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int prefixID = -1;

        int anyDeathCount = statsManager.getStatValue(uuid, PrefixStat.ANY_DEATH_COUNT);
        anyDeathCount++;
        statsManager.setStatValue(uuid, PrefixStat.ANY_DEATH_COUNT, anyDeathCount);

        if (anyDeathCount == 1) {
            // 죽음을 거부하는 자
            prefixID = 3;
        }
        PrefixGranter.grantPrefix(player, prefixID);
    }

    @EventHandler
    public void onPlayerLavaDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int prefixID = -1;

        if (event.getDamageSource().getDamageType().equals(DamageType.LAVA)) {
            int lavaDeathCount = statsManager.getStatValue(uuid, PrefixStat.LAVA_DEATH_COUNT);
            lavaDeathCount++;
            statsManager.setStatValue(uuid, PrefixStat.LAVA_DEATH_COUNT, lavaDeathCount);

            if (lavaDeathCount == 1) {
                // 라바 치킨
                prefixID = 4;

                ItemStack potion = new ItemStack(Material.POTION);
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*60*3, 0), true);
                potionMeta.customName(Component.text("화염 저항의 물약").decoration(TextDecoration.ITALIC, false));
                potion.setItemMeta(potionMeta);
                potion.setAmount(1);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (player.isOnline()) {
                        player.give(List.of(potion));
                    }
                });
            }
        }
        PrefixGranter.grantPrefix(player, prefixID);
    }

    @EventHandler
    public void onPlayerVoidDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int prefixID = -1;

        if (event.getDamageSource().getDamageType().equals(DamageType.OUT_OF_WORLD)) {
            int voidDeathCount = statsManager.getStatValue(uuid, PrefixStat.VOID_DEATH_COUNT);
            voidDeathCount++;
            statsManager.setStatValue(uuid, PrefixStat.VOID_DEATH_COUNT, voidDeathCount);

            if (voidDeathCount == 1) {
                // 나는 카이사가 될거야
                prefixID = 5;
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
                if (headMeta != null) {
                    headMeta.setOwningPlayer(event.getPlayer());
                }
                playerHead.setItemMeta(headMeta);
                playerHead.setAmount(1);

                Bukkit.getScheduler().runTask(plugin, () -> {
                   if (player.isOnline()) {
                       player.give(playerHead);
                   }
                });
            }
        }
        PrefixGranter.grantPrefix(player, prefixID);
    }

}
