package org.nationsatwar.nations.datasource;

import java.util.ArrayList;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.objects.NationsObject;

public abstract class DataSource {
	protected PluginBase plugin;

	public DataSource(PluginBase instance) {
		plugin = instance;
	}
	
	public abstract boolean save(NationsObject obj);
	public abstract boolean load(NationsObject obj);
	public abstract boolean delete(NationsObject obj);
	public abstract ArrayList<NationsObject> gatherDataset(NationsObject obj);
	
}
