package org.nationsatwar.nations.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.objects.User;

public class UserManager extends NationsManagement {
	private HashMap<String, User> userMap = new HashMap<String, User>();

	public UserManager(PluginBase plugin) {
		super(plugin);
	}

	public boolean exists(String string) {
		if(userMap.isEmpty()) {
			return false;
		}
		return userMap.containsKey(string);
	}

	public User getUserByName(String name) {
		return this.userMap.get(name);
	}

	public User getUserByPlayer(Player player) {
		if(player == null) {
			return null;
		}
		return(this.userMap.get(player.getName()));
	}

	public boolean addUser(User user) {
		if(!this.exists(user.getName())) {
			this.userMap.put(user.getName(), user);
			return true;
		} else {
			return false;
		}
	}

}
