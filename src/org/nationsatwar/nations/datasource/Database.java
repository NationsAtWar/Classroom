package org.nationsatwar.nations.datasource;

import java.util.logging.Level;

import org.nationsatwar.nations.Nations;

public class Database {
	private Nations plugin = null;
	private DataSource source = null;

	public Database(Nations nations) {
		this.plugin = nations;
		DatabaseType type = DatabaseType.fromString(plugin.getConfig().getString("database"));
		switch (type) {
		   case FILE:
		   default:
			   this.source = new File(plugin);
			   this.source.reloadDatabase();
			   break;
		}
	}
	
	public boolean reload() {
		return this.source.reloadDatabase();
	}
	
	public boolean save() {
		return this.source.saveDatabase();
	}

	public boolean add(String playerName, String playerDisplayName, int amount) {
		if(!this.setDisplayName(playerName, playerDisplayName)) {
			return false;
		}
		return this.source.setAmount(playerName, this.source.getAmount(playerName) + amount);
	}

	public boolean subtract(String playerName, String playerDisplayName, int amount) {
		if(!this.setDisplayName(playerName, playerDisplayName)) {
			return false;
		}
		return this.source.setAmount(playerName, this.source.getAmount(playerName) - amount);
	}

	public boolean set(String playerName, String playerDisplayName, int amount) {
		if(!this.setDisplayName(playerName, playerDisplayName)) {
			return false;
		}
		return this.source.setAmount(playerName, amount);
	}
	
	public int get(String playerName) {
		return this.source.getAmount(playerName);
	}
	
	private boolean setDisplayName(String playerName, String displayName) {
		if(this.source.exists(playerName)) {
			if(displayName != null) {
				if(!this.source.getDisplayName(playerName).equals(displayName)) {
					if(!this.source.setDisplayName(playerName, displayName)) {
						this.plugin.getLogger().log(Level.SEVERE, "Unable to set new display name " + displayName + " for " + playerName);
					} else {
						return true;
					}
				}
			}
		}
		return false;
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
