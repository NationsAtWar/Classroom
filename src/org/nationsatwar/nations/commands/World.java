package org.nationsatwar.nations.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;

public class World extends NationsCommand {

	protected World(CommandSender commandSender, String[] command) {
		super(commandSender, command);
	}

	@Override
	public void run() {
		
		if(command.length == 1 && command[1].equalsIgnoreCase("help")) {
			this.helpText(commandSender, null, "Shows you a list of nations in the world.");
			return;
		}
		
		this.successText(commandSender, "["+plugin.getName() + "] Current Nations", null);
		
		int pageNumber = 1;
		if(command.length > 1) {
			pageNumber = Integer.parseInt(command[1]);
		}
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		ArrayList<String> nationList = nations.nationManager.getNationList();
		int nationAmount = nationList.size();
		int pageAmount = (int) (nationAmount / 10) + 1;
		
		if(pageNumber > pageAmount) {
			pageNumber = pageAmount;
		}
		
		this.successText(commandSender, "Page " + pageNumber + " of " + pageAmount, null);
		
		int nationNumber = (pageNumber - 1)*10;
		while(nationNumber<nationNumber + 10) {
			this.successText(commandSender, null, nationList.get(nationNumber));
			nationNumber++;
		}
	}

}
