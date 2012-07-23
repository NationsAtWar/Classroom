package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Plot;
import org.nationsatwar.nations.objects.Town;
import org.nationsatwar.nations.objects.User;

public class TownCommand extends NationsCommand {

	public TownCommand(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // Town()
	
	@Override
	public void run() {
		//double nationPrice = plugin.getConfig().getDouble("nation_price");
		
		// -town || town help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/town [found|leave|founder]", "Town manipulation commands. Use /town [subcommand] for more help.");
			return;
		}
		
		// -town found
		if(command[0].equalsIgnoreCase("found")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/town found [town name]", "Forms a town.");
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
				
					Town town = nations.townManager.createTown(nation, townName);
					if(town == null ) {
						this.errorText(commandSender, "Couldn't create town.", null);
						return;
					} else {
						nations.notifyAll("Town: " + townName + " created!");
					}
					if(user != null) {							
						if(town.addMember(user, nations.rankManager.getFounderRank())) {
							this.successText(commandSender, null, "Added you as a founder of "+town.getName());
						}
						
						Plot plot = nations.plotManager.createPlot(plugin.getServer().getPlayer(user.getName()).getLocation(), nation, town);
						if(plot == null) {
							this.errorText(commandSender, "Couldn't create plot.", null);
						}
						
						if(!town.addPlot(plot)) {
							this.errorText(commandSender, "Error assigning plot to town.", null);
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
		
		// -town leave
		if(command[0].equalsIgnoreCase("leave")) {
			if(command.length == 2 && command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/town leave", "Leaves the town you're in.");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			Nation nation = nations.nationManager.getNationByUserID(user.getID());
			if(user != null) {
				if(nation == null) {
					this.errorText(commandSender, null, "You aren't in a nation!");
					return;						
				}
			}
			
			Town town = nations.townManager.getTownByUserID(user.getID());
			if(user != null) {
				if(town == null) {
					this.errorText(commandSender, null, "You aren't in a town!");
					return;
				}
			}
			
			if(town.removeMember(user)) {
				if(town.getMembers(null) == null || town.getMembers(null).isEmpty()) {
					if(nations.townManager.delete(town)) {
						nations.notifyAll("The town of " + nation.getName() + " was lost to the sands of time!");
					}
				}
				this.successText(commandSender, null, "Removed you from "+town.getName());
			} else {
				this.errorText(commandSender, "Couldn't remove you from the town.", null);
			}
			
			return;
		}
		
		// -town founder
		if(command[0].equalsIgnoreCase("founder")) {
			if(command.length == 2 && command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/town founder [player name]", "Takes your founder status away and gives it to the player specified.");
				return;
			}
			
			if(user == null) {
				this.errorText(commandSender, "No user found.", null);
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			Nation nation = nations.nationManager.getNationByUserID(user.getID());
			if(user != null) {
				if(nation == null) {
					this.errorText(commandSender, null, "You aren't in a nation!");
					return;						
				}
			}
			
			Town town = nations.townManager.getTownByUserID(user.getID());
			if(user != null) {
				if(town == null) {
					this.errorText(commandSender, null, "You aren't in a town!");
					return;
				}
			}
			
			String newFounderName = this.connectStrings(command, 2, command.length);
			User newFounder = nations.userManager.getUserByName(newFounderName);
			
			if(newFounder == null) {
				this.errorText(commandSender, null, "The user you specified is not valid.");
				return;
			}
			if(town.getID() != nations.townManager.getTownByUserID(newFounder.getID()).getID()) {
				this.errorText(commandSender, null, "The user you specified is not a member of your town.");
				return;
			}
			
			if(town.setRank(newFounder, nations.rankManager.getFounderRank()) &&
			town.setRank(user, nations.rankManager.getRecruitRank())) {
				this.successText(commandSender, "Successfully transferred ownership.", null);
				return;
			} else {
				this.errorText(commandSender, null, "Something prevented you from transferring ownership.");
				return;
			}
			
		}
	}
}
