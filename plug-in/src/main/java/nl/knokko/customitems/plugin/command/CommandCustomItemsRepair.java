package nl.knokko.customitems.plugin.command;

import nl.knokko.customitems.item.CustomItemValues;
import nl.knokko.customitems.item.CustomToolValues;
import nl.knokko.customitems.plugin.set.ItemSetWrapper;
import nl.knokko.customitems.plugin.set.item.CustomToolWrapper;
import nl.knokko.customitems.plugin.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static nl.knokko.customitems.plugin.command.CommandCustomItems.getOnlinePlayer;
import static nl.knokko.customitems.plugin.set.item.CustomToolWrapper.wrap;

class CommandCustomItemsRepair {

    final ItemSetWrapper itemSet;

    CommandCustomItemsRepair(ItemSetWrapper itemSet) {
        this.itemSet = itemSet;
    }

    void handle(String[] args, CommandSender sender) {
        if (
                (args[0].equals("repair") && !sender.hasPermission("customitems.repair"))
                        || (args[0].equals("damage") && !sender.hasPermission("customitems.damage"))
        ) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have access to this command.");
            return;
        }

        if (args.length <= 1) {
            sender.sendMessage(ChatColor.RED + "You should use /kci " + args[0] + "<amount> [player]");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException notAnInteger) {
            sender.sendMessage(ChatColor.RED + "The amount (" + args[1] + ") should be an integer");
            return;
        }

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "The amount should be positive");
            return;
        }

        Player target;
        if (args.length > 2) {
            target = getOnlinePlayer(args[2]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "No online player with name " + args[2] + " was found");
                return;
            }
        } else {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "You should use /kci " + args[0] + " <amount> <player>");
                return;
            }
        }

        ItemStack item = target.getInventory().getItemInMainHand();
        if (ItemUtils.isEmpty(item)) {
            sender.sendMessage(ChatColor.RED + target.getName() + " should hold the item to " + args[0] + "in the main hand");
            return;
        }

        CustomItemValues customItem = itemSet.getItem(item);
        if (customItem == null) {
            sender.sendMessage(ChatColor.RED + "The item in the main hand of " + target.getName() + " should be a custom item");
            return;
        }

        if (!(customItem instanceof CustomToolValues)) {
            sender.sendMessage(ChatColor.RED + "The item in the main hand of " + target.getName() + " should be a custom tool");
            return;
        }

        CustomToolValues customTool = (CustomToolValues) customItem;
        if (customTool.getMaxDurabilityNew() == null) {
            sender.sendMessage(ChatColor.RED + "The tool in the main hand of " + target.getName() + " is unbreakable");
            return;
        }

        if (args[0].equals("repair")) {
            CustomToolWrapper.IncreaseDurabilityResult result = wrap(customTool).increaseDurability(item, amount);
            if (result.increasedAmount == 0) {
                sender.sendMessage(ChatColor.RED + "The tool in the main hand of " + target.getName() + " wasn't damaged");
                return;
            }

            target.getInventory().setItemInMainHand(result.stack);
        }

        if (args[0].equals("damage")) {
            ItemStack result = wrap(customTool).decreaseDurability(item, amount);
            target.getInventory().setItemInMainHand(result);
        }

    }
}
