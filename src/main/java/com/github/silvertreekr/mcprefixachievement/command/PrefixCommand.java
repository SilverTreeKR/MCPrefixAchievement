package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.config.PrefixConfigManager;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SortedMap;

public class PrefixCommand extends BukkitCommand {
    private static final int PAGE_SIZE = 10;
    private final MCPrefixAchievement plugin;

    public PrefixCommand(@NotNull MCPrefixAchievement plugin) {
        super("칭호");
        this.plugin = plugin;
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        // /칭호 정보 [칭호 ID] -> 특정 칭호의 달성 조건을 보는 커맨드
        // /칭호 목록 [페이지] -> 칭호들의 목록을 보는 커맨드
        // /칭호 -> 칭호 명령어들 반환
        if (args.length == 0) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>사용 가능한 명령어:");
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>/칭호 정보 [칭호 ID] - 특정 칭호의 정보를 확인합니다.");
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>/칭호 목록 [페이지] - 칭호들의 목록을 확인합니다.");
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>/[칭호명] - 특정 칭호의 특수 효과를 부여받습니다. (공백 없이 입력)");
            return true;
        }

        PrefixConfigManager prefixConfigManager = plugin.getPrefixConfigManager();
        switch (args[0]) {
            case "정보" -> showPrefixInfo(sender, args, prefixConfigManager);
            case "목록" -> showPrefixList(sender, args, prefixConfigManager);
            default -> {
                sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 사용법입니다.");
                sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바른 사용법: /칭호 정보 [칭호 ID] 또는 /칭호 목록 [페이지]");
            }
        }
        return true;
    }

    private void showPrefixInfo(CommandSender sender, String[] args, PrefixConfigManager prefixConfigManager) {
        if (args.length != 2) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>사용법: /칭호 정보 [칭호 ID]");
            return;
        }

        Integer prefixId = parsePositiveInt(args[1]);
        if (prefixId == null) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 칭호 ID입니다.");
            return;
        }

        Prefix prefix = prefixConfigManager.getPrefixById(prefixId);
        if (prefix == null) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 칭호 ID입니다.");
            return;
        }

        sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>칭호: <prefix>", Placeholder.component("prefix", prefix.getDisplayPrefix()));
        sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: <description>", Placeholder.component("description", prefix.getDescription()));
        sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: <reward>", Placeholder.component("reward", prefix.getReward()));

        if (sender instanceof Player player) {
            boolean hasPrefix = plugin.getUserPrefixManager().hasPrefix(player.getUniqueId(), prefixId);
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>");
            sender.sendRichMessage(hasPrefix
                    ? "<bold>[ 칭호 시스템 ] <reset>현재 이 칭호를 <bold><green>보유<reset>하고 있습니다."
                    : "<bold>[ 칭호 시스템 ] <reset>현재 이 칭호를 <bold><red>미보유<reset>하고 있습니다.");
        }
    }

    private void showPrefixList(CommandSender sender, String[] args, PrefixConfigManager prefixConfigManager) {
        if (args.length > 2) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>사용법: /칭호 목록 [페이지]");
            return;
        }

        int page = 1;
        if (args.length == 2) {
            Integer parsedPage = parsePositiveInt(args[1]);
            if (parsedPage == null) {
                sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 페이지 번호입니다.");
                return;
            }
            page = parsedPage;
        }

        SortedMap<Integer, Prefix> prefixMap = prefixConfigManager.getPrefixMap();
        int skip = (page - 1) * PAGE_SIZE;
        if (skip >= prefixMap.size()) {
            sender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>해당 페이지에는 칭호가 존재하지 않습니다.");
            return;
        }

        prefixMap.entrySet().stream()
                .skip(skip)
                .limit(PAGE_SIZE)
                .forEach(entry -> sendPrefixListItem(sender, entry));
    }

    private void sendPrefixListItem(CommandSender sender, Map.Entry<Integer, Prefix> entry) {
        sender.sendRichMessage(
                "<bold>[ 칭호 시스템 ] <reset>%d. <prefix>".formatted(entry.getKey()),
                Placeholder.component("prefix", entry.getValue().getDisplayPrefix())
        );
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
