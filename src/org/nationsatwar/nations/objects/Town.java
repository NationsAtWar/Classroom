package org.nationsatwar.nations.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.nationsatwar.nations.objects.Rank.RankType;

public class Town extends NationsObject {
	private String name;
	private ArrayList<Plot> plots;
	private HashMap<String, Rank> members;
	private ArrayList<Rank> ranks;
	
	public Town() {
		super();
		this.ranks.add(new Rank("Founder",RankType.FOUNDER));
		this.ranks.add(new Rank("Recruit",RankType.RECRUIT));
	}

	public String getName() {
		return name;
	}

	public void setName(String newname) {
		name = newname;
	}
	
	public boolean addPlot(Plot plot) {
		if(!this.plots.contains(plot)) {
			this.plots.add(plot);
			return true;
		}
		return false;
	}

	public ArrayList<String> getMembers() {
		ArrayList<String> membs = new ArrayList<String>(this.members.keySet());
		return membs;
	}

	public ArrayList<Rank> getRanks(RankType type) {
		if(type == null) {
			return this.ranks;
		}
		ArrayList<Rank> list = new ArrayList<Rank>();
		for(Rank rank : this.ranks) {
			if(rank.type == type) {
				list.add(rank);
			}
		}
		return list;
	}

	public boolean setRank(String playername, Rank rank) {
		if(this.ranks.contains(rank)) {
			this.members.put(playername, rank);
			return true;
		}
		return false;
	}

	public boolean removeMember(User user) {
		if(this.members.containsKey(user.getName())) {
			Rank rank = this.members.remove(user.getName());
			if(rank.type == RankType.FOUNDER) {
				//TODO: stuff for handling founder roles.
			}
			return true;
		}
		return false;
	}
}
