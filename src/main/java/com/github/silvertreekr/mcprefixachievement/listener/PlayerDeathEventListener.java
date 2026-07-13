package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.customItems.models.Reward;
import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

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
            giveIfOnlineNextTick(player, Reward.LAVA_CHICKEN_REWARD.create(1));
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
            giveIfOnlineNextTick(player, Reward.WANT_TO_BE_KAISA_REWARD.create(1, player));
            PrefixGranter.grantPrefix(player, PrefixName.WANT_TO_BE_KAISA);
        }
    }

    private void giveIfOnlineNextTick(Player player, ItemStack itemStack) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.isOnline()) {
                player.give(List.of(itemStack));
            }
        });
    }
}
