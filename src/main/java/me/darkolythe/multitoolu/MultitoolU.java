package me.darkolythe.multitoolu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultitoolU extends JavaPlugin implements Listener {

    public static MultitoolU plugin;
    public MultitoolInventory multitoolInventory;
    public static MultitoolUtils multitoolUtils;
    public MultitoolToolDetect multitoolToolDetect;
    public MultitoolConfig multitoolConfig;

    public List<ItemStack> placeholders = new ArrayList<>();
    public static String prefix = "[placeholder prefix]";
    public static String lore = "[placeholder lore]";
    public static String mtoinv = "[placeholder lore]";
    public static Map<String, String> messages = new HashMap<>();
    public static Inventory tools = null;

    public void onEnable() {
        plugin = this;

        getMessages();

        multitoolInventory = new MultitoolInventory(this);
        multitoolUtils = new MultitoolUtils(this);
        multitoolToolDetect = new MultitoolToolDetect(this);
        multitoolConfig = new MultitoolConfig(this);

        multitoolInventory.addPlaceholders();
        multitoolConfig.setUp();
        multitoolConfig.importToolList();

        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(multitoolInventory, this);
        Bukkit.getPluginManager().registerEvents(multitoolToolDetect, this);
        getCommand("multitoolu").setExecutor(new MultitoolCommand());

        System.out.println(prefix + ChatColor.GREEN + "MultitoolU enabled!");
    }

    public void onDisable() {
        multitoolConfig.saveToolList();

        System.out.println(prefix + ChatColor.RED + "MultitoolU disabled!");
    }

    private void getMessages() {
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
        lore = ChatColor.translateAlternateColorCodes('&', getConfig().getString("lore"));
        mtoinv = ChatColor.translateAlternateColorCodes('&', getConfig().getString("mtoinv"));

        MultitoolU.messages.put("msgempty", ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("msgempty").replace("%prefix%", MultitoolU.prefix)));
        MultitoolU.messages.put("msggiven", ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("msggiven").replace("%prefix%", MultitoolU.prefix)));
        MultitoolU.messages.put("msgnospace", ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("msgnospace").replace("%prefix%", MultitoolU.prefix)));
        MultitoolU.messages.put("msgnopermission", ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("msgnopermission").replace("%prefix%", MultitoolU.prefix)));
    }

    public static MultitoolU getInstance() {
        return plugin;
    }
}
