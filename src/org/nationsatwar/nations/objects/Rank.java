package org.nationsatwar.nations.objects;

public class Rank extends NationsObject {
	private String name;
	private RankType type;
	
	public Rank(int newID, String newName, RankType newType) {
		super(newID);
		this.setName(newName);
		this.setType(newType);
	}
	
	public RankType getType() {
		return type;
	}

	public void setType(RankType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public enum RankType {
		FOUNDER, RECRUIT, CUSTOM;
	}
}
