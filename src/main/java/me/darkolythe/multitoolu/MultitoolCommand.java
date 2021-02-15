package me.darkolythe.multitoolu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultitoolCommand implements CommandExecutor {

	private MultitoolU main = MultitoolU.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("multitoolu.command")) {
				if (cmd.getName().equalsIgnoreCase("multitoolu")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("o")) {
							player.openInventory(main.multitoolInventory.getInventory());
							return true;
						}
					}
				}
				sender.sendMessage(MultitoolU.prefix + ChatColor.RED + "Invalid command arguments: /mtu [open]");
			} else {
				sender.sendMessage(MultitoolU.messages.get("msgnopermission"));
			}
		}
		sender.sendMessage(main.prefix + "You cannot use that command as the console.");
		return true;
	}
}
