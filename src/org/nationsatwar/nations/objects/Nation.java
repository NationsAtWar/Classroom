package org.nationsatwar.nations.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.nationsatwar.nations.objects.Rank.RankType;

public class Nation extends NationsObject {
	private String name;
	private ArrayList<Rank> ranks = new ArrayList<Rank>();
	private HashMap<String, Rank> members = new HashMap<String,Rank>();
	private ArrayList<Town> towns = new ArrayList<Town>();
	
	public Nation() {
		super();
		this.ranks.add(new Rank("Founder",RankType.FOUNDER));
		this.ranks.add(new Rank("Recruit",RankType.RECRUIT));
	}

	public void setName(String nationName) {
		this.name = nationName;
	}

	public String getName() {
		return this.name;
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

	public ArrayList<String> getMembers() {
		ArrayList<String> membs = new ArrayList<String>(this.members.keySet());
		return membs;
	}

	public boolean addTown(Town town) {
		if(this.towns.add(town)) {
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
			for(Town town : this.towns) {
				if(town.getMembers().contains(user.getName())) {
					if(!town.removeMember(user)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean addMember(User user) {
		if(!this.members.containsKey(user.getName())) {
			for(Rank rank : this.ranks) {
				if(rank.type == RankType.RECRUIT) {
					this.members.put(user.getName(), rank);
				}
			}
		}
		return false;
	}

}
