package org.nationsatwar.nations;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.nationsatwar.listeners.NationsUserListener;
import org.nationsatwar.nations.commands.CommandManager;
import org.nationsatwar.nations.datasource.DataSource;
import org.nationsatwar.nations.managers.InviteManager;
import org.nationsatwar.nations.managers.NationManager;
import org.nationsatwar.nations.managers.PlotManager;
import org.nationsatwar.nations.managers.RankManager;
import org.nationsatwar.nations.managers.TownManager;
import org.nationsatwar.nations.managers.UserManager;

public class Nations extends JavaPlugin {
	public DataSource database = DataSource.getDatabase(this);

	public NationManager 	nationManager 	= new NationManager(this);
	public TownManager		townManager		= new TownManager(this);
	public PlotManager		plotManager		= new PlotManager(this);
	public UserManager		userManager		= new UserManager(this);
	public InviteManager	inviteManager	= new InviteManager(this);
	public RankManager		rankManager		= new RankManager(this);
	
	public CommandManager	command			= new CommandManager(this);
	
	public String getVersion() {
		return this.getDescription().getVersion();
	}
	
	public void onEnable() {
		
		new NationsUserListener(this);
		this.getConfig().options().copyDefaults(true);
		
		plotManager.loadAll();
		nationManager.loadAll();
		townManager.loadAll();
		rankManager.loadAll();
		inviteManager.loadAll();
		userManager.loadAll();
		
		this.getLogger().info(this.getVersion()+ " Loaded");
		
	}
	
	public void onDisable() {
		this.saveConfig();
		
		plotManager.saveAll();
		nationManager.saveAll();
		townManager.saveAll();
		rankManager.saveAll();
		inviteManager.saveAll();
		userManager.saveAll();
		database = null;
		
		this.getLogger().info(this.getVersion()+ " Unloaded");
	}
	
	public void reload(CommandSender sender) {

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return command.execute(sender, cmd, commandLabel, args);
	}
	
	public void messageAll(String message) {
		this.getServer().broadcastMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + message);
	}
	
	public void notifyAll(String message) {
		this.getServer().broadcastMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + ChatColor.DARK_AQUA + message);
	}
}