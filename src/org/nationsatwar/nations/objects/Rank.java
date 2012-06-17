package org.nationsatwar.nations.objects;

public class Rank extends NationsObject {
	public String name;
	public RankType type;
	
	public Rank(String newName, RankType newType) {
		this.name = newName;
		this.type = newType;
	}
	
	public enum RankType {
		FOUNDER, RECRUIT, CUSTOM;
	}
}
