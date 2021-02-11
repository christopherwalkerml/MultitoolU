package me.darkolythe.multitoolu;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MultitoolConfig {

    MultitoolU main;
    MultitoolConfig(MultitoolU plugin) {
        main = plugin;
    }

    public FileConfiguration toolscfg;
    public File tools;

    public void importToolList() {
        Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.HOPPER, MultitoolU.mtoinv);
        if (toolscfg.contains("tools")) {
            Object identifier = toolscfg.get("tools");
            if (identifier instanceof List) {
                List<?> temp = toolscfg.getList("tools");
                int index = 0;
                for (Object o : temp) {
                    if (o == null || ((ItemStack)o).getType() == Material.AIR) {
                        o = main.placeholders.get(index);
                    }
                    index++;
                    inv.addItem((ItemStack) o);
                }
            }
        }
        MultitoolU.tools = inv;
    }

    public void saveToolList() {
        toolscfg.set("tools", null);
        toolscfg.set("tools", MultitoolU.tools.getContents());

        try {
            toolscfg.save(tools);
        } catch (IOException e) {
            System.out.println("----------- CRITICAL -----------");
            System.out.println("Could not save tools");
        }
    }

    public void setUp() {
        tools = new File(main.getDataFolder(), "tools.yml");

        if (!tools.exists()) {
            try {
                tools.createNewFile();
                System.out.println(MultitoolU.prefix + ChatColor.GREEN + "tools.yml has been created");
            } catch (IOException e) {
                System.out.println(MultitoolU.prefix + ChatColor.RED + "Could not create tools.yml");
            }
        }
        toolscfg = YamlConfiguration.loadConfiguration(tools);
    }
}
