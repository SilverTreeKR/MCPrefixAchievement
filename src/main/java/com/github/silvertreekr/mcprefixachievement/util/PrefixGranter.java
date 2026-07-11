package com.github.silvertreekr.mcprefixachievement.util;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;
import com.github.silvertreekr.mcprefixachievement.dao.UserPrefixManager;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrefixGranter {
    private PrefixGranter() {

    }
    public static void grantPrefix(Player player, PrefixName prefixName) {
        MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
        PrefixConfigManager prefixConfigManager = plugin.getPrefixConfigManager();
        UserPrefixManager prefixManager = plugin.getUserPrefixManager();
        UUID uuid = player.getUniqueId();
        Prefix prefix = prefixConfigManager.getPrefixById(prefixName);

        if (prefix == null) {
            plugin.getSLF4JLogger().warn("Failed to grant prefix {}: prefix not found in config (still not loaded?)", prefixName);
            return;
        }
        if (prefixManager.hasPrefix(uuid, prefixName)) {
            return;
        }
        if (prefixName == PrefixName.NONE) {
            return;
        }
        Component hoverText = Component.text("")
                .append(prefix.getDisplayPrefix())
                .appendNewline()
                .append(Component.text("달성 조건: "))
                .append(prefix.getDescription())
                .appendNewline()
                .append(Component.text("달성 보상: "))
                .append(prefix.getReward());

        Component prefixWithHover = prefix.getDisplayPrefix().hoverEvent(HoverEvent.showText(hoverText));

        prefixManager.addPrefix(uuid, prefixName);
        player.sendRichMessage("<bold>【 칭호 】 <reset>축하합니다 ! <prefix><reset>을 획득하셨습니다 !", Placeholder.component("prefix", prefixWithHover));
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }

    public static void broadcastPrefix(Player player, PrefixName prefixName) {
        MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
        PrefixConfigManager prefixConfigManager = plugin.getPrefixConfigManager();
        Prefix prefix = prefixConfigManager.getPrefixById(prefixName);

        if (prefix == null) {
            return;
        }

        if (prefixName == PrefixName.NONE) {
            return;
        }

        Component hoverText = Component.text("")
                .append(prefix.getDisplayPrefix())
                .appendNewline()
                .append(Component.text("달성 조건: "))
                .append(prefix.getDescription())
                .appendNewline()
                .append(Component.text("달성 보상: "))
                .append(prefix.getReward());

        Component prefixWithHover = prefix.getDisplayPrefix().hoverEvent(HoverEvent.showText(hoverText));

        Component playerName = Component.text(player.getName());
        Component message = MiniMessage.miniMessage().deserialize(
                "<bold>【 칭호 】 <reset><green><player><reset>님께서 <prefix><reset>을 획득하셨습니다 !"
                ,Placeholder.component("player", playerName)
                ,Placeholder.component("prefix", prefixWithHover)
        );
        Bukkit.broadcast(message);
    }
}