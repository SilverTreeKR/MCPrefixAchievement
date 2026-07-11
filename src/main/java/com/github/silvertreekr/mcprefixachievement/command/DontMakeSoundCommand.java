package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class DontMakeSoundCommand extends BukkitCommand {
    public DontMakeSoundCommand(@NotNull JavaPlugin plugin) {
        super("발설금지");
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    private final HashMap<UUID, LocalDateTime> lastExecutions = new HashMap<>();
    private final int COOLTIME = 60 * 24;

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (!MCPrefixAchievement.getInstance().getUserPrefixManager().hasPrefix(player.getUniqueId(), PrefixName.HAMMER_ON)) {
            Prefix prefix = MCPrefixAchievement.getInstance().getPrefixConfigManager().getPrefixById(PrefixName.HAMMER_ON);
            if (prefix == null) {
                return false;
            }
            sender.sendRichMessage("<bold>【 칭호 】 <reset><prefix><reset><red>를 가지고 있지 않습니다 !", Placeholder.component("prefix", prefix.getDisplayPrefix()));

            return false;
        }
        if (args.length != 0) {
            sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 명령어입니다.");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>올바른 사용법: /[칭호명]");

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


                player.sendRichMessage("<bold>【 칭호 】 <reset>남은 시간: <bold><red><cooltime>분", Placeholder.unparsed("cooltime", cooltimeText));
                return true;
            }
        }
        lastExecutions.put(player.getUniqueId(), nowTime);

        player.give(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
        player.sendRichMessage("<bold>【 칭호 】 <reset><aqua>불사의 토템<reset>이 지급되었습니다 !");

        return true;
    }
}
