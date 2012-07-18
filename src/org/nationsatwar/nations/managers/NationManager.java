package org.nationsatwar.nations.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Town;

public class NationManager extends NationsManagement {
	private HashMap<Integer, Nation> nationMap = new HashMap<Integer, Nation>();
	
	public NationManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		nationMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new Nation(0, null))) {
			Nation object = (Nation) obj;
			if (!nationMap.containsKey(object.getID()))
				nationMap.put(object.getID(), object);
		}
	}

	@Override
	public void saveAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Nation object : nationMap.values()) {
			nations.database.save(object);
		}
	}

	@Override
	public void deleteAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Nation object : nationMap.values()) {
			nations.database.delete(object);
		}
		nationMap.clear();
	}
	
	public Nation createNation(String name) {
		int newKey = 0; 
		try {
			newKey = Collections.max(nationMap.keySet())+1;
		} catch(NoSuchElementException e) {
			
		}
		Nation newNation = new Nation(newKey, name);
		if(this.addNation(newNation)) {
			return newNation;
		}
		return null;
	}

	private boolean addNation(Nation nation) {
		if(!this.nationMap.containsKey(nation.getID())) {
			this.nationMap.put(nation.getID(), nation);
			return true;
		} else {
			return false;
		}
	}
	
	public Nation getNationByUserID(int id) {
		for(Nation nation : this.nationMap.values()) {
			if(nation.getMembers(null).contains(id)) {
				return nation;
			}
		}
		return null;		
	}

	//Trying not to use
	/*private Nation getNationByUsername(String name) {
		for(Nation nation : this.nationMap.values()) {
			if(nation.getMembers(null).contains(name)) {
				return nation;
			}
		}
		return null;
	}*/
	
	public Nation getNationByName(String name) {
		for(Nation nation : this.nationMap.values()) {
			if(nation.getName().equalsIgnoreCase(name)) {
				return nation;
			}
		}
		return null;
	}
	
	public HashMap<Integer, Nation> getNations() {
		return this.nationMap;
	}

	public Nation getNationByID(int nation) {
		return this.nationMap.get(nation);
	}

	public boolean delete(Nation nation) {
		if(!(plugin instanceof Nations)) {
			return false;
		}
		Nations nations = (Nations) plugin;
		
		if(this.nationMap.containsKey(nation.getID())) {
			for(int townID : nation.getTowns()) {
				Town town = nations.townManager.getTownByID(townID);
				if(town != null) {
					nations.townManager.delete(town);
				}
			}
			if(this.nationMap.remove(nation.getID()) != null) {
				nations.database.delete(nation);
				return true;
			}
		}
		return false;
		
	}
}
