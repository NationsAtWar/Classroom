package org.nationsatwar.nations.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginBase;

public abstract class NationsCommand implements Runnable {
	protected static PluginBase plugin;
	protected CommandSender commandSender;
	protected String[] command;
	
	protected NationsCommand(CommandSender commandSender, String[] command) {
		this.commandSender = commandSender;
		this.command = command;
	}
	
	/**
	 * Sends the user the help text for this command.
	 * @param commandSender The person to message.
	 */
	public void helpText(CommandSender commandSender) {
		commandSender.sendMessage("No help available for:" + command[0]);
	}
	
	/**
	 * Cancels the command and tells the user they don't have access.
	 * @param commandSender The person to message.
	 */
	public boolean hasAccess(CommandSender commandSender, boolean permission) {
		if (!permission) {
			commandSender.sendMessage(ChatColor.RED + "Permission denied.");
			return false;
		}
		return true;
	}
	
	/*
	 *  All the methods after this are string parsing methods designed to extract 
	 *  relevant information from a string as it pertains to the plugin.
	 */
	
	/**
	 * Connects the strings of a command together from one index to another.
	 * If the indexes don't exist, the string returns empty.
	 * 
	 * @param command[] The command you want the strings combined..
	 * @param startIndex Which index you want the method to start at.
	 * @param endIndex Which index you want the method to end at.
	 * @return This is the end result of the combined strings
	 */
	public String connectStrings(String command[], int startIndex, int endIndex) {
		
		String connectedString = command[startIndex];
		
		if (endIndex - startIndex > 0) {
			for (int i=startIndex+1; i<endIndex; i++) {
				if (command[i] != null)
					connectedString += " " + command[i];
				else
					return "";
			}
		}
		return connectedString;
	}
	
	/**
	 * This returns how many words are in a phrase. Useful for parsing commands.
	 * @param phrase The phrase you want to count indexes on.
	 * @return The amount of indexes the phrase takes up.
	 */
	public int getIndexCount(String phrase) {
		int index = 1;
		if (phrase.length() > 0) {
			for (int i=0; i<phrase.length(); i++) {
				if (phrase.charAt(i) == ' ') {
					index += 1;
				}
			}
		}
		return index;
	}
}