package org.nationsatwar.nations.objects;

import java.util.ArrayList;

public class Nation extends NationsObject {
	private String name;
	private ArrayList<String> founders = new ArrayList<String>();

	public Nation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setName(String nationName) {
		this.name = nationName;
	}

	public String getName() {
		return this.name;
	}

	public void addFounder(String founder) {
		this.founders.add(founder);
	}

}
