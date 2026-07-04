    public PlayerJoinEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MCPrefixAchievement plugin = MCPrefixAchievement.getInstance();
        UserPrefixManager prefixManager = plugin.getUserPrefixManager();
        UserStatsManager statsManager = plugin.getUserStatsManager();
        PrefixConfigManager prefixConfigManager = plugin.getPrefixConfigManager();


        // load PlayerPrefixData & PlayerStatsData to each Cache
        prefixManager.loadPlayerPrefixData(uuid);
        statsManager.loadPlayerStatsData(uuid);
}
