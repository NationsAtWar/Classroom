package org.nationsatwar.nations.objects;

public class User extends NationsObject {
	private String name;
	private String locationKey;
	private String locationDescription = "";
	
	public User() {
		super(-1);
	}
	
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

	public String getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(String locationKey) {
		this.locationKey = locationKey;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}
}