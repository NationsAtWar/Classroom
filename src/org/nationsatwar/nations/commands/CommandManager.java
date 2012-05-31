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
		if(!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) {
			plugin.getLogger().log(Level.WARNING, "CommandSender not console or player.");   
			return false;
		}
		
		if(commandLabel.equalsIgnoreCase("found")) {
			new Found(sender, commandLabel, args).run();
			return true;
		}
		if(commandLabel.equalsIgnoreCase("world")) {
			new World(sender, commandLabel, args).run();
			return true;
		}
		if(commandLabel.equalsIgnoreCase("invite")) {
			new Invite(sender, commandLabel, args).run();
			return true;
		}
		
		/*if(commandLabel.equalsIgnoreCase("nations")) {
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if(plugin instanceof Nations){
					if(player != null) {
						if(!player.hasPermission("nations.paste.admin")) {
							player.sendMessage("Sorry, you don't have permissions for that");
							return false;
						}
					}
					((Nations) plugin).reload(sender);
				}
			}
		}*/
		
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
