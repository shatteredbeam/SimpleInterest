package net.shatteredlands.shatteredbeam.simpleinterest;


import java.util.logging.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class SimpleInterest extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Permission perms = null;
	public static Economy econ = null;
	
	//Command Handling
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().toLowerCase().startsWith("/si")) return;
		}
	
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
	 
	 private void processInterest() {
		 double bal, gained;
		 
		 for(Player player : getServer().getOnlinePlayers()) {			 
			 	bal = econ.getBalance(player.getPlayerListName());
			 	gained = Math.floor((bal * Config.interest));
			 	player.sendMessage("* gained " + gained + " " + econ.currencyNamePlural() + " in Interest.");
			 	econ.depositPlayer(player.getPlayerListName(), gained);
			 	log.info("Player " + player.getPlayerListName() + " gained " + gained + " " + econ.currencyNamePlural() + " in Interest.");
		}
	}
}