package org.nationsatwar.nations.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Alliance extends NationsObject {
	private String name;
	private ArrayList<Integer> nations = new ArrayList<Integer>();
	private HashMap<Integer, Integer> members = new HashMap<Integer, Integer>();
	
	public Alliance() {
		super(-1);
	}
	
	public Alliance(int newID, String newName) {
		super(newID);
		this.name = newName;
	}

	public void setName(String nationName) {
		this.name = nationName;
	}

	public String getName() {
		return this.name;
	}

	public boolean addNation(Nation nation) {
		if(!this.nations.contains(nation.getID())) {
			this.nations.add(nation.getID());
			return true;
		}
		return false;
	}
	
	public boolean removeNation(Nation nation) {
		if(this.nations.contains(nation.getID())) {
			this.nations.remove((Integer) nation.getID());
			return true;
		}
		return false;
	}

	public ArrayList<Integer> getNations() {
		return this.nations;
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
	
	public ArrayList<Integer> getMembers() {
		return new ArrayList<Integer>(this.members.keySet());
	}
}
