package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Invite.InviteType;
import org.nationsatwar.nations.objects.User;

public class InviteCommand extends NationsCommand {

	protected InviteCommand(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	}

	@Override
	public void run() {
		// --invite || -invite help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "e.g. '/invite [player|town|nation]'", "Invites player to join your nation. Nation permission may apply.");
			return;
		}
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		if(command[0].equalsIgnoreCase("player")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/invite player [player name]", "invites a player to your nation.");
				return;
			}
			// -invite player [String: userName]
			
			User inviter = user;
			User invitee = nations.userManager.findUser(command[1]);
			
			if(invitee == null) {
				this.errorText(commandSender, "That user does not exist or is not registered!", null);
				return;
			}
			
			if(nations.inviteManager.createInvite(InviteType.PLAYERNATION, inviter, invitee)) {
				this.successText(commandSender, "Invite Sent.", null);
			}
			
			return;
		}
		
		if(command[0].equalsIgnoreCase("town")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/invite town [town name]", "invites a town to your nation.");
				return;
			}
		}
		
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/invite nation [nation name]", "invites a nation to your alliance.");
				return;
			}
		}
		
	}

}
