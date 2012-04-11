package net.shatteredlands.shatteredbeam.simpleinterest;


import java.util.logging.Logger;

import org.bukkit.event.Listener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleInterest extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Permission perms = null;
	public static Economy econ = null;
	
	@Override
	public void onEnable() {
		// Perform on plugin disable.
		if (!setupEconomy()) {
			log.severe(getDescription().getFullName() + " Disabled; Vault not found.");
			getServer().getPluginManager().disablePlugin(this);
		}
		setupPermissions();
		
		Config.load(this);
		
		getServer().getPluginManager().registerEvents(this, this);
		
		log.info(getDescription().getFullName() + " Loaded.");
	}
	
	public void onDisable() {
		// Perform on plugin disable.
		
		log.info(getDescription().getFullName() + " Unloaded.");
	}
	
	 private boolean setupEconomy() {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) {
	            return false;
	        }
	        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp == null) {
	            return false;
	        }
	        econ = rsp.getProvider();
	        return econ != null;
	    }
	 
	 private boolean setupPermissions() {
	        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
	        perms = rsp.getProvider();
	        return perms != null;
	    }

}