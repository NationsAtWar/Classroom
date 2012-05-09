package org.nationsatwar.nations.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class NationsUserListener extends PlayerListener {
	
	private TownManager plugin;

	PlayerRespawnEvent newSpawn;

	public NationsUserListener(TownManager instance) {

		plugin = instance;
	}

	public synchronized void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		String input = "";
		if (event.getMessage().length() > 4)
			input = event.getMessage().substring(0, 5);
		if (input.equalsIgnoreCase("/tell"))
			event.setCancelled(true);
	}

	@Override
	public synchronized void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		String name = plugin.util.getPlayerName(player);

		if (plugin.userManager.exists(name))
			plugin.userManager.setupUser(player);
		else {
			if (Boolean.parseBoolean(plugin.config.get("auto_registration"))) {
				plugin.userManager.registerUser(player);
				plugin.userManager.setupUser(player);
			} else {
				player.sendMessage("*** You are not yet registered on the server ***");
			}
		}
	}

	@Override
	public synchronized void onPlayerInteract(PlayerInteractEvent event) {
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && 
				event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		Block block = event.getClickedBlock();

		if (block != null) {
			
			User user = plugin.userManager.getUser(event.getPlayer());
			Plot plot = plugin.plotManager.getPlot(plugin.util.getLocationKey(block.getLocation()));
			
			if (plot == null)
				return;

			Town town = plugin.townManager.getTownAtPlot(plot);
			
			if (town == null)
				return;
			
			if (!plugin.blockListener.canInteract(plot, user)) {
				
				if (town.locked)
					event.setCancelled(true);
				else if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER || 
							block.getType() == Material.FURNACE || block.getType() == Material.STORAGE_MINECART || 
							block.getType() == Material.JUKEBOX || block.getType() == Material.NOTE_BLOCK)
					event.setCancelled(true);
			}
		}
	}

	@Override
	public synchronized void onPlayerQuit(PlayerQuitEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
		user.setOnline(false);
		plugin.database.save(user);
	}

	public synchronized void onPlayerRespawn(PlayerRespawnEvent event) {
		
		RespawnThread respawnTime = new RespawnThread(plugin, event.getPlayer(), 500);
		respawnTime.start();
	}

	@Override
	public synchronized void onPlayerKick(PlayerKickEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
		user.setOnline(false);
		plugin.database.save(user);
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
			
		if (user != null)
			plugin.userManager.updateLocation(user);
	}
}