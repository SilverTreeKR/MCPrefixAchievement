package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class PlayerGetDragonBreathEventListener extends AbstractPrefixListener {
    private static final int DRAGON_RUNNY_NOSE_THIEF_REQUIRED_VALUE = 1;

    public PlayerGetDragonBreathEventListener(MCPrefixAchievement plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        EquipmentSlot hand = event.getHand();
        if (hand == null) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemBefore = player.getInventory().getItem(hand);
        if (itemBefore.getType() != Material.GLASS_BOTTLE) {
            return;
        }

        UUID uuid = player.getUniqueId();
        int dragonBreathBefore = countDragonBreath(player);


        Bukkit.getScheduler().runTask(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            int dragonBreathAfter = countDragonBreath(player);
            if (dragonBreathAfter <= dragonBreathBefore) {
                return;
            }

            int count = increaseStatValue(uuid, PrefixStat.GET_DRAGON_BREATH);

            if (count == DRAGON_RUNNY_NOSE_THIEF_REQUIRED_VALUE) {
                player.give(createDragonBreathReward());
                PrefixGranter.grantPrefix(player, PrefixName.DRAGON_RUNNY_NOSE_THIEF);
            }
        });
    }

    private int countDragonBreath(Player player) {
        int total = 0;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.getType() == Material.DRAGON_BREATH) {
                total += stack.getAmount();
            }
        }
        return total;
    }

    private ItemStack createDragonBreathReward() {
        ItemStack dragonBreath = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta itemMeta = dragonBreath.getItemMeta();
        itemMeta.customName(Component.text("용의 콧물").decoration(TextDecoration.ITALIC, false));
        dragonBreath.setItemMeta(itemMeta);
        dragonBreath.setAmount(1);
        return dragonBreath;
    }
}
