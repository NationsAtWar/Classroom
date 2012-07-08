package org.nationsatwar.nations.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Nation extends NationsObject {
	private String name;
	private ArrayList<Integer> customRanks = new ArrayList<Integer>();
	private HashMap<Integer, Integer> members = new HashMap<Integer, Integer>();
	private ArrayList<Integer> towns = new ArrayList<Integer>();
	
	public Nation() {
		super(-1);
	}
	
	public Nation(int newID, String newName) {
		super(newID);
		this.name = newName;
	}

	public void setName(String nationName) {
		this.name = nationName;
	}

	public String getName() {
		return this.name;
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

	public boolean addTown(Town town) {
		if(!this.towns.contains(town.getID())) {
			this.towns.add(town.getID());
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

	public ArrayList<Integer> getTowns() {
		return this.towns;
	}
}
