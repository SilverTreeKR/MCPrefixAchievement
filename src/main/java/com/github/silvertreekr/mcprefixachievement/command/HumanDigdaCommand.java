package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class HumanDigdaCommand extends BukkitCommand {
    public HumanDigdaCommand(@NotNull JavaPlugin plugin) {
        super("인간디그다");
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    private final HashMap<UUID, LocalDateTime> lastExecutions = new HashMap<>();
    private final int COOLTIME = 60 * 12;

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (!MCPrefixAchievement.getInstance().getUserPrefixManager().hasPrefix(player.getUniqueId(), PrefixName.HUMAN_DIGDA)) {
            Prefix prefix = MCPrefixAchievement.getInstance().getPrefixConfigManager().getPrefixById(PrefixName.HUMAN_DIGDA);
            if (prefix == null) {
                return false;
            }
            sender.sendRichMessage("<bold>【 칭호 】 <reset><prefix><reset><red>를 가지고 있지 않습니다 !", Placeholder.component("prefix", prefix.getDisplayPrefix()));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);

            return false;
        }
        if (args.length != 0) {
            sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 명령어입니다.");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>올바른 사용법: /[칭호명]");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);

            return false;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        if (lastExecutions.containsKey(player.getUniqueId())) {
            LocalDateTime lastExecution = lastExecutions.get(player.getUniqueId());
            Duration duration = Duration.between(lastExecution, nowTime);
            long minutesPassed = duration.toMinutes();

            if (minutesPassed < COOLTIME) {
                long minutesLeft = COOLTIME - minutesPassed;
                long hoursLeft = minutesLeft / 60;
                long remainMinutes = minutesLeft % 60;

                String cooltimeText;
                if (hoursLeft > 0) {
                    cooltimeText = hoursLeft + "시간" + remainMinutes + "분";
                } else {
                    cooltimeText = remainMinutes + "분";
                }

                player.sendRichMessage("<bold>【 칭호 】 <reset>남은 시간: <bold><red><cooltime>", Placeholder.unparsed("cooltime", cooltimeText));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);

                return true;
            }
        }
        lastExecutions.put(player.getUniqueId(), nowTime);

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*60*60*2, 0));
        player.sendRichMessage("<bold>【 칭호 】 <reset><aqua>야간투시 효과<reset>가 적용되었습니다 !");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        return true;
    }
}
