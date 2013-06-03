package org.nationsatwar.nations.managers;

import org.nationsatwar.nations.Nations;

public abstract class NationsManagement {
	protected Nations plugin;
	
	protected NationsManagement(Nations plugin) {
		this.plugin = plugin;
	}
	
	public abstract void loadAll();

	public abstract void saveAll();

	public abstract void deleteAll();
}
