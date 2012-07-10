package org.nationsatwar.nations.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Town extends NationsObject {
	private String name;
	private ArrayList<Integer> plots = new ArrayList<Integer>();
	private HashMap<Integer, Integer> members = new HashMap<Integer, Integer>();
	private ArrayList<Integer> customRanks = new ArrayList<Integer>();
	private Integer nation;
	
	public Town() {
		super(-1);
	}
	
	public Town(int newID, Nation newNation, String name) {
		super(newID);
		this.name = name;
		this.nation = newNation.getID();
	}

	public String getName() {
		return name;
	}

	public void setName(String newname) {
		name = newname;
	}
	
	public boolean addPlot(Plot plot) {
		if(!this.plots.contains(plot.getID())) {
			this.plots.add(plot.getID());
			return true;
		}
		return false;
	}

	public ArrayList<Integer> getMembers(Rank rank) {
		if(rank == null) {
			return new ArrayList<Integer>(this.members.keySet());
		}
		ArrayList<Integer> membs = new ArrayList<Integer>();
		for(int a : this.members.keySet()) {
			if(this.members.get(a) == rank.getID()) {
				membs.add(a);
			}
		}
		return membs;
	}

	public ArrayList<Integer> getRanks() {
		return this.customRanks;
	}

	public boolean setRank(User user, Rank rank) {
		if(this.members.containsKey(user.getID())) {
			this.members.put(user.getID(), rank.getID());
			return true;
		}
		return false;
	}

	public boolean removeMember(User user) {
		if(this.members.containsKey(user.getID())) {
			this.members.remove(user.getID());
			return true;
		}
		return false;
	}

	public boolean addMember(User user, Rank rank) {
		if(!this.members.containsKey(user.getID())) {
			this.members.put(user.getID(), rank.getID());
			return true;
		}
		return false;
	}

	public ArrayList<Integer> getPlots() {
		return this.plots;
	}
	
	public int getNation() {
		return this.nation;
	}
	
	public boolean setNation(int newNation) {
		if(this.nation != newNation) {
			this.nation = newNation;
			return true;
		}
		return false;
	}
}
