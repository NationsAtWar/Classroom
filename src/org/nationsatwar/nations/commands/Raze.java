package org.nationsatwar.nations.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;

public class Raze extends NationsCommand {

	protected Raze(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	}

	@Override
	public void run() {
		// -claim help
		if(command.length == 1 && command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/raze", "Razes the plot of land where you are currently standing.");
			return;
		}
	
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		if(user == null) {
			this.errorText(commandSender, "Sorry, console can't raze.", null);
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
		
		if(plot == null) {
			this.errorText(commandSender, "This plot is not owned.", null);
			return;			
		}
		
		if(nations.townManager.getTownByID(plot.getTownID()).removePlot(plot) && nations.plotManager.delete(plot)) {
			this.successText(commandSender, null, "Plot razed! You no longer own it.");
			return;
		}
		this.errorText(commandSender, null, "Couldn't raze plot.");
		return;
	}
}

