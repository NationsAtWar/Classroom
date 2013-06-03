package org.nationsatwar.nations.objects;

import org.bukkit.Location;

public class Plot {
	private int id;
	private String world;
	private int x;
	private int z;
	private String description;
	
	public Plot(int newId, Location location) {
		this.id = newId;
		if(location != null) {
			this.x = location.getChunk().getX();
			this.z = location.getChunk().getZ();
			this.world = location.getWorld().getName();
		}
	}

	public String getLocationKey() {
		return this.getWorld()+";"+this.getX()+";"+this.getZ();
	}

	public int getZ() {
		return z;
	}

	public int getX() {
		return x;
	}

	public String getWorld() {
		return world;
	}
	
	public int getId() {
		return this.id;
	}

	public String getLocationDescription() {
		return this.description;
	}

}
