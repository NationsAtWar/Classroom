package org.nationsatwar.nations.objects;

import java.util.ArrayList;

public class Alliance extends NationsObject {
	private String name;
	private ArrayList<Integer> nations = new ArrayList<Integer>();
	
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

	public ArrayList<Integer> getNations() {
		return this.nations;
	}
}
