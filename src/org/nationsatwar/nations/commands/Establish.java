package org.nationsatwar.nations.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Establish extends NationsCommand {
	
	public Establish(CommandSender commandSender, String[] command) {
		super(commandSender, command);
	} // Establish()
	
	@Override
	public void run() {
		
		// -establish || -establish help
		if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
			this.helpText(commandSender);
			return;
		}
		
		// -establish [String: newName]
		String newName = "";
		
		if (command.length > 1)
			newName = this.connectStrings(command, 1, command.length);
		
		establishCommand(newName);
		return;
	}
	
	/**
	 * Sends the user the help text for Jobs.
	 * @param commandSender The person to message.
	 */
	@Override
	public void helpText(CommandSender commandSender) {
		commandSender.sendMessage(ChatColor.DARK_RED + "[TownManager]" + ChatColor.DARK_AQUA + " -=[ESTABLISH]=-");
		commandSender.sendMessage(ChatColor.GREEN + "Usage: '/tm establish [town name]");
		commandSender.sendMessage(ChatColor.YELLOW + "Establishes a new town for your nation.");
	}

	/**
	 * This establishes the first plot of a new town.
	 * 
	 * @param newName The new name you want the town to be.
	 */
	public void establishCommand(String newName) {
		
		if (newName.equalsIgnoreCase("") || newName.equalsIgnoreCase("Help")) {
			commandSender.message(Color.DarkRed() + "[TownManager]" + Color.DarkAqua() + " -=[TOWN - ESTABLISH]=-");
			commandSender.message(Color.DarkRed() + "[TownManager]" + Color.Green() + " e.g. '/tm town establish [name]'");
			commandSender.message(Color.Yellow() + "Establishes the first plot of your new town, as well as giving it a [name].");
		}
		
		if (!plugin.util.validName(commandSender, newName))
			return;
		
		if (plugin.townManager.establishTown(commandSender.getName(), newName, commandSender))
			commandSender.message(Color.Yellow() + "You have purchased a new town!");
		else
			commandSender.message(Color.Yellow() + "Something screwed up.");
	}
}
