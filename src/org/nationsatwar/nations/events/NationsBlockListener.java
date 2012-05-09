package org.nationsatwar.nations.events;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Listener class for all block activity relating to 'Nations at War'
 * 
 * @author Aculem, Shizukesa
 * 
 */
public class NationsBlockListener extends BlockListener {
	
	private static TownManager plugin;

	public NationsBlockListener(TownManager instance) {
		
		plugin = instance;
	}

	public synchronized boolean canInteract(Plot plot, User user) {
		// Checks if plot is owned (True if no)
		if (plot != null) {

			// Checks if plot is owned by event.user
			if (plot.getNation().equalsIgnoreCase(user.getName())) {
					return true;
			}
			
			//Check if the town is locked
			Town town = plugin.townManager.getTownAtPlot(plot);
			if(town != null && !town.locked) {
				return true;
			}
			
			// Checks if nation has upkeep debt (True if yes)
			if (user.getUpkeepDebt() > 0)
				return true;
			
		}
		// Plot is not owned.
		else
			return true;

		return false;
	}

	@Override
	public synchronized void onBlockBreak(BlockBreakEvent event) {
		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotByLocation(plugin.util.getLocationKey(event.getBlock().getLocation()));

		if (!canInteract(plot, user)) {
			event.setCancelled(true);
			return;
		}
	}

	@Override
	public synchronized void onBlockDamage(BlockDamageEvent event) {

		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlot(plugin.util.getLocationKey(event.getBlock().getLocation()));

		if (!canInteract(plot, user))
			event.setCancelled(true);
	}

	@Override
	public synchronized void onBlockPlace(BlockPlaceEvent event) {

		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlot(plugin.util.getLocationKey(event.getBlock().getLocation()));

		if (!canInteract(plot, user))
			event.setCancelled(true);
	}
}
