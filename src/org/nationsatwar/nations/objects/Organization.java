package org.nationsatwar.nations.objects;

import java.util.ArrayList;

public class Organization {
	private String name;
	private ArrayList<String> members;
	private ArrayList<Integer> plots;
	private int level;
	
	public Organization(String newName, String founder, int newLevel) {
		this.name = newName;
		this.members = new ArrayList<String>();
		this.members.add(founder);
		this.level = newLevel;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean addMember(String newMember) {
		return this.members.add(newMember);
	}
	
	public ArrayList<String> getMembers() {
		return this.members;
	}

	public boolean removeMember(String member) {
		return this.members.remove(member);
	}
	
	public int getLevel() {
		return this.level;
	}
}
