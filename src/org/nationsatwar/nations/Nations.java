package org.nationsatwar.nations;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.nationsatwar.nations.managers.OrganizationManager;

public class Nations extends JavaPlugin {
	
	public Map<String, OrganizationManager> orgManagerMap = new HashMap<String, OrganizationManager>();
	
	public void onEnable() {
		
		this.getConfig().options().copyDefaults(true);
		
		for (String key : this.getConfig().getConfigurationSection("organizations").getKeys(false)) {
			
			int level = this.getConfig().getInt("organizations." + key + ".level");
			boolean hasPlots = this.getConfig().getBoolean("organizations." + key + ".hasplots", false);
			boolean removeEmpty = this.getConfig().getBoolean("organizations." + key + ".removeEmpty", true);
			
			orgManagerMap.put(key, new OrganizationManager(this, key, level, hasPlots, removeEmpty));
		}
		
		for (OrganizationManager o : orgManagerMap.values())
			o.loadAll();
	}
	
	public void onDisable() {
		this.saveConfig();
	}
	
	public OrganizationManager getOrgManager(String orgType) {
		return this.orgManagerMap.get(orgType);
	}
	
	public void messageAll(String message) {
		this.getServer().broadcastMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + message);
	}
	
	public void notifyAll(String message) {
		this.getServer().broadcastMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + ChatColor.DARK_AQUA + message);
	}
	
	public void sendMessage(CommandSender to, String string) {
		to.sendMessage(ChatColor.AQUA + "["+this.getName()+"] " + ChatColor.RESET +string);
	}
	
	public void sendMessage(CommandSender to, String[] string) {
		to.sendMessage(ChatColor.AQUA + "["+this.getName()+"] "  + ChatColor.RESET + string);
	}


}