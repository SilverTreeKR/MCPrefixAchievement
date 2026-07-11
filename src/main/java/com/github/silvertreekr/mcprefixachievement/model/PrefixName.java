package com.github.silvertreekr.mcprefixachievement.model;

import java.util.HashMap;
import java.util.Map;

public enum PrefixName {
    NONE(-1),
    FIRST_STEP(1),
    JEWELLERY_COLLECTOR(2),
    DEATH_REJECTOR(3),
    LAVA_CHICKEN(4),
    WANT_TO_BE_KAISA(5),
    DRAGON_SLAYER(6),
    HAMMER_ON(7),
    BUILDER(8),
    PRO_WORKER(9),
    I_AM_GAUDI(10),
    DRAGON_RUNNY_NOSE_THIEF(11),
    HOME_WRECKER(12),
    PATRON(13),
    KOPI_LUWAK(14),
    MATCH_GIRL(15),
    THE_THIEF(16),
    BOMBER_MAN(17),
    DONT_MAKE_SOUND(18),
    LANDLORD(19),
    BLOCK_MASTER(20),
    ;

    private final int index;

    PrefixName(int index) {
        this.index = index;
    }

    public int getPrefixIndex() {
        return index;
    }

    private static final Map<Integer, PrefixName> INDEX_MAP = new HashMap<>();

    static {
        for (PrefixName prefix : values()) {
            INDEX_MAP.put(prefix.index, prefix);
        }
    }

    public static PrefixName getPrefixByIndex(int index) {
        return INDEX_MAP.getOrDefault(index, NONE);
    }

}
