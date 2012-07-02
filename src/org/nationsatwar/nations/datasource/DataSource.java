package org.nationsatwar.nations.datasource;

import java.util.ArrayList;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.objects.NationsObject;

public abstract class DataSource {
	protected PluginBase plugin;

	public static DataSource getDatabase (PluginBase instance) {
		if(instance.getConfig().getBoolean("use_mysql"))
			return new MySQL(instance);
		return null;
	}
	
	public abstract boolean save(NationsObject obj);
	public abstract boolean load(NationsObject obj);
	public abstract boolean delete(NationsObject obj);
	public abstract ArrayList<NationsObject> gatherDataset(NationsObject obj);
	
}
