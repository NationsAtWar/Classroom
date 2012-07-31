package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;

public class AdminCommand extends NationsCommand {

	public AdminCommand(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // AdminCommand()
	
	@Override
	public void run() {
		
		if(!commandSender.hasPermission("nationsatwar.nations.admin")) {
			this.errorText(commandSender, "You're not an admin, sorry.", null);
			return;
		}
		
		// -nadmin || nadmin help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/nadmin [nation|town]", "Admin manipulation commands. Use '/nadmin [subcommand] help' for more help.");
			return;
		}
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		// -nadmin nation
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/nadmin nation [disband]", "Nation Manipulation.");
				return;
			}
			
			if(command[1].equalsIgnoreCase("disband")) {
				if(command.length == 2 || command[2].equalsIgnoreCase("help")) {
					this.helpText(commandSender, "i.e. '/nadmin nation disband [nation name]", "Forcefully disbands a nation.");
					return;
				}
				
				String nationName = this.connectStrings(command, 2, command.length);
				Nation cmdnation = null;
				
				for(Nation nation : nations.nationManager.getNations().values()) {
					if(nation.getName().equalsIgnoreCase(nationName)) {
						cmdnation = nation;
					}
				}	
				
				if(cmdnation == null) {
					this.errorText(commandSender, "A Nation with that name does not exist", null);
					return;
				}
				
				
				if(nations.nationManager.delete(cmdnation)) {
					nations.notifyAll("The nation of " + cmdnation.getName() + " was lost to the sands of time!");
					this.successText(commandSender, "Successfully disbanded " + cmdnation.getName(), null);
					return;
				}
			}
		}

	}
}
