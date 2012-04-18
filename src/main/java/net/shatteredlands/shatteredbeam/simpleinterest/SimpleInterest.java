package net.shatteredlands.shatteredbeam.simpleinterest;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
		// Perform on enable.
		if (!setupEconomy()) {
			log.severe(getDescription().getFullName() + " Disabled; Vault not found.");
			getServer().getPluginManager().disablePlugin(this);
		}
		setupPermissions();
		
		Config.load(this);
		
		log.info(getDescription().getFullName() + " Loaded.");
	}
	
	public void onDisable() {
		// Perform on disable.
		
		log.info(getDescription().getFullName() + " Unloaded.");
	}
	
	 @Override
	 @SuppressWarnings("unused")
	 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	 			if(command.getName().equalsIgnoreCase("si")) {
	 			
	 			if(args.length == 0) { return false; }
	 			
	 				if (sender instanceof Player) {
	 					Player player = (Player) sender;
	 					
	 					if(args[0].equalsIgnoreCase("run")) {
	 						this.processInterest();
	 					}
	 					
	 					if(args[0].equalsIgnoreCase("force")) {
	 						this.processSingle("test");
	 					}
	 					
	 					if(args.length != 1) {
	 						sender.sendMessage(ChatColor.YELLOW + "Simple Interest:");
	 						sender.sendMessage(ChatColor.YELLOW + " Usage: /si [run]");
	 						sender.sendMessage(ChatColor.YELLOW + "   run: Force Interest gain for online players.");
	 						return true;
	 					}
	 				} else {
	 					if (sender instanceof ConsoleCommandSender) {
		 					if(args.length != 1) {
		 						sender.sendMessage("Simple Interest:");
		 						sender.sendMessage(" Usage: /si [run]");
		 						sender.sendMessage("   run: Force Interest gain for online players.");
		 						return true;
		 					}
		 						if(args[0].equalsIgnoreCase("run")) {
		 							processInterest();
		 							sender.sendMessage("Interest cycle forced");
		 							return true;
		 					}
		 						
		 						if(args[0].equalsIgnoreCase("force")) {
			 						this.processSingle("test");
			 					}
		 						
		 						if(args[0].equalsIgnoreCase("check")) {
		 							if (args.length < 2) return false;
		 							
		 							if (econ.hasAccount(args[1])) {
		 								sender.sendMessage(args[1] + "has an account.");
		 								return true;
		 							} else { 
		 								sender.sendMessage(args[1] + "does NOT have an account.");
		 								return false; }
		 						}
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
	 
	 public void processSingle(String player) {
		 double bal, gained;
		 
		 NumberFormat formatter = new DecimalFormat("#.##");
		 
		 if (econ.hasAccount(player)) {
			 bal = econ.getBalance(player);
			 
			 gained = bal * ( 1 * Config.interest);
			 
			 if (gained == 0) {
				 log.info("SimpleInterest: " + player + " gained no interest.");
			 } else {
				 log.info("SimpleInterest: Player " + player + " gained " + formatter.format(gained) + " " + econ.currencyNamePlural() + " in Interest.");
			 }
			 
			 econ.depositPlayer(player, gained);
			 
			 
		 } else {
			 log.info("SimpleInterest: Player " + player + " not found or no account");
		 }
	 }
}







