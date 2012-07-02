package org.nationsatwar.nations.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.User;

public abstract class NationsCommand implements Runnable {
	protected static PluginBase plugin;
	protected CommandSender commandSender;
	protected User user;
	protected String[] command;
	protected String commandLabel;
	
	protected NationsCommand(CommandSender commandSender, String commandLabel, String[] command) {
		this.commandSender = commandSender;
		this.command = command;
		if(plugin instanceof Nations) {
			Nations nations = (Nations) plugin;
			if(commandSender instanceof Player) {
				this.user = nations.userManager.getUserByPlayer((Player) commandSender);
			}
		}
	}
	
	/**
	 * Sends the user the help text for this command.
	 * @param commandSender The person to message.
	 */
	public void helpText(CommandSender commandSender) {
		commandSender.sendMessage("No help available for:" + this.commandLabel);
	}
	
	/**
	 * Sends the user the help text for this command.
	 * @param commandSender The person to message.
	 */
	public void helpText(CommandSender commandSender, String example, ArrayList<String> text) {
		if(text == null || text.isEmpty()) {
			commandSender.sendMessage("No help available for:" + this.commandLabel);
		} else {
			if(this.commandLabel != null) {
				commandSender.sendMessage(ChatColor.DARK_RED + "["+plugin.getName()+"] " + ChatColor.DARK_AQUA + " -=["+this.commandLabel.toUpperCase()+"]=-");
			}
			if(example != null) {
				commandSender.sendMessage(ChatColor.DARK_RED + "["+plugin.getName()+"] " + ChatColor.GREEN + example);
			}
			for(String t : text){
				commandSender.sendMessage(ChatColor.YELLOW + t);
			}
		}
	}
	
	/**
	 * Sends the user the help text for this command.
	 * @param commandSender The person to message.
	 */
	public void helpText(CommandSender commandSender, String example, String text) {
		if(text == null || text.isEmpty()) {
			commandSender.sendMessage("No help available for:" + commandLabel);
			return;
		} else {
			if(this.commandLabel != null) {
				commandSender.sendMessage(ChatColor.DARK_RED + "["+plugin.getName()+"] " + ChatColor.DARK_AQUA + " -=["+commandLabel.toUpperCase()+"]=-");
			}
			if(example != null) {
				commandSender.sendMessage(ChatColor.DARK_RED + "["+plugin.getName()+"] " + ChatColor.GREEN + example);
			}
			if(text != null){
				commandSender.sendMessage(ChatColor.YELLOW + text);
			}
			return;
		}
	}
	
	/**
	 * Sends the user the help text for this command.
	 * @param commandSender The person to message.
	 */
	public void errorText(CommandSender commandSender, String err, String info) {
		if(err != null && info != null) {
			commandSender.sendMessage(ChatColor.RED + err + " " + ChatColor.YELLOW + info);
			return;
		}
		if(err != null) {
			commandSender.sendMessage(ChatColor.RED + err);
			return;
		}
		if(info != null) {
			commandSender.sendMessage(ChatColor.YELLOW + info);
			return;
		}
	}
	
	/**
	 * Sends the user the help text for this command.
	 * @param commandSender The person to message.
	 */
	public void successText(CommandSender commandSender, String completion, String info) {
		if(completion != null && info != null) {
			commandSender.sendMessage(ChatColor.GREEN + completion + " " + ChatColor.YELLOW + info);
			return;
		}
		if(completion != null) {
			commandSender.sendMessage(ChatColor.GREEN + completion);
			return;
		}
		if(info != null) {
			commandSender.sendMessage(ChatColor.YELLOW + info);
			return;
		}
	}
	
	/**
	 * Cancels the command and tells the user they don't have access.
	 * @param commandSender The person to message.
	 */
	public boolean hasAccess(CommandSender commandSender, boolean permission) {
		if (!permission) {
			commandSender.sendMessage(ChatColor.RED + commandLabel + ": Permission denied.");
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