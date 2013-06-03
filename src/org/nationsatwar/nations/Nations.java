package org.nationsatwar.nations;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.nationsatwar.nations.datasource.Database;
import org.nationsatwar.nations.managers.OrganizationManager;

public class Nations extends JavaPlugin {
	public Database database;
	public OrganizationManager orgManager;
	
	public void onEnable() {
		this.database = new Database(this);
		
		this.orgManager = new OrganizationManager(this);

		this.getConfig().options().copyDefaults(true);
	}
	
	public void onDisable() {
		this.saveConfig();
		database = null;
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