package org.nationsatwar.nations.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;

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
		
		plot = null;
		plot = nations.plotManager.createPlot(userLoc, nation, town);
		if(plot == null) {
			this.errorText(commandSender, "Couldn't create plot.", null);
			return;			
		}
		
		if(!town.addPlot(plot)) {
			this.errorText(commandSender, "Error adding plot to town.", null);
			return;				
		}
		
		nations.plotManager.showBoundaries(plot);
		this.successText(commandSender, null, "You now own this plot.");
	}
}

