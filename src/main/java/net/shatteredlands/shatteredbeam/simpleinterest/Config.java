package net.shatteredlands.shatteredbeam.simpleinterest;

import java.io.File;

public class Config {
	public static int interval;
	public static double interest;
	
	public static void load(SimpleInterest plugin) {
		
		if(!new File(plugin.getDataFolder() , "config.yml").exists()) {
			
			plugin.saveDefaultConfig();
		
		}
		
		interest = plugin.getConfig().getDouble("General.InterestRate");
		
		interval = plugin.getConfig().getInt("General.Interval");
	}

}