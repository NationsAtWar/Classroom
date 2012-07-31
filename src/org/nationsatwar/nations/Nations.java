package org.nationsatwar.nations;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.nationsatwar.nations.commands.CommandManager;
import org.nationsatwar.nations.datasource.DataSource;
import org.nationsatwar.nations.datasource.MySQL;
import org.nationsatwar.nations.listeners.NationsBlockListener;
import org.nationsatwar.nations.listeners.NationsUserListener;
import org.nationsatwar.nations.managers.AllianceManager;
import org.nationsatwar.nations.managers.InviteManager;
import org.nationsatwar.nations.managers.NationManager;
import org.nationsatwar.nations.managers.PlotManager;
import org.nationsatwar.nations.managers.RankManager;
import org.nationsatwar.nations.managers.TownManager;
import org.nationsatwar.nations.managers.UserManager;
import org.nationsatwar.nations.threads.ReloadThread;

public class Nations extends JavaPlugin {
	public DataSource database;

	public NationManager 	nationManager 	= new NationManager(this);
	public TownManager		townManager		= new TownManager(this);
	public AllianceManager 	allianceManager 	= new AllianceManager(this);
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
		new NationsBlockListener(this);
		
		this.getConfig().options().copyDefaults(true);
		
		if(this.getConfig().getBoolean("datasource.use_mysql"))
			database = new MySQL(this);
		
		this.load();
		
		this.getLogger().info(this.getVersion()+ " Loaded");
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new ReloadThread(this), 20L * 60 * 60 * 4, 20L * 60 * 60 * 4);
	}
	
	public void onDisable() {
		this.saveConfig();
		
		this.save();
		database = null;
		
		this.getLogger().info(this.getVersion()+ " Unloaded");
	}
	
	public void load() {
		plotManager.loadAll();
		nationManager.loadAll();
		townManager.loadAll();
		rankManager.loadAll();
		inviteManager.loadAll();
		userManager.loadAll();		
	}
	
	public void save() {
		plotManager.saveAll();
		nationManager.saveAll();
		townManager.saveAll();
		rankManager.saveAll();
		inviteManager.saveAll();
		userManager.saveAll();
	}
	
	public void reload(CommandSender sender) {
		if(sender != null) {
			sender.sendMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + "Reloading Nations.");
			this.reloadConfig();
		}
		
		this.save();
		this.load();
		
		if(sender != null) {
			sender.sendMessage(ChatColor.DARK_RED + "["+this.getName()+"]: " + "Reloaded.");
		}	
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