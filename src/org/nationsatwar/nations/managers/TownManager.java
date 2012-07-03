package org.nationsatwar.nations.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Town;

public class TownManager extends NationsManagement {
	private HashMap<Integer, Town> townMap = new HashMap<Integer, Town>();

	public TownManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		townMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new Town(0, null))) {
			Town object = (Town) obj;
			if (!townMap.containsKey(object.getID()))
				townMap.put(object.getID(), object);
		}
	}

	@Override
	public void saveAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Town object : townMap.values()) {
			nations.database.save(object);
		}
	}

	@Override
	public void deleteAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Town object : townMap.values()) {
			nations.database.delete(object);
		}
		townMap.clear();
	}
	
	public Town createTown(String name) {
		int newKey = 0; 
		try {
			newKey = Collections.max(townMap.keySet())+1;
		} catch(NoSuchElementException e) {
			
		}
		Town newTown = new Town(newKey, name);
		if(this.addTown(newTown)) {
			return newTown;
		}
		return null;
	}
	
	private boolean addTown(Town town) {
		if(!this.townMap.containsKey(town.getID())) {
			this.townMap.put(town.getID(), town);
			return true;
		} else {
			return false;
		}
	}

	//Trying not to use
	/*private Town getTownByUsername(String name) {
		for(Town town : this.townMap.values()) {
			if(town.getMembers().contains(name)) {
				return town;
			}
		}
		return null;
	}*/

	public Town getTownByUserID(int id) {
		for(Town town : this.townMap.values()) {
			if(town.getMembers(null).contains(id)) {
				return town;
			}
		}
		return null;		
	}
/*
	public ArrayList<String> getTownList() {
		ArrayList<String> townList = new ArrayList<String>();
		for(Town town : this.townMap.values()) {
			townList.add(town.getName());
		}
		return townList;
	}

	/*public Town getTownByName(String townName) {
		for(Town town : this.townMap.values()) {
			if(town.getName().equalsIgnoreCase(townName)) {
				return town;
			}
		}
		return null;
	}*/

	public Town getTownByID(int key) {
		return this.townMap.get(key);		
	}

	public HashMap<Integer, Town> getTowns() {
		return this.townMap;
	}
}
