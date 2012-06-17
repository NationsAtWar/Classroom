package org.nationsatwar.nations.objects;

import org.bukkit.Location;

public class Plot extends NationsObject {
	public String world;
	public int x;
	public int z;
	
	public Plot(String world, int x, int z) {
		super();
		this.x = x;
		this.z = z;
		this.world = world;
	}

	public Plot(Location location) {
		super();
		this.x = location.getChunk().getX();
		this.z = location.getChunk().getZ();
		this.world = location.getWorld().getName();
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
