package org.nationsatwar.nations.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.User;

public class UserManager extends NationsManagement {
	private HashMap<Integer, User> userMap = new HashMap<Integer, User>();

	public UserManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(!(plugin instanceof Nations)) {
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
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (User object : userMap.values()) {
			nations.database.save(object);
		}
	}

	@Override
	public void deleteAll() {
		if(!(plugin instanceof Nations)) {
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
		int newKey = 0; 
		try {
			newKey = Collections.max(userMap.keySet())+1;
		} catch(NoSuchElementException e) {
			
		}
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

	public boolean updateLocation(User user) {
		if(!(plugin instanceof Nations)) {
			return false;
		}
		Nations nations = (Nations) plugin;
		
		Location playerLoc = nations.getServer().getPlayerExact(user.getName()).getLocation();
		String locKey = nations.plotManager.getLocationKey(playerLoc);
		
		if (user.getLocationKey() == null || !user.getLocationKey().equals(locKey)) {
			this.updateLocationDescription(user, playerLoc);
			user.setLocationKey(locKey);
			return true;
		}

		return false;
	}
	
	public synchronized boolean updateLocationDescription(User user, Location loc) {
		if(!(plugin instanceof Nations)) {
			return false;
		}
		Nations nations = (Nations) plugin;
		
		Plot plot = nations.plotManager.getPlotByLocation(loc);
		
		if (plot != null) {
			String locdesc = plot.getLocationDescription();
			if (!user.getLocationDescription().equals(locdesc)) {
				user.setLocationDescription(locdesc);
				this.message(user, ChatColor.AQUA + "[Entering] " + locdesc);
				return true;
			}
		} else {
			if (user.getLocationDescription() != "") {
				this.message(user, ChatColor.AQUA + "[Leaving] " + user.getLocationDescription());
				user.setLocationDescription("");
				return true;
			}
		}
		return false;
	}

	private void message(User user, String string) {
		Player player = plugin.getServer().getPlayerExact(user.getName());
		player.sendMessage(string);
	}

}
