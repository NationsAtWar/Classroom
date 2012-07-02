package org.nationsatwar.nations.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.User;

public class UserManager extends NationsManagement {
	private HashMap<Integer, User> userMap = new HashMap<Integer, User>();

	public UserManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(plugin instanceof Nations) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		userMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new User(0, null))) {
			User object = (User) obj;
			if (!userMap.containsKey(object.getID()))
				userMap.put(object.getID(), object);
		}
	}

	@Override
	public void saveAll() {
		if(plugin instanceof Nations) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (User object : userMap.values()) {
			nations.database.save(object);
		}
	}

	@Override
	public void deleteAll() {
		if(plugin instanceof Nations) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (User object : userMap.values()) {
			nations.database.delete(object);
		}
		userMap.clear();
	}

	public User getUserByPlayer(Player player) {
		if(player == null) {
			return null;
		}
		for(User user : this.userMap.values()) {
			if(user.getName().equalsIgnoreCase(player.getName())) {
				return user;
			}
		}
		return null;
	}
	
	public boolean createUser(String name) {
		int newKey = Collections.max(this.userMap.keySet())+1;
		User newUser = new User(newKey, name);
		if(this.addUser(newUser)) {
			return true;
		}
		return false;
	}
	
	private boolean addUser(User user) {
		if(!this.userMap.containsKey(user.getID())) {
			this.userMap.put(user.getID(), user);
			return true;
		} else {
			return false;
		}
	}

	public User getUserByID(int ID) {
		return this.userMap.get(ID);
	}
	
	public ArrayList<String> getUserList() {
		ArrayList<String> userList = new ArrayList<String>();
		for(User user : this.userMap.values()) {
			userList.add(user.getName());
		}
		return userList;
	}
	
	//Use Sparingly.
	public User findUser(String string) {
		for(User user: this.userMap.values()) {
			if(user.getName().equalsIgnoreCase(string)) {
				return user;
			}
		}
		return null;
	}

}
