package com.github.silvertreekr.mcprefixachievement.dto;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PrefixDataTranferObject {
    private final Component displayPrefix;
    private final PrefixStat requiredStat;
    private final int requiredStatValue;

    private PrefixDataTranferObject(@NotNull Component displayPrefix, @NotNull PrefixStat requiredStat, int requiredStatValue) {
        this.displayPrefix = displayPrefix;
        this.requiredStat = requiredStat;
        this.requiredStatValue = requiredStatValue;
    }

    public static @NotNull PrefixDataTranferObject fromBukkitConfig(@NotNull ConfigurationSection config) throws IllegalArgumentException {

        String displayPrefix = config.getString("displayPrefix");
        if (displayPrefix == null) {
            throw new IllegalArgumentException("displayPrefix is null");
        }
        Component deserializedDisplayPrefix = MiniMessage.miniMessage().deserialize(displayPrefix);


        String requiredStatString = config.getString("requiredStat");
        if (requiredStatString == null) {
            throw new IllegalArgumentException("requiredStat is null");
        }
        PrefixStat requiredStat;
        try {
            requiredStat = PrefixStat.valueOf(requiredStatString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid requiredStat: " + requiredStatString);
        }

        int requiredStatValue = config.getInt("requiredStatValue", -1);
        if (requiredStatValue < 0) {
            throw new IllegalArgumentException("Invalid requiredStatValue: " + requiredStatValue);
        }

        return new PrefixDataTranferObject(deserializedDisplayPrefix, requiredStat, requiredStatValue);
    }

    public @NotNull Component getDisplayPrefix() {
        return displayPrefix;
    }

    public @NotNull PrefixStat getRequiredStat() {
        return requiredStat;
    }

    public int getRequiredStatValue() {
        return requiredStatValue;
    }
}
