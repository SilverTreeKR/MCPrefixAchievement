package com.github.silvertreekr.mcprefixachievement.listener;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerQuitEventListener implements Listener {

    public PlayerQuitEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();

        // save database & unload from Cache (PlayerPrefixData)
        plugin.getUserPrefixManager().savePlayerPrefixData(uuid);
        plugin.getUserPrefixManager().unloadPlayerPrefixData(uuid);

        // save database & unload from Cache (PlayerStatsData)
        plugin.getUserStatsManager().savePlayerStatsData(uuid);
        plugin.getUserStatsManager().unloadPlayerStatsData(uuid);
    }
}
