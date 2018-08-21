package com.bytekangaroo.setrankpex.commands;

import com.bytekangaroo.setrankpex.Main;
import com.bytekangaroo.setrankpex.utils.PexUserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by Mark on 8/21/2018
 * Written for project SetRankPEX
 * Please do not use or edit this code unless permission has been given (or if it's on GitHub...)
 * Contact me on Twitter, @Mobkinz78, with any questions
 * §
 */
public class BaseCommand implements CommandExecutor {

    String prefix = Main.getInstance().getPrefix();

    PexUserManager manager = PexUserManager.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        // No check necessary, but if something comes up, add a check for player and console here

        // Base command to check for general permission, then more specifics as we go down the line
        if(!sender.hasPermission("setrank.base")){ // or && sender instanceof Player
            sender.sendMessage(prefix + "Sorry, you don't have permission to do this! " + ChatColor.RED + "setrank.base");
            return true;
        } else {
            // Command to change the rank of another player
            if (command.getName().equalsIgnoreCase("setrank")) {
                if (args.length == 0) { // If they just did /setrank
                    sender.sendMessage(prefix + "Too few arguments!");
                    sender.sendMessage(prefix + "See " + ChatColor.YELLOW + " /setrankhelp " + ChatColor.getLastColors(prefix) + " for command help.");
                    return true;
                }
                if (args.length > 2) { // If they provided too many arguments, e.g. /setrank Mark Member Admin
                    sender.sendMessage(prefix + "Too many arguments!");
                    sender.sendMessage(prefix + "See " + ChatColor.YELLOW + " /setrankhelp " + ChatColor.getLastColors(prefix) + " for command help.");
                    return true;
                }
                if (args.length == 1) { // If they only provided the name/rank, e.g. /setrank Mark or /setrank Member
                    sender.sendMessage(prefix + "Please provide a player name AND rank.");
                    sender.sendMessage(prefix + "See " + ChatColor.YELLOW + " /setrankhelp " + ChatColor.getLastColors(prefix) + " for command help.");
                    return true;
                }
                // Argument conditions met, we're good to go! /setrank username rank
                String username = args[0];
                String rankTo = args[1];

                /* Check to make sure the command sender can indeed change the rank of the player. */
                // First check if they can rank TO the specified rank. (E.g. Prevent ranking themselves to an Admin)
                String rankToPerm = "srpex.rankto." + rankTo;
                if (!sender.hasPermission(rankToPerm)) {
                    sender.sendMessage(prefix + "Sorry, you can't rank someone to " + ChatColor.YELLOW + rankTo + ".\n" + prefix + "Required Permission: " + ChatColor.RED + rankToPerm);
                    return true;
                }
                // Next check if they can rank FROM the specified rank. (E.g. Prevent ranking an Admin to a Member)
                String rankFrom = manager.getUserGroup(username);
                String rankFromPerm = "srpex.rankfrom." + rankFrom;
                if(!sender.hasPermission(rankFromPerm)){
                    sender.sendMessage(prefix + "Sorry, you can't rank someone from " + ChatColor.YELLOW + rankFrom + ".\n" + prefix + "Required Permission: " + ChatColor.RED + rankFromPerm);
                    return true;
                }

                manager.setUserGroup(username, rankTo, rankFrom);
                return true;
            }

            // Reload the configuration file
            if (command.getName().equalsIgnoreCase("setrankreload")) {
                if (!sender.hasPermission("srpex.reload")) {
                    sender.sendMessage(prefix + "Sorry, you don't have permission to do this!" + ChatColor.RED + "srpex.reload");
                    return true;
                }

                Main.getInstance().reloadConfig();
                sender.sendMessage(prefix + "Configuration has successfully been reloaded!");
                return true;
            }

            String authors = Main.getInstance().getDescription().getAuthors().toString();
            String authorsEdited = authors.substring(1, authors.length());
            // Send the help menu
            if (command.getName().equalsIgnoreCase("setrankhelp")) {
                sender.sendMessage("----" + ChatColor.DARK_GRAY + "[" + ChatColor.getLastColors(prefix) + "SetRankPex v" + Main.getInstance().getDescription().getVersion()
                        + ChatColor.YELLOW + " by " + authorsEdited + ChatColor.DARK_GRAY + "]" + ChatColor.getLastColors(prefix) + "----"); // Oh boy...
                sender.sendMessage(prefix + "/setrank" + ChatColor.YELLOW + " <player> <rank>" + ChatColor.GRAY + " - Set a player's rank.");
                sender.sendMessage(prefix + "/setrankreload" + ChatColor.GRAY + " - Reload the configuration file.");
                sender.sendMessage(prefix + "Original Plugin Creator: " + ChatColor.YELLOW + "Irock23");
                return true;
            }
            return true;
        }
    }
}
