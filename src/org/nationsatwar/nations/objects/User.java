package org.nationsatwar.nations.objects;

import org.nationsatwar.nations.objects.Rank.RankType;

public class User extends NationsObject {
	private String name;
	private String rank;
	private RankType rankType;
	private String nation;
	
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

	public boolean setRankType(RankType type) {
		this.rankType = type;
		return true;
	}
	
	public RankType getRankType() {
		return this.rankType;
	}
	
	public String getNation() {
		return this.nation;
	}
}