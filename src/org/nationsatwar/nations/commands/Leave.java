package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Town;

public class Leave extends NationsCommand {

	public Leave(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // Leave()
	
	@Override
	public void run() {
		//double nationPrice = plugin.getConfig().getDouble("nation_price");
		
		// -leave || leave help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/leave [town|nation|alliance]", "Leaves your respective town/nation/alliance.");
			return;
		}
		
		// -leave nation
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 2 && command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/leave nation", "Leaves the nation you're in.");
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
			
			if(nation.removeMember(user)) {
				this.successText(commandSender, null, "Removed you from "+nation.getName());
				if(nation.getMembers(null) == null || nation.getMembers(null).isEmpty()) {
					if(nations.nationManager.delete(nation)) {
						nations.notifyAll("The nation of " + nation.getName() + " was lost to the sands of time!");
					}
				}
			} else {
				this.errorText(commandSender, "Couldn't remove you from the nation.", null);
			}
			
			return;
		}
		
		// -leave town
		if(command[0].equalsIgnoreCase("town")) {
			if(command.length == 2 && command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/leave town", "Leaves the town you're in.");
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
	}
}
