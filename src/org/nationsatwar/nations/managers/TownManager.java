package org.nationsatwar.nations.managers;

import java.util.HashMap;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Rank;
import org.nationsatwar.nations.objects.Town;
import org.nationsatwar.nations.objects.User;
import org.nationsatwar.nations.objects.Rank.RankType;

public class TownManager extends NationsManagement {
	private HashMap<String, Town> townMap = new HashMap<String, Town>();

	public TownManager(PluginBase plugin) {
		super(plugin);
	}

	public boolean exists(String townName) {
		if(townMap.isEmpty()) {
			return false;
		}
		return townMap.containsKey(townName);
	}

	public Town getTownByUsername(String name) {
		for(Town town : this.townMap.values()) {
			if(town.getMembers().contains(name)) {
				return town;
			}
		}
		return null;
	}

	public boolean addFounder(Town town, String name) {
		if(plugin instanceof Nations) {
			User user = ((Nations) plugin).userManager.getUserByName(name);
			if(user == null) {
				return false;
			}
			for(Rank rank : town.getRanks(RankType.FOUNDER)) {
				if(town.setRank(user.getName(), rank)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean addTown(Town town) {
		if(!this.exists(town.getName())) {
			this.townMap.put(town.getName(), town);
			return true;
		} else {
			return false;
		}
	}

	public boolean addPlotToTown(Plot plot, Town town) {
		if(town.addPlot(plot)) {
			return true;
		}
		return false;
	}

}
