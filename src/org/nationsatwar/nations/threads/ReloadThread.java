package org.nationsatwar.nations.threads;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;

public class ReloadThread implements Runnable {
	private PluginBase plugin;
	
	/**
	 * 
	 * @param name Name for the thread -- this same value must be given to the 
	 * @param delay
	 */
	public ReloadThread(PluginBase instance) {
		super();
		plugin = instance;
	}

	@Override
	public void run() {
		if(plugin instanceof Nations) {
			((Nations) plugin).reload(null);
		}
	}
}
