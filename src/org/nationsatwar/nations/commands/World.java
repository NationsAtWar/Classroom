package org.nationsatwar.nations.commands;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;

public class World extends NationsCommand {

	protected World(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	}

	@Override
	public void run() {
		
		if(command.length > 0 && command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, null, "Shows you a list of nations in the world.");
			return;
		}
		
		this.successText(commandSender, "["+plugin.getName() + "] Current Nations", null);
		
		int pageNumber = 1;
		if(command.length > 0) {
			pageNumber = Integer.parseInt(command[1]);
		}
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		ArrayList<Nation> nationList = new ArrayList<Nation>(nations.nationManager.getNations().values());
		if(nationList == null || nationList.size() < 1) {
			this.errorText(commandSender, null, "No nations yet.");
			return;
		}
		int nationAmount = nationList.size();
		int pageAmount = ((int) (nationAmount / 10)) + 1;
		
		if(pageNumber > pageAmount) {
			pageNumber = pageAmount;
		}
		
		this.successText(commandSender, "Page " + pageNumber + " of " + pageAmount, null);
		
		int nationNumber = (pageNumber - 1)*10;
		int initialNationNumber = nationNumber;
		while(nationNumber<initialNationNumber + 10 && nationNumber < nationList.size()) {
			String nationName = nationList.get(nationNumber).getName();
			this.successText(commandSender, null, nationName);
			nationNumber++;
		}
	}

}
