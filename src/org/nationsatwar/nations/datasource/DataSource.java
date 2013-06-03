package org.nationsatwar.nations.datasource;

import java.util.ArrayList;

import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;

public abstract class DataSource {
	protected Nations plugin;

	public DataSource(Nations instance) {
		plugin = instance;
	}
	
	public abstract boolean save(NationsObject obj);
	public abstract boolean load(NationsObject obj);
	public abstract boolean delete(NationsObject obj);
	public abstract ArrayList<NationsObject> gatherDataset(NationsObject obj);

	public abstract boolean reloadDatabase();
	public abstract boolean saveDatabase();
	
}
