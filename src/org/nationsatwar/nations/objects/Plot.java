package org.nationsatwar.nations.objects;

import org.bukkit.Location;

public class Plot extends NationsObject {
	public String world;
	public int x = -1;
	public int z = -1;
	private String locationDescription;
	
	public Plot() {
		super(-1);
	}
	
	public Plot(int newId, Location location, Nation nation, Town town) {
		super(newId);
		if(location != null) {
			this.x = location.getChunk().getX();
			this.z = location.getChunk().getZ();
			this.world = location.getWorld().getName();
		}
		if(nation != null && town != null) {
			this.locationDescription = nation.getName()+";"+town.getName();
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

	public String getLocationDescription() {
		String nation = "";
		String town = "";
		
		String[] description = this.locationDescription.split(";");
		if(description.length > 2) {
			nation = description[0];
			town = description[1];
		}
		return "Nation: "+nation+" Town: "+town;
	}

}
