package com.github.silvertreekr.mcprefixachievement.model;

public enum PrefixStat {
    NONE,
    // 접속 관련
    FIRST_JOIN,

    // 블럭 파괴 관련
    BREAK_BLOCK,
    BREAK_DIAMOND_ORE,

    // 블럭 설치 관련
    PLACE_BLOCK,
    PLACE_REDSTONE_TORCH,
    PLACE_TNT,

    // 플레이어 죽음 관련
    ANY_DEATH_COUNT,
    LAVA_DEATH_COUNT,
    VOID_DEATH_COUNT,

    // 아이템 획득 관련
    GET_DRAGON_BREATH,
    GET_DRAGON_EGG,
    GET_COCOA_BEANS,

    // 엔티티 죽음 관련
    KILL_ENDERMAN_BY_MACE,
    KILL_ENDER_DRAGON,
    KILL_WANDERING_TRADER,
    KILL_WARDEN,
    ;
}
