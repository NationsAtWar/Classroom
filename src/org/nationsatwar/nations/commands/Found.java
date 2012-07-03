package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
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
			
			for(Nation nation : nations.nationManager.getNations().values()) {
				if(nation.getName().equalsIgnoreCase(nationName)) {
					this.errorText(commandSender, "A Nation with that name already exists!", null);
					return;
				}
			}
			
			if(user != null) {
				if(nations.nationManager.getNationByUserID(user.getID()) != null) {
					this.errorText(commandSender, null, "You are already a member of a nation. You must leave that nation " +
							"before you can found a new one!");
					return;						
				}
			}
			Nation nation = nations.nationManager.createNation(nationName);
			if(nation != null) {
				nations.notifyAll(nationName + " created!");
			} else {
				this.errorText(commandSender, "Couldn't create nation.", null);
				return;
			}
			if(user != null) {
				if(nation.addMember(user, nations.rankManager.getFounderRank())) {
					this.successText(commandSender, null, "Added you as a founder of "+nation.getName());
				}
			}
			return;
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
			
			if(user == null) {
				this.errorText(commandSender, "Sorry, console can't claim.", null);
				return;
			}
			
			Nation nation = nations.nationManager.getNationByUserID(user.getID());
			
			for(Town town : nations.townManager.getTowns().values()) {
				if(town.getName().equalsIgnoreCase(townName)) {
					this.errorText(commandSender, "A Town with that name already exists!", null);
					return;
				}
			}
			
			if(nation != null) {
				if(nations.townManager.getTownByUserID(user.getID()) == null) {
				
					Town town = nations.townManager.createTown(townName);
					if(town == null ) {
						this.errorText(commandSender, "Couldn't create town.", null);
						return;
					} else {
						nations.notifyAll(townName + " created!");
					}
					if(user != null) {							
						if(town.addMember(user, nations.rankManager.getFounderRank())) {
							this.successText(commandSender, null, "Added you as a founder of "+town.getName());
						}
						
						Plot plot = nations.plotManager.createPlot(plugin.getServer().getPlayer(user.getName()).getLocation());
						if(plot == null) {
							this.errorText(commandSender, "Couldn't create plot.", null);
						}
						
						if(!town.addPlot(plot)) {
							this.errorText(commandSender, "Error assigning plot to town.", null);
						}
						if(!nation.addTown(town)) {
							this.errorText(commandSender, "Error assigning town to nation.", null);
						}
						nations.plotManager.showBoundaries(plot);
						this.successText(commandSender, null, "You now own this plot.");
					}
					return;
				} else {
					this.errorText(commandSender, null, "You are already a member of a town. You must leave that town to form a new one.");
					return;						
				}
			} else {
				this.errorText(commandSender, null, "You are not a member of a nation. You can't form a town without a nation.");
				return;
			}
		}
	}
}
