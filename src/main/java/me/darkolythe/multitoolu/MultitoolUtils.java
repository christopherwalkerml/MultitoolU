package me.darkolythe.multitoolu;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MultitoolUtils {

    MultitoolU main;
    MultitoolUtils(MultitoolU plugin) {
        main = plugin;
    }

    public boolean isTool(ItemStack item, String lore) {
        if (item != null) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasLore()) {
                    for (String line : meta.getLore()) {
                        if (line.equals(lore)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
