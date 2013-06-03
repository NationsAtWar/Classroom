package org.nationsatwar.nations.datasource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nationsatwar.nations.Nations;

public class DataFile extends DataSource {
	private FileConfiguration storage = null;
	private File storageFile = null;

	public DataFile(Nations instance, String orgType) {
		super(instance, orgType);
	}

	public boolean reloadDatabase() {
		if(storageFile == null) {
			try {
				storageFile = new File(plugin.getDataFolder(), organizationType+"_database.yml");
			} catch(Exception e) {
				plugin.getLogger().log(Level.SEVERE, "Could not open file " + plugin.getDataFolder()+ "/"+organizationType+"_database.yml");
				return false;
			}
		}
		storage = YamlConfiguration.loadConfiguration(storageFile);
		return true;
	}
	
	private FileConfiguration getDatabase() {
		if(storage == null) {
			this.reloadDatabase();
		}
		return storage;
	}
	
	public boolean saveDatabase() {
		if(storage == null || storageFile == null) {
			return false;
		}
		try {
			getDatabase().save(storageFile);
			return true;
		} catch(IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save database to "+storageFile, e);
			return false;
		}
	}

	@Override
	public boolean exists(String orgName) {
		return this.getDatabase().getConfigurationSection("orgName") != null;
	}

	@Override
	public boolean saveOrganization(String orgName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> getOrgNames() {
		// TODO Auto-generated method stub
		return null;
	}
}
