package me.darkolythe.multitoolu;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MultitoolToolDetect implements Listener {

    MultitoolU main;
    MultitoolToolDetect(MultitoolU plugin) {
        main = plugin;
    }

    private int getToolType(Material material) {
        ToolMap map = new ToolMap();
        String mat = material.toString();

        if (map.map.containsKey(mat)) {
            return map.map.get(mat);
        }

        return 6;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity) {
                setItem(player, null, true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        //Get player action
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        //Check if the action is a left click before advancing
        if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR)) {
            //Set the player equal to a variable

            if (action.equals(Action.LEFT_CLICK_AIR) || block.getType().toString().contains("BAMBOO") || block.getType() == Material.COBWEB) {
                setItem(player, block, true);
                return;
            }
            setItem(player, block, false);
        }
    }

    private void setItem(Player player, Block block, boolean isEntity) {
        if (player.hasPermission("multitool.use")) { //If the player has permission, continue

            //Get item in player's hand
            ItemStack handitem = player.getInventory().getItemInMainHand();

            if (MultitoolU.multitoolUtils.isTool(handitem, MultitoolU.lore)) {
                ItemStack givestack;
                if (isEntity) { //if the block is air, make it a sword, else, continue
                    giveSword(player);
                } else if (isEntity) {
                    if (MultitoolU.tools.getItem(5) != null) {
                        if (MultitoolU.tools.getItem(5).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                            givestack = MultitoolU.tools.getItem(5).clone();
                            giveStack(givestack, player);
                        }
                    }
                } else {
                    //if the air was not clicked, continue down the checklist
                    //Check what material it is, and change the tool
                    if (block != null) {
                        Material blocktype = block.getType();

                        int tooltype = getToolType(blocktype);

                        if (tooltype != 6 && MultitoolU.tools.getItem(tooltype) != null) {
                            if (MultitoolU.tools.getItem(tooltype).getType() != Material.GRAY_STAINED_GLASS_PANE) {

                                givestack = MultitoolU.tools.getItem(tooltype).clone();
                                giveStack(givestack, player);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean giveSword(Player player) {
        if (MultitoolU.tools.getItem(0) != null) {
            if (MultitoolU.tools.getItem(0).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                ItemStack givestack = MultitoolU.tools.getItem(0).clone();
                giveStack(givestack, player);
                return true;
            }
        }
        return false;
    }

    public void giveStack(ItemStack givestack, Player player) {
        if (givestack.getType() != Material.AIR) { //if the block being hit changed, update the held item
            ItemMeta givemeta = givestack.getItemMeta();
            givemeta.setLore(MultitoolInventory.addLore(givemeta, MultitoolU.lore, false));
            givestack.setItemMeta(givemeta);

            ItemStack handItem = player.getInventory().getItemInMainHand();
            float dura = ((float)handItem.getType().getMaxDurability() - (float)handItem.getDurability()) / (float)handItem.getType().getMaxDurability();

            givestack.setDurability((short)(givestack.getType().getMaxDurability() - Math.round(givestack.getType().getMaxDurability() * dura)));

            player.getInventory().setItemInMainHand(givestack);
        }
    }
}
