package net.shatteredlands.shatteredbeam.simpleinterest;

import java.io.File;

public class Config {
	public static int version;
	
	public static void load(SimpleInterest plugin) {
		if(!new File(plugin.getDataFolder() , "config.yml").exists()) {
			plugin.saveDefaultConfig();
		}
		
	//set nodes
		version = plugin.getConfig().getInt("General.Version");
	}

}