package org.nationsatwar.listeners;

import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.User;

public class NationsUserListener implements Listener {
	
	private PluginBase plugin;

	public NationsUserListener(PluginBase instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public synchronized void onPlayerJoin(PlayerJoinEvent event) {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		Player player = event.getPlayer();
		if(!nations.userManager.getUserList().contains(player.getName())) {
			if(player.hasPermission("nationsatwar.nations.player")) {
				User user = nations.userManager.getUserByPlayer(player);
				if(user == null) {
					if(nations.userManager.createUser(player.getName())) {
						nations.getLogger().log(Level.FINE, "Added user: " + player.getName());
					}
				}
			}
		}
		
	}

}
