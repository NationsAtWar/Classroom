package org.nationsatwar.nations.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Rank.RankType;
import org.nationsatwar.nations.objects.User;

public class NationManager extends NationsManagement {
	private HashMap<String, Nation> nationMap = new HashMap<String, Nation>();
	
	public NationManager(PluginBase plugin) {
		super(plugin);
	}

	public boolean exists(String nationName) {
		if(nationMap.isEmpty()) {
			return false;
		}
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

	public boolean addFounder(String name) {
		if(plugin instanceof Nations) {
			User user = ((Nations) plugin).userManager.getUserByName(name);
			if(user == null) {
				return false;
			}
			if(user.setRankType(RankType.FOUNDER)) {
				return true;
			}
		}
		return false;
	}

}
