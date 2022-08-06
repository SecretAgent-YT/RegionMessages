package me.secretagent.regionmessages.listeners;

import me.secretagent.regionmessages.RegionMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class PlayerMovementListener implements Listener {

    private final RegionMessages plugin;

    private final HashMap<Player, Boolean> hashMap = new HashMap<>();

    public PlayerMovementListener(RegionMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPluginDatabase().regionExists(player.getLocation())) {
            hashMap.put(player, true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getRegionEnter()));
        } else if (!plugin.getPluginDatabase().regionExists(player.getLocation())) {
            hashMap.put(player, false);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        onUpdate(event.getPlayer());
    }

    private void onUpdate(Player player) {
        if (plugin.getPluginDatabase().regionExists(player.getLocation()) && !hashMap.get(player)) {
            hashMap.put(player, true);
            player.sendMessage("Welcome to the region!");
        } else if (!plugin.getPluginDatabase().regionExists(player.getLocation()) && hashMap.get(player)) {
            hashMap.put(player, false);
            player.sendMessage("Come back to the region!");
        }
    }

}
