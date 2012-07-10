package org.nationsatwar.nations.commands;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Town;

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
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
				
		String youText = "";
		if(user != null) {
			Nation nation = nations.nationManager.getNationByUserID(user.getID());
			Town town = nations.townManager.getTownByUserID(user.getID());
			if(nation != null) {
				youText += "You are in the nation of "+nation.getName()+". ";
			}
			if(town != null) {
				youText += "You are in the town of "+town.getName()+". ";
			}
			if(nation == null && town == null) {
				youText += "You are in no town or nation.";
			}
		}
		this.successText(commandSender, null, youText);
		this.successText(commandSender, "["+plugin.getName() + "] Current Nations", null);
		
		int pageNumber = 1;
		if(command.length > 0) {
			pageNumber = Integer.parseInt(command[1]);
		}
		

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
