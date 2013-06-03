package org.nationsatwar.nations.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.events.NationsDestroyEvent;
import org.nationsatwar.nations.events.NationsDestroyEvent.NationsDestroyEventType;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Organization;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;

public class OrganizationManager extends NationsManagement {
	private ArrayList<Organization> orgs;
	
	
	private HashMap<Integer, Town> townMap = new HashMap<Integer, Town>();

	public OrganizationManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		townMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new Town(0, null, null))) {
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
	
	public Town createTown(Nation nation, String name) {
		int newKey = 0; 
		try {
			newKey = Collections.max(townMap.keySet())+1;
		} catch(NoSuchElementException e) {
			
		}
		Town newTown = new Town(newKey, nation, name);
		if(this.addTown(newTown) && nation.addTown(newTown)) {
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

	public boolean delete(Town town) {
		if(!(plugin instanceof Nations)) {
			return false;
		}
		Nations nations = (Nations) plugin;
		
		if(this.townMap.containsKey(town.getID())) {
			for(int plotID : town.getPlots()) {
				Plot plot = nations.plotManager.getPlotByID(plotID);
				if(plot != null) {
					nations.plotManager.delete(plot);
				}
			}
			if(this.townMap.remove(town.getID()) != null) {
				nations.database.delete(town);
				return true;
			}
		}
		return false;
	}

	public Organization getOrganization(String org) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addMember(String org, String member) {
		return this.getOrganization(org).addMember(member);
	}

	public boolean removeMember(String memberOrg, String member) {
		Organization org = this.getOrganization(memberOrg);
		if(org.removeMember(member)) {
			if(org.getMembers().isEmpty()) {
				NationsDestroyEvent event = new NationsDestroyEvent(member, org.getName(), NationsDestroyEventType.EMPTY, TODO: ORGLEVEL);
				plugin.getServer().getPluginManager().callEvent(event);
			}
		}
	}

	public void removeOrg(String org, String member, NationsDestroyEventType nationsDestroyEventType) {
		// TODO Auto-generated method stub
		
	}
}
