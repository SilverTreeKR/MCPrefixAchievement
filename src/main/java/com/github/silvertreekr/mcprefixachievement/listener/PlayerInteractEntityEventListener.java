package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserStatsManager;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixStat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerInteractEntityEventListener implements Listener {
    private final MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
    private final UserPrefixManager prefixManager = plugin.getUserPrefixManager();
    private final UserStatsManager statsManager = plugin.getUserStatsManager();
    private final PrefixConfigManager prefixConfigManager = plugin.getPrefixConfigManager();

    private void grantPrefix(UUID uuid, int prefixID, Player player) {
        if (prefixID != -1) {
            Prefix prefix = prefixConfigManager.getPrefixById(prefixID);

            if (prefix != null) {
                prefixManager.addPrefix(uuid, prefixID);
                player.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>축하합니다 ! <prefix><reset>을 획득하셨습니다 !", Placeholder.component("prefix", prefix.getDisplayPrefix()));
            }
        }
    }

    public PlayerInteractEntityEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCollectDragonBreath(PlayerInteractEntityEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        int prefixID = -1;
        if (event.getRightClicked() instanceof AreaEffectCloud enetity) {
            ItemStack bottle = event.getPlayer().getInventory().getItem(event.getHand());
            if (bottle.getType().equals(Material.GLASS_BOTTLE)) {

                if (enetity.getParticle().equals(Particle.DRAGON_BREATH)) {
                    int count = statsManager.getStatValue(uuid, PrefixStat.GET_DRAGON_BREATH);
                    count++;
                    statsManager.setStatValue(uuid, PrefixStat.GET_DRAGON_BREATH, count);
                    if (count == 1) {
                        prefixID = 11;
                        ItemStack dragonBreath = new ItemStack(Material.DRAGON_BREATH);
                        ItemMeta itemMeta = dragonBreath.getItemMeta();
                        itemMeta.customName(Component.text("용의 콧물"));
                        dragonBreath.setItemMeta(itemMeta);
                        dragonBreath.setAmount(1);

                        event.getPlayer().give(dragonBreath);
                    }
                }
            }
        }
        grantPrefix(uuid, prefixID, event.getPlayer());
    }
}
