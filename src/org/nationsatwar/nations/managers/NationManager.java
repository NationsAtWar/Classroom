package org.nationsatwar.nations.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.objects.Nation;

public class NationManager extends NationsManagement {
	private HashMap<String, Nation> nationMap = new HashMap<String, Nation>();
	
	public NationManager(PluginBase plugin) {
		super(plugin);
	}

	public boolean exists(String nationName) {
		return nationMap.containsKey(nationName);
	}

	public boolean addNation(Nation nation) {
		if(!this.exists(nation.getName())) {
			this.nationMap.put(nation.getName(), nation);
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> getNationList() {
		return new ArrayList<String>(nationMap.keySet());
	}

}
