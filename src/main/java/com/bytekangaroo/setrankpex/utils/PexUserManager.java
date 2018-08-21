package com.bytekangaroo.setrankpex.utils;

import com.bytekangaroo.setrankpex.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Map;

/**
 * Created by Mark on 8/21/2018
 * Written for project SetRankPEX
 * Please do not use or edit this code unless permission has been given (or if it's on GitHub...)
 * Contact me on Twitter, @Mobkinz78, with any questions
 * §
 */
public class PexUserManager {

    private static PexUserManager instance = null;
    private String prefix = Main.getInstance().getPrefix();

    private PexUserManager(){

    }

    public static PexUserManager getInstance(){
        if(instance == null){
            instance = new PexUserManager();
        }
        return instance;
    }

    @SuppressWarnings("deprecation")
    public String getUserGroup(String playerName){
        // Player player = Bukkit.getPlayer(playerName);
        PermissionUser user = PermissionsEx.getUser(playerName);
        String group = user.getGroups()[0].getName();
        System.out.println("User group: " + group);
        return group;
    }

    public void setUserGroup(String playerName, String rankTo, String rankFrom){
        PermissionUser user = PermissionsEx.getUser(playerName);
        user.addGroup(rankTo);
        user.removeGroup(rankFrom);
        Player rankedPlayer = Bukkit.getServer().getPlayer(playerName);

        // Check if broadcast option is selected
        if(Main.getInstance().getConfig().getBoolean("broadcast-rank-change")){
            Bukkit.getServer().broadcastMessage(prefix + ChatColor.GREEN + playerName + " has been ranked to " + ChatColor.YELLOW + ChatColor.BOLD + rankTo + "!");
        }
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            if(player.getName().equals(playerName)){
                rankedPlayer.sendMessage(Main.getInstance().getPrefix() + ChatColor.YELLOW + "Your rank has changed!");
            }
        }
    }

}
