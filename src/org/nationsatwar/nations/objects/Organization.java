package org.nationsatwar.nations.objects;

import java.util.ArrayList;

public class Organization {
	private String name;
	private ArrayList<String> members;
	private ArrayList<Integer> plots;
	
	public Organization(String newName, String founder) {
		this.name = newName;
		this.members = new ArrayList<String>();
		this.members.add(founder);
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
}
