package org.nationsatwar.nations.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;
import org.nationsatwar.nations.objects.User;

public class NationsBlockListener implements Listener {
	
	private Nations plugin;

	public NationsBlockListener(Nations instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public synchronized boolean canInteract(Plot plot, User user) {

		if(plot == null) {
			//if there's no plot here, canInteract is always true.
			return true;
		}
		
		if(user == null) {
			//if there's no user here, I'm not sure what's breaking it. Let it through.
			return true;
		}
		
		Nation nation = plugin.nationManager.getNationByUserID(user.getID());
		if(nation == null) {
			//since there's a plot, see if the user is in a nation. if not, they can't touch anything.
			return false;
		}
		
		Town town = plugin.townManager.getTownByUserID(user.getID());
		
		Town plotTown = plugin.townManager.getTownByID(plot.getTownID());
		Nation plotNation = null;
		if(plotTown != null) {
			plotNation = plugin.nationManager.getNationByID(plotTown.getID());
		}
		
		//check these when adding locks
		/*if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER || 
							block.getType() == Material.FURNACE || block.getType() == Material.STORAGE_MINECART || 
							block.getType() == Material.JUKEBOX || block.getType() == Material.NOTE_BLOCK)*/
		
		//the user is in the plot's town
		if(town != null) {
			if(plotTown.getID() == town.getID()) {
				return true;
			}
		}
		
		//the user is in the plot's nation, use until finer tuned permissions.
		if(plotNation.getID() == nation.getID()) {
			return true;
		}
		
		return false;
	}
	
	@EventHandler
	public synchronized void onBlockDamage(BlockDamageEvent event) {
		User user = plugin.userManager.getUserByPlayer(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotByLocation(event.getBlock().getLocation());
		if(!this.canInteract(plot, user)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public synchronized void onBlockBreak(BlockBreakEvent event) {
		User user = plugin.userManager.getUserByPlayer(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotByLocation(event.getBlock().getLocation());
		if(!this.canInteract(plot, user)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public synchronized void onBlockPlace(BlockPlaceEvent event) {
		User user = plugin.userManager.getUserByPlayer(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotByLocation(event.getBlock().getLocation());
		if(!this.canInteract(plot, user)) {
			event.setCancelled(true);
		}
		
		//Don't allow chest placement in unowned areas.
		if(event.getBlockPlaced().getType() == Material.BED || 
				event.getBlockPlaced().getType() == Material.BED_BLOCK) {
			if(plot == null) { //Interaction and therefore plot ownership is implied by the above check.
				event.getPlayer().sendMessage(ChatColor.RED + "You can only place beds in your plots.");
				event.setCancelled(true);
			}
			
			if(plugin instanceof Nations) {
				return;
			}
			Nations nations = (Nations) plugin;
			Town newTown = nations.townManager.getTownByID(plot.getTownID());
			Town oldTown = nations.townManager.getTownByUserID(user.getID());
			if(newTown.getID() != oldTown.getID()) {
				//Moving towns
				oldTown.removeMember(user);
				newTown.addMember(user, nations.rankManager.getRecruitRank());
			}
		}
	}
	
	@EventHandler
	public synchronized void onPlayerInteract(PlayerInteractEvent event) {
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		Block block = event.getClickedBlock();

		if (block != null) {
			
			User user = plugin.userManager.getUserByPlayer(event.getPlayer());
			Plot plot = plugin.plotManager.getPlotByLocation(block.getLocation());
	
			if(!this.canInteract(plot, user)) {
				event.setCancelled(true);
				return;
			}
		}
		return;
	}

}
