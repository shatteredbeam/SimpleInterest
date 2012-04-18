package net.shatteredlands.shatteredbeam.simpleinterest;


import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
		// Perform on plugin disable.
		if (!setupEconomy()) {
			log.severe(getDescription().getFullName() + " Disabled; Vault not found.");
			getServer().getPluginManager().disablePlugin(this);
		}
		setupPermissions();
		
		Config.load(this);
		
		//getServer().getPluginManager().registerEvents(this, this);
		
		log.info(getDescription().getFullName() + " Loaded.");
	}
	
	public void onDisable() {
		// Perform on plugin disable.
		
		log.info(getDescription().getFullName() + " Unloaded.");
	}
	
	 @Override
	 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	 			if(command.getName().equalsIgnoreCase("si")) {
	 				
	 				if (sender instanceof Player) {
	 					Player player = (Player) sender;
	 					sender.sendMessage(ChatColor.YELLOW + "Simple Interest:");
	 				} else {
	 					if (sender instanceof ConsoleCommandSender) {
	 						sender.sendMessage("Simple Interest:");
	 					}
	 				return true;
	 				}
	 			}
	 	return false;
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







