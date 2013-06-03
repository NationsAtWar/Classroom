package org.nationsatwar.nations.datasource;

import java.util.ArrayList;

import org.nationsatwar.nations.Nations;

public abstract class DataSource {
	protected Nations plugin;
	protected String organizationType;

	public DataSource(Nations instance, String type) {
		plugin = instance;
		this.organizationType = type;
	}
	
	public abstract boolean reloadDatabase();
	public abstract boolean saveDatabase();
	
	public abstract boolean saveOrganization(String orgName);

	public abstract ArrayList<String> getOrgNames();
	public abstract boolean exists(String orgName);
	
}
