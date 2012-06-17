package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;

public class Found extends NationsCommand {

	public Found(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // Found()
	
	@Override
	public void run() {
		//double nationPrice = plugin.getConfig().getDouble("nation_price");
		
		// -found || found help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/found [town|nation|alliance]", "Forms a town/nation/alliance. Use /found [subcommand] for more help.");
			return;
		}
		
		// -found nation
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/found nation [nation name]", "Forms a nation.");
				return;
			}
			
			String nationName = this.connectStrings(command, 1, command.length);
			
			if (nationName.length() > 30) {
				this.errorText(commandSender, "Name too long:", "Nation names can only be 32 characters long. Blame iConomy.");
				return;
			}
			
			int aposAmount = 0;
			for (int i=0; i<nationName.length(); i++) {
				if (nationName.charAt(i) == '\'')
					aposAmount += 1;
				if (!Character.isLetterOrDigit(nationName.charAt(i)) && nationName.charAt(i) != ' ' && nationName.charAt(i) != '\'') {
					this.errorText(commandSender, "Invalid Nation Name:", "Letters or numbers only.");
					return;
				}
			}
			
			if (aposAmount > 1) {
				this.errorText(commandSender, "Too many apostrophes:", ":/");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			if(!nations.nationManager.exists(nationName)) {
				if(nations.nationManager.getNationByUsername(commandSender.getName()) != null) {
					
						Nation nation = new Nation();
						nation.setName(nationName);

						if(commandSender instanceof Player) {
							if(nations.nationManager.addFounder(nation, commandSender.getName())) {
								this.successText(commandSender, null, "Added you as a founder of "+nation.getName());
							}
						}
						
						if(nations.nationManager.addNation(nation)) {
							nations.notifyAll(nationName + " created!");
						} else {
							this.errorText(commandSender, "Couldn't create nation.", null);
						}
				} else {
					this.errorText(commandSender, null, "You are already a member of a nation. You must leave that nation " +
							"before you can found a new one!");
					return;
				}
			} else {
				this.errorText(commandSender, "A Nation with that name already exists!", null);
				return;
			}
		}
			
		if(command[0].equalsIgnoreCase("town")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/found town [town name]", "Forms a town.");
				return;
			}
			
			String townName = this.connectStrings(command, 1, command.length);
			
			if (townName.length() > 30) {
				this.errorText(commandSender, "Name too long:", "Town names can only be 32 characters long. Blame iConomy.");
				return;
			}
			
			int aposAmount = 0;
			for (int i=0; i<townName.length(); i++) {
				if (townName.charAt(i) == '\'')
					aposAmount += 1;
				if (!Character.isLetterOrDigit(townName.charAt(i)) && townName.charAt(i) != ' ' && townName.charAt(i) != '\'') {
					this.errorText(commandSender, "Invalid Town Name:", "Letters or numbers only.");
					return;
				}
			}
			
			if (aposAmount > 1) {
				this.errorText(commandSender, "Too many apostrophes:", ":/");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			Nation nation = nations.nationManager.getNationByUsername(commandSender.getName());
			
			if(!nations.townManager.exists(townName)) {
				if(nation != null) {
					if(nations.townManager.getTownByUsername(commandSender.getName()) == null) {
					
						Town town = new Town();
						town.setName(townName);

						if(commandSender instanceof Player) {
							if(nations.townManager.addFounder(town, commandSender.getName())) {
								this.successText(commandSender, null, "Added you as a founder of "+town.getName());
							}
							Plot plot = new Plot(((Player) commandSender).getLocation());
							if(nations.plotManager.addPlot(plot) && nations.townManager.addPlotToTown(plot, town)) {
								nations.plotManager.showBoundaries(plot);
								this.successText(commandSender, null, "You now own this plot.");
							}
						}
						
						if(nations.townManager.addTown(town) && nations.nationManager.addTownToNation(town, nation)) {
							nations.notifyAll(townName + " created!");
						} else {
							this.errorText(commandSender, "Couldn't create town.", null);
						}
					} else {
						this.errorText(commandSender, null, "You are already a member of a town. You must leave that town to form a new one.");
						return;						
					}
				} else {
					this.errorText(commandSender, null, "You are not a member of a nation. You can't form a town without a nation.");
					return;
				}
			} else {
				this.errorText(commandSender, "A Town with that name already exists!", null);
				return;
			}
		}
	}
}
