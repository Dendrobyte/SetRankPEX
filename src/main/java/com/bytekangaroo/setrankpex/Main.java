package com.bytekangaroo.setrankpex;

import com.bytekangaroo.setrankpex.commands.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    // PLUGIN DESCRIPTION
    private String prefix = "§8[§7SetRankPEX§8]§7 ";

    private static Main main;

    @Override
    public void onEnable() {
        // Get the plugin manager
        PluginManager pm = Bukkit.getServer().getPluginManager();
        final Plugin PERMISSIONS_EX = pm.getPlugin("PermissionsEx");

        // Check for and enable PLUGIN_NAME
        if(PERMISSIONS_EX != null && !PERMISSIONS_EX.isEnabled()){
            getLogger().log(Level.WARNING, "SetRankPex could not find the core PermissionsEx plugin!");
            getLogger().log(Level.WARNING, "SetRankPex has been disabled.");
            pm.disablePlugin(this);
            return;
        }

        // Create configuration
        createConfig();

        // Register the Main instance
        main = this;

        // Register events

        // Register commands
        getCommand("setrank").setExecutor(new BaseCommand());
        getCommand("setrankreload").setExecutor(new BaseCommand());
        getCommand("setrankhelp").setExecutor(new BaseCommand());

        getLogger().log(Level.INFO, "SetRankPex has successfully been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "SetRankPex has successfully been disabled!");
    }

    public void createConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            getLogger().log(Level.INFO, "No configuration found for SetRankPex " + getDescription().getVersion());
            saveDefaultConfig();
        } else {
            getLogger().log(Level.INFO, "Configuration found for SetRankPex v" + getDescription().getVersion() + "!");
        }
    }

    public static Main getInstance() {
        return main;
    }

    public String getPrefix() {
        return prefix;
    }
}