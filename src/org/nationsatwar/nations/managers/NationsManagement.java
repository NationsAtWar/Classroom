package org.nationsatwar.nations.managers;

import org.bukkit.plugin.PluginBase;

public abstract class NationsManagement {
	protected PluginBase plugin;
	
	protected NationsManagement(PluginBase plugin) {
		this.plugin = plugin;
	}
	
	public abstract void loadAll();

	public abstract void saveAll();

	public abstract void deleteAll();
}
