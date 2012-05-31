package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;

public class Found extends NationsCommand {

	public Found(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // Found()
	
	@Override
	public void run() {
		//double nationPrice = plugin.getConfig().getDouble("nation_price");
		
		// -found || found help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/found [nation] [nation name]", "Forms a nation.");
			return;
		}
		
		// -found nation
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/found [nation] [nation name]", "Forms a nation.");
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
				if(!nations.userManager.isInNation(commandSender.getName())) {
					
						Nation nation = new Nation();
						nation.setName(nationName);
						if(commandSender instanceof Player) {
							nation.addFounder(commandSender.getName());
						}
						plugin.getLogger().info("creating nation: "+nation.getName());
						
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
	}
}
