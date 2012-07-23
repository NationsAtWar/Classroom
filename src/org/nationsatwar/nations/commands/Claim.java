package org.nationsatwar.nations.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;
import org.bukkit.World;

public class Claim extends NationsCommand {

	protected Claim(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	}

	@Override
	public void run() {
		// -claim help
		if(command.length == 1 && command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/claim", "Claims the plot of land where you are currently standing.");
			return;
		}
	
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		if(user == null) {
			this.errorText(commandSender, "Sorry, console can't claim.", null);
			return;
		}
		
		Nation nation = nations.nationManager.getNationByUserID(user.getID());
		if(nation == null) {
			this.errorText(commandSender, "You're not in a nation", null);
			return;
		}
		
		Town town = nations.townManager.getTownByUserID(user.getID());
		if(town == null) {
			this.errorText(commandSender, "You're not in a town", null);
			return;			
		}
		
		Location userLoc = plugin.getServer().getPlayer(user.getName()).getLocation();
		
		Plot plot = nations.plotManager.getPlotByLocation(userLoc);
		
		if(plot != null) {
			this.errorText(commandSender, "This plot is already owned.", null);
			return;			
		}
		
		//Dominant Town check, may remove?
		Town domTown = this.getDominantTown(userLoc);
		if(domTown == null) {
			this.errorText(commandSender, "You can only claim plots next to existing towns.", null);
			return;	
		}
		if(domTown.getID() != town.getID()) {
			this.errorText(commandSender, "The nearby town of " + domTown.getName() + " is too dominant, you cannot claim this plot.", null);
			return;	
		}
		
		//Doughnut protection
		int connPlots = this.connectedPlots(town.getID(), userLoc.getWorld(), userLoc.getChunk().getX(), userLoc.getChunk().getZ(), userLoc.getChunk().getX(), userLoc.getChunk().getZ());
		int surrPlots = this.surroundingPlots(town.getID(), userLoc.getWorld(), userLoc.getChunk().getX(), userLoc.getChunk().getZ());
		if(connPlots < surrPlots) {
			this.errorText(commandSender, "You may not claim this plot as it results in a poor configuration.", null);
			return;	
		}
		
		plot = null;
		plot = nations.plotManager.createPlot(userLoc, nation, town);
		if(plot == null) {
			this.errorText(commandSender, "Couldn't create plot.", null);
			return;			
		}
		
		if(!town.addPlot(plot)) {
			nations.plotManager.delete(plot);
			this.errorText(commandSender, "Error adding plot to town.", null);
			return;				
		}
		
		nations.plotManager.showBoundaries(plot);
		this.successText(commandSender, null, "You now own this plot.");
	}
	
	public int connectedPlots(int townID, World world, int origX, int origZ, int plotX, int plotZ) {
		int found = 0;
		if(!(plugin instanceof Nations)) {
			return found;
		}
		Nations nations = (Nations) plugin;
		
		//We're checking each plot in a 3x3 for existence
		Plot checkPlot = nations.plotManager.getPlotByChunkCoords(world, plotX, plotZ);
		
		//Only check within the original 3x3 bounds.
		if(plotX > origX +1 || plotX < origX -1) {
			return found;
		}
		if(plotZ > origZ + 1 || plotZ < origZ - 1) {
			return found;
		}
		
		//if there's a plot and the ID is the same as our original town, this plot is connected.
		if(checkPlot != null && nations.townManager.getTownByID(checkPlot.getTownID()).getID() == townID) {
			found++;
		}
		
		//recursively check NESW plots (within the 3x3 via check above), and add them to the total
		found += this.connectedPlots(townID, world, origX, origZ, plotX+1, plotZ);
		found += this.connectedPlots(townID, world, origX, origZ, plotX-1, plotZ);
		found += this.connectedPlots(townID, world, origX, origZ, plotX, plotZ+1);
		found += this.connectedPlots(townID, world, origX, origZ, plotX, plotZ-1);
		
		return found;
	}
	
	public int surroundingPlots(int townID, World world, int plotX, int plotZ) {
		int found = 0;
		if(!(plugin instanceof Nations)) {
			return found;
		}
		Nations nations = (Nations) plugin;
		
		//along x from -1 to 1 from origin
		for(int xAxis = -1; xAxis <= 1; xAxis++) {
			//along z from -1 to 1 from origin
			for(int zAxis = -1; zAxis <= 1; zAxis++) {
				//if the calculated plot isn't the original... (might as well save the calc, as this should be verified empty)
				if(plotX != plotX + xAxis && plotZ != plotZ + zAxis) {
					//get the plot
					Plot checkPlot = nations.plotManager.getPlotByChunkCoords(world, plotX+xAxis, plotZ+zAxis);
					//if there is a plot
					if(checkPlot != null) {
						//and the plot's town id matches that of the town we're adding it to, increment.
						if(nations.townManager.getTownByID(checkPlot.getTownID()).getID() == townID) {
							found++;
						}
					}
				}
			}
		}
		
		return found;
	}
	
	public Town getDominantTown(Location loc) {
		//Ehh, not sure if i like this here, but let's see.how this plays out.
		if(!(plugin instanceof Nations)) {
			return null;
		}
		Nations nations = (Nations) plugin;
		
		int numPlots = 0;
		
		int plotX = loc.getChunk().getX();
		int plotZ = loc.getChunk().getZ();
		
		Plot northPlot = nations.plotManager.getPlotByChunkCoords(loc.getWorld(), plotX+1, plotZ);
		Plot eastPlot = nations.plotManager.getPlotByChunkCoords(loc.getWorld(), plotX, plotZ+1);
		Plot southPlot = nations.plotManager.getPlotByChunkCoords(loc.getWorld(), plotX-1, plotZ);
		Plot westPlot = nations.plotManager.getPlotByChunkCoords(loc.getWorld(), plotX, plotZ-1);
		
		Town dominantTown = null;
		
		if(northPlot != null) {
			Town town = nations.townManager.getTownByID(northPlot.getTownID());
			if(town != null && (town.getPlots().size() > numPlots || dominantTown == null)) {
				dominantTown = town;
				numPlots = town.getPlots().size();
			}
		}
		if(eastPlot != null) {
			Town town = nations.townManager.getTownByID(eastPlot.getTownID());
			if(town != null && (town.getPlots().size() > numPlots || dominantTown == null)) {
				dominantTown = town;
				numPlots = town.getPlots().size();
			}
		}
		if(southPlot != null) {
			Town town = nations.townManager.getTownByID(southPlot.getTownID());
			if(town != null && (town.getPlots().size() > numPlots || dominantTown == null)) {
				dominantTown = town;
				numPlots = town.getPlots().size();
			}
		}
		if(westPlot != null) {
			Town town = nations.townManager.getTownByID(westPlot.getTownID());
			if(town != null && (town.getPlots().size() > numPlots || dominantTown == null)) {
				dominantTown = town;
				numPlots = town.getPlots().size();
			}
		}
		
		return dominantTown;
	}
}

