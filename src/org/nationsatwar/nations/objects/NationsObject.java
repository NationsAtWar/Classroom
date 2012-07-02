package org.nationsatwar.nations.objects;

public abstract class NationsObject {
	protected int id;
	
	protected NationsObject(int newID) {
		this.id = newID;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

}
