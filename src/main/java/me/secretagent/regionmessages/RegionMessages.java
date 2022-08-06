package me.secretagent.regionmessages;

import co.aikar.commands.BukkitCommandManager;
import me.secretagent.regionmessages.commands.RegionMessagesCommand;
import me.secretagent.regionmessages.database.Database;
import me.secretagent.regionmessages.database.mongo.MongoDB;
import me.secretagent.regionmessages.listeners.PlayerMovementListener;
import me.secretagent.regionmessages.listeners.PlayerWandListener;
import me.secretagent.regionmessages.region.Region;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class RegionMessages extends JavaPlugin {

    private final PlayerWandListener playerWandListener = new PlayerWandListener();
    private final PlayerMovementListener playerMoveListener = new PlayerMovementListener(this);
    private final List<Region> regions = new ArrayList<>();
    private MongoDB mongoDB;

    private String regionEnter;
    private String regionExit;

    @Override
    public void onEnable() {
        mongoDB = new MongoDB(this, getConfig().getString("mongo-url"));
        reload();
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new RegionMessagesCommand(this));
        getServer().getPluginManager().registerEvents(playerWandListener, this);
        getServer().getPluginManager().registerEvents(playerMoveListener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PlayerWandListener getPlayerWandListener() {
        return playerWandListener;
    }

    public Database getPluginDatabase() {
        return mongoDB;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void reload() {
        saveDefaultConfig();
        saveResource("messages.yml", false);
        getPluginDatabase().loadRegions();
        regionEnter = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("messages.yml"))).getString("message-enter-region");
        regionExit = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("messages.yml"))).getString("message-exit-region");
    }

    public String getRegionEnter() {
        return regionEnter;
    }

    public String getRegionExit() {
        return regionExit;
    }

}
