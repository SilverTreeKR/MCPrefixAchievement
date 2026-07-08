package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import com.github.silvertreekr.mcprefixachievement.util.PrefixGranter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SortedMap;

public class PrefixCommand extends BukkitCommand {
    public PrefixCommand(@NotNull JavaPlugin plugin) {
        super("칭호");
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        // /칭호 정보 [칭호ID] -> 특정 칭호의 달성 조건을 보는 커맨드
        // /칭호 목록 [페이지] -> 칭호들의 목록을 보는 커맨드 (15개씩 끊어서 페이지)
        // /칭호 지급 [대상 플레이어] [칭호ID] -> 대상 플레이어에게 칭호를 지급하는 커맨드
        // /칭호 -> 칭호 명령어들 반환
        if (args.length == 0) {
            sender.sendRichMessage("<bold>【 칭호 】 <reset>사용 가능한 명령어:");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>/칭호 정보 [칭호ID] - 특정 칭호의 정보를 확인합니다.");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>/칭호 목록 [페이지] - 칭호들의 목록을 확인합니다.");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>/[칭호명] - 특정 칭호의 특수 효과를 부여받습니다.");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>(공백 없이 입력)");
            if (!sender.isOp()) {
                return true;
            }
            sender.sendRichMessage("<bold>【 칭호 】 ---------------------------------------");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>/칭호 지급 [대상 플레이어] [칭호ID] ");
            sender.sendRichMessage("<bold>【 칭호 】 <reset>- 대상 플레이어에게 칭호를 지급합니다.");
            return true;
        }

        PrefixConfigManager prefixConfigManager = MCPrefixAchievement.getInstance().getPrefixConfigManager();
        switch (args[0]) {
            case "정보" -> {
                if (args.length == 1) {
                    sender.sendRichMessage("<bold>【 칭호 】 <reset>사용법: /칭호 정보 [칭호 ID]");
                    if (!(sender instanceof Player player)) {
                        return false;
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    return false;
                }
                if (args.length == 2) {
                    try {
                        PrefixName prefixName = PrefixName.getPrefixByIndex(Integer.parseInt(args[1]));
                        Prefix prefix = prefixConfigManager.getPrefixById(prefixName);

                        if (prefix == null) {
                            sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 칭호 ID입니다.");
                            return false;
                        }
                        sender.sendRichMessage("<bold>【 칭호 】 <reset>칭호: <prefix>", Placeholder.component("prefix", prefix.getDisplayPrefix()));
                        sender.sendRichMessage("<bold>【 칭호 】 <reset>달성 조건: <description>", Placeholder.component("description", prefix.getDescription()));
                        sender.sendRichMessage("<bold>【 칭호 】 <reset>달성 보상: <reward>", Placeholder.component("reward", prefix.getReward()));
                        if (!(sender instanceof Player player)) {
                            return true;
                        }
                        sender.sendRichMessage("<bold>【 칭호 】 <reset>");

                        if (MCPrefixAchievement.getInstance().getUserPrefixManager().hasPrefix(player.getUniqueId(), prefixName)) {
                            sender.sendRichMessage("<bold>【 칭호 】 <reset>현재 이 칭호를 <bold><green>보유<reset>하고 있습니다.");
                        } else {
                            sender.sendRichMessage("<bold>【 칭호 】 <reset>현재 이 칭호를 <bold><red>미보유<reset>하고 있습니다.");
                        }
                    } catch (NumberFormatException e) {
                        sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 칭호 ID입니다.");
                        if (!(sender instanceof  Player player)) {
                            return false;
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        return false;
                    }
                }
            }
            case "목록" -> {
                SortedMap<Integer, Prefix> prefixMap = prefixConfigManager.getPrefixMap();
                if (args.length == 1) {
                    int index = 1;
                    for (Map.Entry<Integer, Prefix> entry : prefixMap.entrySet()) {
                        if (index > 10) break;
                        sender.sendRichMessage("<bold>【 칭호 】 <reset>%d. <prefix>".formatted(entry.getKey()), Placeholder.component("prefix", entry.getValue().getDisplayPrefix()));
                        index++;
                    }
                    return true;
                }
                if (args.length == 2) {
                    try {
                        int page = Integer.parseInt(args[1]);
                        int pageSize = 10;
                        int skip = (page - 1) * pageSize;
                        if (skip >= prefixMap.size()) {
                            sender.sendRichMessage("<bold>【 칭호 】 <reset><red>해당 페이지에는 칭호가 존재하지 않습니다.");
                            if (!(sender instanceof Player player)) {
                                return false;
                            }
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                            return false;
                        }
                        int shown = 0;
                        for (Map.Entry<Integer, Prefix> entry : prefixMap.entrySet()) {
                            if (shown >= skip) {
                                sender.sendRichMessage("<bold>【 칭호 】 <reset>%d. <prefix>".formatted(entry.getKey()), Placeholder.component("prefix", entry.getValue().getDisplayPrefix()));
                            }
                            shown++;
                            if (shown >= skip + pageSize) break;
                        }
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 페이지 번호입니다.");
                        if (!(sender instanceof Player player)) {
                            return false;
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        return false;
                    }
                }
            }
            case "지급" -> {
                if (args.length < 3) {
                    sender.sendRichMessage("<bold>【 칭호 】 <reset>사용법: /칭호 지급 [대상 플레이어] [칭호ID]");
                    if (!(sender instanceof Player player)) {
                        return false;
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendRichMessage("<bold>【 칭호 】 <reset><red>온라인 상태인 플레이어가 아닙니다.");
                    if (!(sender instanceof Player player)) {
                        return false;
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    return false;
                }
                try {
                    PrefixName prefixName = PrefixName.getPrefixByIndex(Integer.parseInt(args[2]));
                    Prefix prefix = prefixConfigManager.getPrefixById(prefixName);

                    if (prefix == null) {
                        sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 칭호 ID입니다.");
                        return false;
                    }

                    PrefixGranter.grantPrefix(target, prefixName);
                    sender.sendRichMessage(
                            "<bold>【 칭호 】 <reset><target>님께 <prefix> 칭호를 지급했습니다.",
                            Placeholder.component("target", Component.text(target.getName())),
                            Placeholder.component("prefix", prefix.getDisplayPrefix())
                    );
                    if (!(sender instanceof Player player)) {
                        return true;
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    return true;

                } catch (NumberFormatException e) {
                    sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 칭호 ID입니다.");
                    if (!(sender instanceof Player player)) {
                        return false;
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    return false;
                }
            }
            default -> {
                sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바르지 않은 사용법입니다.");
                sender.sendRichMessage("<bold>【 칭호 】 <reset><red>올바른 사용법: /칭호 정보 [칭호명] 또는 /칭호 목록 [페이지]");
                if (!(sender instanceof Player player)) {
                    return false;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                return false;
            }
        }
        return false;
    }
}
