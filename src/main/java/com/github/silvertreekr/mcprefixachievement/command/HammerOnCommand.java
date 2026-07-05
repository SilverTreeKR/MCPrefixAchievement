package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.PrefixIds;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HammerOnCommand extends BukkitCommand {
    private static final Duration COOLDOWN = Duration.ofHours(1);
    private static final int JUMP_BOOST_TICKS = 20 * 60 * 3;

    private final MCPrefixAchievement plugin;
    private final Map<UUID, LocalDateTime> lastExecutions = new HashMap<>();

    public HammerOnCommand(@NotNull MCPrefixAchievement plugin) {
        super("망치나가신다");
        this.plugin = plugin;
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>플레이어만 사용할 수 있습니다.");
            return true;
        }
        if (!plugin.getUserPrefixManager().hasPrefix(player.getUniqueId(), PrefixIds.HAMMER_ON)) {
            Prefix prefix = plugin.getPrefixConfigManager().getPrefixById(PrefixIds.HAMMER_ON);
            if (prefix == null) {
                return true;
            }
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><prefix><reset><red>를 가지고 있지 않습니다 !", Placeholder.component("prefix", prefix.getDisplayPrefix()));

            return true;
        }
        if (args.length != 0) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 명령어입니다.");
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>올바른 사용법: /[칭호명]");

            return true;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        if (lastExecutions.containsKey(player.getUniqueId())) {
            LocalDateTime lastExecution = lastExecutions.get(player.getUniqueId());
            Duration duration = Duration.between(lastExecution, nowTime);

            if (duration.compareTo(COOLDOWN) < 0) {
                long minutesLeft = COOLDOWN.minus(duration).toMinutes() + 1;

                player.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>남은 시간: <bold><red><cooltime>분", Placeholder.unparsed("cooltime", String.valueOf(minutesLeft)));
                return true;
            }
        }
        lastExecutions.put(player.getUniqueId(), nowTime);

        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, JUMP_BOOST_TICKS, 0));
        player.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><aqua>점프 강화 효과<reset>가 적용되었습니다 !");

        return true;
    }
}
