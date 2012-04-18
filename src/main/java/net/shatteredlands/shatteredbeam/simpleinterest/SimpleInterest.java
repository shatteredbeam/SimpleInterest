package net.shatteredlands.shatteredbeam.simpleinterest;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class SimpleInterest extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Permission perms = null;
	public static Economy econ = null;
	
	@Override
	public void onEnable() {

		Integer interval = null;
		
		if (!setupEconomy()) {
			log.severe(getDescription().getFullName() + " Disabled; Vault or Economy Plugin Not found.");
			
			getServer().getPluginManager().disablePlugin(this);
		}
		
		setupPermissions();
		
		Config.load(this);

		interval = (Config.interval * 1200);
	
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			   public void run() {
			       processInterest();
			   }
			}, 60, interval);
		
		log.info(getDescription().getFullName() + " Loaded.");
	}
	
	@Override
	public void onDisable() {
		
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
	 
	 public void processInterest() {
		 double bal, gained;
		 
		 NumberFormat formatter = new DecimalFormat("#.##");
		 
		 if(getServer().getOnlinePlayers().length == 0) {
			 
			 	log.info("No Interest Processed: No Players online.");
			 	
		 }
		 
		 if(getServer().getOnlinePlayers().length >= 1) {
			 
			 for(Player player : getServer().getOnlinePlayers()) {			 
			 	
				 bal = econ.getBalance(player.getPlayerListName());
			 	
				 gained = bal * ( 1 * Config.interest);
			 	
				 	if (gained == 0) {
					 
				 		player.sendMessage("Interest: You gained no interest this cycle.");
					 
				 	} else {
					 
				 		player.sendMessage("* You gained " + formatter.format(gained) + " " + econ.currencyNamePlural() + " in interest.");
				 	}
				 
				 econ.depositPlayer(player.getPlayerListName(), gained);
			 	
				 log.info("Player " + player.getPlayerListName() + " gained " + formatter.format(gained) + " " + econ.currencyNamePlural() + " in Interest.");
			 }
		 }
	 }
}







