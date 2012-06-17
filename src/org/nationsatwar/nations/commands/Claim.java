package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/claim", "Claims the plot of land where you are currently standing.");
			return;
		}
	
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		if(!(commandSender instanceof Player)) {
			this.errorText(commandSender, "Sorry, console can't claim.", null);
			return;
		}
		Player player = (Player) commandSender;
		//User user = nations.userManager.getUserByPlayer(player);
		
		Nation nation = nations.nationManager.getNationByUsername(commandSender.getName());
		if(nation == null) {
			this.errorText(commandSender, "You're not in a nation", null);
			return;
		}
		
		Town town = nations.townManager.getTownByUsername(commandSender.getName());
		if(town == null) {
			this.errorText(commandSender, "You're not in a town", null);
			return;			
		}
		
		Plot plot = new Plot(player.getLocation());
		
		if(nations.plotManager.isPlot(plot)) {
			this.errorText(commandSender, "This plot is already owned.", null);
			return;			
		}
		
		if(nations.plotManager.addPlot(plot) && nations.townManager.addPlotToTown(plot, town)) {
			nations.plotManager.showBoundaries(plot);
			this.successText(commandSender, null, "You now own this plot.");
		}
	}
}

