package org.nationsatwar.nations.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Alliance;
import org.nationsatwar.nations.objects.NationsObject;

public class AllianceManager extends NationsManagement {
	private HashMap<Integer, Alliance> allianceMap = new HashMap<Integer, Alliance>();
	
	public AllianceManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		allianceMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new Alliance(0, null))) {
			Alliance object = (Alliance) obj;
			if (!allianceMap.containsKey(object.getID()))
				allianceMap.put(object.getID(), object);
		}
	}

	@Override
	public void saveAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Alliance object : allianceMap.values()) {
			nations.database.save(object);
		}
	}

	@Override
	public void deleteAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Alliance object : allianceMap.values()) {
			nations.database.delete(object);
		}
		allianceMap.clear();
	}
	
	public Alliance createAlliance(String name) {
		int newKey = 0; 
		try {
			newKey = Collections.max(allianceMap.keySet())+1;
		} catch(NoSuchElementException e) {
			
		}
		Alliance newAlliance = new Alliance(newKey, name);
		if(this.addAlliance(newAlliance)) {
			return newAlliance;
		}
		return null;
	}

	private boolean addAlliance(Alliance alliance) {
		if(!this.allianceMap.containsKey(alliance.getID())) {
			this.allianceMap.put(alliance.getID(), alliance);
			return true;
		} else {
			return false;
		}
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
	
	public Alliance getAllianceByName(String name) {
		for(Alliance alliance : this.allianceMap.values()) {
			if(alliance.getName().equalsIgnoreCase(name)) {
				return alliance;
			}
		}
		return null;
	}
	
	public HashMap<Integer, Alliance> getAlliances() {
		return this.allianceMap;
	}

	public Alliance getAllianceByID(int alliance) {
		return this.allianceMap.get(alliance);
	}

	public boolean delete(Alliance alliance) {
		if(!(plugin instanceof Nations)) {
			return false;
		}
		Nations nations = (Nations) plugin;
		
		if(this.allianceMap.containsKey(alliance.getID())) {
			if(this.allianceMap.remove(alliance.getID()) != null) {
				nations.database.delete(alliance);
				return true;
			}
		}
		return false;
		
	}
}
