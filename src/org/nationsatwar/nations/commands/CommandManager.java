package org.nationsatwar.nations.commands;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

/**
 * 
 * @author Jerik
 * 
 */
public class CommandManager {

	private PluginBase plugin;

	public CommandManager(PluginBase plugin) {

		this.plugin = plugin;
		NationsCommand.plugin = this.plugin;
	}

	//Just like onCommand().
	public boolean execute(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		ConsoleCommandSender console = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		} else if(sender instanceof ConsoleCommandSender) {
			console = (ConsoleCommandSender) sender;
		} else {
			plugin.getLogger().log(Level.WARNING, "CommandSender not console or player.");   
			return false;
		}
		
		if(commandLabel.equalsIgnoreCase("establish")) {
			if(player != null) {
				if(player.hasPermission("nations.establish")) {
					new Establish(player, args).run();
					return true;
				}
			} else {
				console.sendMessage("Console can't token.");
			}
			return false;
		}
		
		if(commandLabel.equalsIgnoreCase("paste")) {
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if(plugin instanceof Paste){
					if(player != null) {
						if(!player.hasPermission("nations.paste.admin")) {
							player.sendMessage("Sorry, you don't have permissions for that");
							return false;
						}
					}
					((Paste) plugin).reload(sender);
				}
			}
		}
		
		//token gateway if needed.
		if(player != null) {
			if(!(PasteAPI.confirmToken(player.getName()))) {
				player.sendMessage("You do not have access to these commands. Type '/token' to register.");
				return false;
			}
		}
		/*
		if (command.length == 0)
			new Help(commandSender, command).run();
		else if (command[0].equalsIgnoreCase("accept"))
			new Accept(commandSender, command).run();
		else if (command[0].equalsIgnoreCase("help"))
			new Help(commandSender, command).run();
		else
			// default for just typing [/naw]
			new Help(commandSender, command).run();
			*/
		return false;
	}
}
