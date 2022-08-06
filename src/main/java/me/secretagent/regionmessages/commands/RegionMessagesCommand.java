package me.secretagent.regionmessages.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.secretagent.regionmessages.RegionMessages;
import me.secretagent.regionmessages.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CommandAlias("rm")
public class RegionMessagesCommand extends BaseCommand {

    private final RegionMessages plugin;

    public RegionMessagesCommand(RegionMessages plugin) {
        this.plugin = plugin;
    }

    @Subcommand("wand")
    @CommandPermission("rm.wand")
    public void onWand(Player player) {
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "RegionMessages Wand!");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
        wand.setItemMeta(meta);
        player.getInventory().addItem(wand);
        player.playSound(player.getLocation(), Sound.EAT, 5, 5);
        player.sendMessage(ChatColor.YELLOW + "Added the RegionMessages Wand to your inventory!");
    }

    @Subcommand("create")
    @CommandPermission("rm.create")
    public void onCreate(Player player, String regionName) {
        HashMap<Player, HashMap<Integer, Location>> hashMap = plugin.getPlayerWandListener().getHashMap();
        if (hashMap.containsKey(player) && hashMap.get(player).keySet().size() == 2) {
            Location location1 = hashMap.get(player).get(1);
            Location location2 = hashMap.get(player).get(2);
            if (regionName != null && !plugin.getPluginDatabase().regionExists(regionName)) {
                Region region = new Region(regionName, location1, location2);
                plugin.getPluginDatabase().saveRegion(region);
                player.sendMessage(ChatColor.YELLOW + "Created region " + regionName + "!");
            } else {
                player.sendMessage(ChatColor.RED + "Please specify a valid region!");
            }
        } else if (!hashMap.containsKey(player) || hashMap.get(player).keySet().size() != 2) {
            player.sendMessage(ChatColor.RED + "You haven't set your locations yet!");
        }
    }

    @Subcommand("delete")
    @CommandPermission("rm.delete")
    public void onDelete(Player player, String string) {
        if (string != null && plugin.getPluginDatabase().regionExists(string)) {
            plugin.getPluginDatabase().deleteRegion(string);
            player.sendMessage(ChatColor.YELLOW + "Deleted region " + string + "!");
        } else {
            player.sendMessage(ChatColor.RED + "Please specify a valid region!");
        }
    }

    @Subcommand("list")
    @CommandPermission("rm.list")
    public void onList(Player player) {
        List<Region> regions = plugin.getRegions();
        StringBuilder stringBuilder = new StringBuilder(ChatColor.YELLOW + "REGIONS\n");
        regions.forEach(region -> stringBuilder.append(ChatColor.YELLOW + "-  ").append(region.getName()).append("\n"));
        player.sendMessage(stringBuilder.toString());
    }

    @Subcommand("reload")
    @CommandPermission("rm.reload")
    public void onReload(Player player) {
        plugin.reload();
        player.sendMessage(ChatColor.YELLOW + "Reloaded plugin!");
    }

}
