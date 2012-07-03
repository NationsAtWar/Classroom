package org.nationsatwar.nations.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Invite;
import org.nationsatwar.nations.objects.Invite.InviteType;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.Town;
import org.nationsatwar.nations.objects.User;

public class Accept extends NationsCommand {

	protected Accept(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	}

	@Override
	public void run() {
		// --accept || -accept help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "e.g. '/accept [list|player|town|nation]'", "Accepts and lists any invitations you may have.");
			return;
		}
		
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		if(user == null) {
			this.errorText(commandSender, "Console doesn't get invites. Poor console :(", null);
			return;
		}
		
		if(command[0].equalsIgnoreCase("list")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept list", "Lists any invites you may have that you can accept.");
				return;
			}
			
			ArrayList<Invite> invites = null;
			if(user != null) {
				invites = nations.inviteManager.getInvites(user);
			}
			if(invites == null || invites.isEmpty()) {
				this.successText(commandSender, null, "No invites present.");
				return;
			}
			for(Invite inv : invites) {
				
				if(inv.getType() == InviteType.PLAYERNATION) {
					User inviter = nations.userManager.getUserByID(inv.getInviter());
					this.successText(commandSender, inv.getNiceType()+" Invitation From: "+inviter.getName()+" ("+nations.nationManager.getNationByUserID(inviter.getID())+")", null);
				}
				
				
			}
			return;
		}
		
		if(command[0].equalsIgnoreCase("player")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept player [player name]", "accepts a nation invite from a player.");
				return;
			}
			
			ArrayList<Invite> invites = null;
			if(user != null) {
				invites = nations.inviteManager.getInvites(user);
			}
			if(invites == null || invites.isEmpty()) {
				this.successText(commandSender, null, "No invites present.");
				return;
			}
			for(Invite inv : invites) {
				User inviter = nations.userManager.getUserByID(inv.getInviter());
				if(inviter.getName().equalsIgnoreCase(command[1])) {
					Nation nation = nations.nationManager.getNationByUserID(user.getID());
					Town town = nations.townManager.getTownByUserID(user.getID());
					if(nation != null) {
						nation.removeMember(user);
					}
					if(town != null) {
						town.removeMember(user);
					}
					if(nations.nationManager.getNationByUserID(inviter.getID()).addMember(user, nations.rankManager.getRecruitRank())) {
						this.successText(commandSender, "You've accepted your invitation into "+nations.nationManager.getNationByUserID(inviter.getID()).getName()+".", null);					
					}
					return;
				}
			}
			this.errorText(commandSender, "That was not a valid invitation", null);			
			
			return;
		}
		
		if(command[0].equalsIgnoreCase("town")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept town [town name]", "accepts your town into the nation.");
				return;
			}
		}
		
		if(command[0].equalsIgnoreCase("nation")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/accept nation [nation name]", "accepts your nation into the alliance.");
				return;
			}
		}
		
	}

}
