package com.github.silvertreekr.mcprefixachievement.command;

import com.github.silvertreekr.mcprefixachievement.MCPrefixAchievement;
import com.github.silvertreekr.mcprefixachievement.model.Prefix;
import com.github.silvertreekr.mcprefixachievement.model.PrefixName;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class BlockMasterCommand extends BukkitCommand {
    public BlockMasterCommand(@NotNull JavaPlugin plugin) {
        super("블록마스터");
        plugin.getServer().getCommandMap().register("mcprefixachievement", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (!MCPrefixAchievement.getInstance().getUserPrefixManager().hasPrefix(player.getUniqueId(), PrefixName.BLOCK_MASTER)) {
            Prefix prefix = MCPrefixAchievement.getInstance().getPrefixConfigManager().getPrefixById(PrefixName.BLOCK_MASTER);
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

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 0));
        player.sendRichMessage("<bold>【 칭호 】 <reset><aqua>신속 효과<reset>가 적용되었습니다 !");

        return true;
    }
}
