package net.shatteredlands.shatteredbeam.simpleinterest;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleInterest extends JavaPlugin implements Listener {
	// Base Class
	
	public void onEnable() {
		// Perform on plugin disable.
		Config.load(this);
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		// Perform on plugin disable.
	}
}