package org.nationsatwar.nations.objects;

import org.bukkit.Location;

public class Plot extends NationsObject {
	public String world;
	public int x;
	public int z;
	
	public Plot(int newId, Location location) {
		super(newId);
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

}
