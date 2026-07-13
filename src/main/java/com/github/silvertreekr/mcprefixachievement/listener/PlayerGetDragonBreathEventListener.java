package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.customItems.models.Reward;
import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class PlayerGetDragonBreathEventListener extends AbstractPrefixListener {
    private final int DRAGON_RUNNY_NOSE_THIEF_REQUIRED_VALUE = plugin.getPrefixConfigManager()
            .getPrefixById(PrefixName.DRAGON_RUNNY_NOSE_THIEF)
            .getRequiredStatValue();

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

            // 용의 콧물 도둑
            if (count == DRAGON_RUNNY_NOSE_THIEF_REQUIRED_VALUE) {
                player.give(Reward.DRAGON_RUNNY_NOSE_THIEF_REWARD.create(1));
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

}
