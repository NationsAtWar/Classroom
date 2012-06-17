package org.nationsatwar.nations.objects;

public class User extends NationsObject {
	private String name;
	
	public User() {
		super();
	}
	
	public boolean setName(String playerName) {
		this.name = playerName;
		return true;
	}
	
	public String getName() {
		return this.name;
	}
}