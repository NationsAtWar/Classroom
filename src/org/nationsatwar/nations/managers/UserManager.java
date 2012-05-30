package org.nationsatwar.nations.managers;

import org.bukkit.plugin.PluginBase;

public class UserManager extends NationsManagement {

	public UserManager(PluginBase plugin) {
		super(plugin);
	}

	public boolean isInNation(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean exists(String string) {
		return plugin.getServer().getPlayerExact(string).hasPermission("nations.nations.user");
	}

}
