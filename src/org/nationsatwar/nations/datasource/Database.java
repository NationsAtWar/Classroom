package org.nationsatwar.nations.datasource;

import java.util.ArrayList;
import org.nationsatwar.nations.Nations;

public class Database {
	private Nations plugin = null;
	private DataSource source = null;

	public Database(Nations nations, String orgType) {
		this.plugin = nations;
		
		DatabaseType type = DatabaseType.fromString(plugin.getConfig().getString("database"));
		switch (type) {
		   case FILE:
		   default:
			   this.source = new DataFile(plugin, orgType);
			   this.source.reloadDatabase();
			   break;
		}
	}
	
	public boolean reload() {
		return this.source.reloadDatabase();
	}
	
	public boolean save(String org) {
		return false;
	}

	public ArrayList<String> getOrgNames() {
		return this.source.getOrgNames();
	}
	
	private enum DatabaseType {
		FILE;

		public static DatabaseType fromString(String text) {
			if (text != null) {
				for (DatabaseType type : DatabaseType.values()) {
					if (text.equalsIgnoreCase(type.name())) {
						return type;
					}
				}
			}
			throw new IllegalArgumentException("Invalid Type");
		}
	}

}
