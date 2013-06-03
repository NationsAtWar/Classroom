package org.nationsatwar.nations.datasource;

import java.util.ArrayList;

import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Organization;

public abstract class DataSource {
	protected Nations plugin;
	protected String organizationType;

	public DataSource(Nations instance, String type) {
		plugin = instance;
		this.organizationType = type;
	}
	
	public abstract boolean reloadDatabase();
	public abstract boolean saveDatabase();
	
	public abstract boolean saveOrganization(Organization orgName);
	public abstract Organization getOrganization(String orgName);
	public abstract boolean removeOrganization(String orgName);
	public abstract boolean addOrganization(Organization org);

	public abstract ArrayList<String> getOrgNames();
	public abstract boolean exists(String orgName);
}
