package me.secretagent.regionmessages.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class PlayerWandListener implements Listener {

    private final HashMap<Player, HashMap<Integer, Location>> hashMap = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        boolean isLeft = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        boolean isBlock = event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK;
        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemStack.hasItemMeta() && itemMeta.hasEnchant(Enchantment.SILK_TOUCH) && itemStack.getType() == Material.STICK) {
            if (!player.hasPermission("rm.wand")) {
                player.sendMessage(ChatColor.RED + "You cannot use this!");
                player.getInventory().remove(player.getItemInHand());
                return;
            }
            int key = isLeft ? 1 : 2;
            if (isBlock) event.setCancelled(true);
            Location location = isBlock ? event.getClickedBlock().getLocation() : player.getLocation();
            if (!hashMap.containsKey(player)) hashMap.put(player, new HashMap<>());
            hashMap.get(player).put(key, location);
            player.sendMessage(ChatColor.YELLOW + "Set location " + key + "!");
            player.playSound(player.getLocation(), Sound.CLICK, 5, 5);
        }
    }

    public HashMap<Player, HashMap<Integer, Location>> getHashMap() {
        return hashMap;
    }

}
