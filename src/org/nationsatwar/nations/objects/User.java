package org.nationsatwar.nations.objects;

public class User extends NationsObject {
	private String name;
	
	public User(int newId, String name) {
		super(newId);
		this.name = name;
	}
	
	public boolean setName(String playerName) {
		this.name = playerName;
		return true;
	}
	
	public String getName() {
		return this.name;
	}
}