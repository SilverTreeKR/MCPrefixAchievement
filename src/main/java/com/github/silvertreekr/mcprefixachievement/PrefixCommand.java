package com.github.silvertreekr.mcprefixachievement;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class PrefixCommand extends BukkitCommand {
    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        // /칭호 정보 [칭호명] -> 특정 칭호의 달성 조건을 보는 커맨드
        // /칭호 목록 [페이지] -> 칭호들의 목록을 보는 커맨드 (15개씩 끊어서 페이지)
        // /칭호 -> 칭호 명령어들 반환
        if (args.length == 0) {
            commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>사용 가능한 명령어:");
            commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>/칭호 정보 [칭호명] - 특정 칭호의 정보를 확인합니다.");
            commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>/칭호 목록 [페이지] - 칭호들의 목록을 확인합니다.");

            return true;
        }

        if (args[0].equals("정보")) {
            if (args.length == 1) {
                commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>사용법: /칭호 정보 [칭호명]");
                return true;
            }
            if (args.length ==2) {
                try {
                    int prefixId = Integer.parseInt(args[1]);
                    Set<Component> prefixes = MCPrefixAchievement.getPrefixConfigManager().getAllDisplayPrefixes();

                    if (prefixId > prefixes.size()) {
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 칭호 ID입니다.");
                        return false;
                    }
                    Component prefix = new ArrayList<>(prefixes).get(prefixId - 1);
                    commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>칭호: <prefix>", Placeholder.component("prefix", prefix));
                    if (prefixId == 1) {
                        // 첫걸음
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 서버에 첫 접속하기");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 빵 16개, 횃불 32개");
                    } else if (prefixId == 2) {
                        // 보석 수집가
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 첫 다이아몬드 채굴하기");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 성급함 효과 5분");
                    } else if (prefixId == 3) {
                        // 죽음을 거부하는 자
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 서버에서의 첫 죽음");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 없음");
                    } else if (prefixId == 4) {
                        // 라바 치킨
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 처음으로 용암에 빠져서 죽기");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 화염 저항 포션 1개(지속 시간 3분)");
                    } else if (prefixId == 5) {
                        // 나는 카이사가 될거야
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 처음으로 세계의 밖에 떨어져 죽기");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 본인의 머리(플레이어 머리)");
                    } else if (prefixId == 6) {
                        // 용살자
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 첫 엔더드레곤 처치");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 없음");
                    } else if (prefixId == 7) {
                        // 망치 나가신다
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 철퇴로 엔더맨 처치");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 점프 강화 명령어(쿨타임 1시간, 유지시간 3분)");
                    } else if (prefixId == 8) {
                        // 건축가
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 블럭 5000개 설치");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 비계 64개");
                    } else if (prefixId == 9) {
                        // 전문 노가다꾼
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 블럭 10000개 파괴");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 다이아몬드 삽(내구성 3)");
                    } else if (prefixId == 10) {
                        // 내가 바로 가우디
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 블럭 50000개 설치");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 스펀지 5개");
                    } else if (prefixId == 11) {
                        // 용의 콧물 도둑
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 첫 용의 숨결 획득");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 용의 콧물");
                    } else if (prefixId == 12) {
                        // 가정 파괴범
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 조건: 첫 드래곤 알 획득");
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>달성 보상: 마법이 부여된 횡금사과 1개");
                    }
                } catch (Exception e) {
                    commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 칭호 ID입니다.");
                    return false;
                }
            }
        } else if (args[0].equals("목록")) {
            Set<Component> prefixes = MCPrefixAchievement.getPrefixConfigManager().getAllDisplayPrefixes();
            if (args.length == 1) {
                for (int i = 0; i < Math.min(15, prefixes.size()); i++) {
                    Component prefix = new ArrayList<>(prefixes).get(i);
                    commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>" + (i + 1) + ". <prefix>", Placeholder.component("prefix", prefix));
                }
                return true;
            }
            if (args.length == 2) {
                try {
                    int page = Integer.parseInt(args[1]);
                    Set<Component> collect = new ArrayList<>(prefixes)
                            .stream()
                            .skip((page - 1) * 15)
                            .limit(15)
                            .collect(Collectors.toSet());
                    if (collect.isEmpty()) {
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>해당 페이지에는 칭호가 존재하지 않습니다.");
                        return false;
                    }
                    int index = 1;
                    for (Component prefix : collect) {
                        commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset>" + index +". <prefix>", Placeholder.component("prefix", prefix));
                        index++;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 페이지 번호입니다.");
                    return false;
                }
            }
        } else {
            commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바르지 않은 사용법입니다.");
            commandSender.sendRichMessage("<bold>[ 칭호 시스템 ] <reset><red>올바른 사용법: /칭호 정보 [칭호명] 또는 /칭호 목록 [페이지]");
            return false;
        }
        return false;
    }

    public PrefixCommand(JavaPlugin plugin) {
        super("칭호");
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }
}
