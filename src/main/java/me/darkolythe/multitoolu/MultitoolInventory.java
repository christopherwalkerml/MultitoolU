package me.darkolythe.multitoolu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultitoolInventory implements Listener {

    MultitoolU main;
    MultitoolInventory(MultitoolU plugin) {
        main = plugin;
    }

    public Inventory getInventory() {
        if (MultitoolU.tools == null) {
            Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.HOPPER, MultitoolU.mtoinv); //create the mv inv
            for (int index = 0; index < 5; index++) {
                inv.setItem(index, main.placeholders.get(index)); //if the player data is empty, set main.placeholders until the inv is saved
            }
            MultitoolU.tools = inv;
        }
        return MultitoolU.tools;
    }

    public void addPlaceholders() {
        String[] names = new String[]{ChatColor.GREEN + "Put Sword Here", ChatColor.GREEN + "Put Pickaxe Here", ChatColor.GREEN + "Put Axe Here",
                ChatColor.GREEN + "Put Shovel Here", ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Multitool"};
        String lore = ChatColor.AQUA + "Click this feather to generate your Multitool.";

        for (int i = 0; i < 5; i++) {
            ItemStack ph = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1); //create gray stained glass
            ItemMeta phmet = ph.getItemMeta();
            phmet.addItemFlags(ItemFlag.HIDE_PLACED_ON); //make them hideplaceon so that players cant replicate them in their inventory
            phmet.setDisplayName(names[i]); //give them their display names
            if (i == 4) {
                ph.setType(Material.FEATHER); //if the item is a feather, give it lores
                phmet.setLore(addLore(phmet, lore, true));
            }
            ph.setItemMeta(phmet);
            main.placeholders.add(ph); //add all the items to a list with place holder glass panes
        }
    }

    public static List<String> addLore(ItemMeta meta, String line, boolean top) {
        List<String> newlore = new ArrayList<>();
        if (!top) {
            if (meta.hasLore() && meta.getLore() != null) {
                newlore = meta.getLore();
            } else {
                newlore = new ArrayList<>();
            }
            newlore.add(line);
        } else {
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                newlore.add(line);
                for (String str : lore) {
                    newlore.add(str);
                }
            } else {
                newlore.add(line);
            }
        }
        return newlore;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryView view = player.getOpenInventory();
        Inventory inv = player.getOpenInventory().getTopInventory();

        Map<String, Integer> toolMap = new HashMap<>();
        toolMap.put("SWORD", 0);
        toolMap.put("PICKAXE", 1);
        toolMap.put("AXE", 2);
        toolMap.put("SHOVEL", 3);

        if (event.getClickedInventory() != null) { //if the user clicks an inventory
            if (player.getOpenInventory().getTopInventory().getType() == InventoryType.HOPPER) {
                if (view.getTitle().equals(MultitoolU.mtoinv)) {
                    if (event.getClickedInventory().equals(player.getInventory()) && event.isShiftClick()) {
                        String type = event.getCurrentItem().clone().getType().toString();
                        for (String s : toolMap.keySet()) {
                            if (type.contains("_" + s) || s.equals(type)) {
                                if (inv.getItem(toolMap.get(s)).getType() == Material.GRAY_STAINED_GLASS_PANE) {
                                    inv.setItem(toolMap.get(s), event.getCurrentItem().clone());
                                    event.getCurrentItem().setAmount(0);
                                    break;
                                }
                            }
                        }
                        event.setCancelled(true);
                    } else if (event.getClickedInventory().getType() == InventoryType.HOPPER) {
                        if (player.getItemOnCursor().getType() != Material.AIR) { //if the cursor has an item in it
                            Material cursorstack = player.getItemOnCursor().getType();
                            if (event.getCurrentItem() != null) {
                                ItemStack clickstack = event.getCurrentItem().clone();
                                if (clickstack.getType() == Material.GRAY_STAINED_GLASS_PANE) { //if the clicked item is a glass pane
                                    String type = cursorstack.toString();
                                    for (String s : toolMap.keySet()) {
                                        if (type.contains("_" + s) || s.equals(type)
                                                && inv.getItem(toolMap.get(s)).getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                                            inv.setItem(toolMap.get(s), player.getItemOnCursor());
                                            player.setItemOnCursor(null);
                                            break;
                                        }
                                    }
                                    event.setCancelled(true);
                                }
                            }
                        } else {
                            if (event.getCurrentItem() != null) {
                                ItemStack clickstack = event.getCurrentItem().clone();
                                String type = clickstack.getType().toString();
                                for (String s : toolMap.keySet()) {
                                    if (type.contains("_" + s) || s.equals(type)) {
                                        inv.setItem(toolMap.get(s), main.placeholders.get(toolMap.get(s)));
                                        player.setItemOnCursor(clickstack);
                                        break;
                                    }
                                }
                                if (type.contains("FEATHER")) {
                                    boolean forloop = false;
                                    ItemStack genstack = null;
                                    for (int i = 0; i < 5; i++) { //this loops through the mt inv, and gives the player the first multitool that shows up
                                        if (MultitoolU.tools.getItem(i) != null) {
                                            Material curmat = MultitoolU.tools.getItem(i).getType();
                                            forloop = false;
                                            if (curmat != Material.GRAY_STAINED_GLASS_PANE && curmat != Material.FEATHER) {
                                                genstack = MultitoolU.tools.getItem(i).clone();
                                                ItemMeta genmeta = genstack.getItemMeta();
                                                genmeta.setLore(addLore(genmeta, MultitoolU.lore, false));
                                                genstack.setItemMeta(genmeta);
                                                forloop = true; //this means a tool has been found, and will be given to the player if they have space
                                                break;
                                            }
                                        }
                                    }
                                    if (!forloop) {
                                        player.sendMessage(MultitoolU.messages.get("msgempty"));
                                    } else {
                                        Inventory plrinv = player.getInventory();

                                        boolean giveitem;
                                        if (plrinv.firstEmpty() == -1) {
                                            giveitem = false;
                                        } else {
                                            giveitem = true;
                                        }

                                        if (giveitem) {
                                            plrinv.addItem(genstack);
                                            player.sendMessage(MultitoolU.messages.get("msggiven"));
                                        } else {
                                            player.sendMessage(MultitoolU.messages.get("msgnospace"));
                                        }
                                    }
                                    event.setCancelled(true);
                                    player.closeInventory();
                                } else {
                                    if (clickstack.getType() != Material.GRAY_STAINED_GLASS_PANE && clickstack.getType() != Material.AIR) {
                                        inv.setItem(event.getRawSlot(), main.placeholders.get(event.getRawSlot()));
                                        player.setItemOnCursor(clickstack);
                                    }
                                }
                            }
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
