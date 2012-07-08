package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;

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
			} else {
				this.errorText(commandSender, "Couldn't remove you from the nation.", null);
			}
			
			return;
		}
	}
}
