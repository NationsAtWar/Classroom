package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Alliance;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.User;
import org.nationsatwar.nations.objects.Invite.InviteType;

public class AllianceCommand extends NationsCommand {

	public AllianceCommand(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // AllianceCommand()
	
	@Override
	public void run() {
		// -alliance || alliance help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/alliance [found|invite|leave]", "Alliance manipulation commands. Use /nation [subcommand] for more help.");
			return;
		}
		
		// -nation found
		if(command[0].equalsIgnoreCase("found")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/alliance found [alliance name]", "Forms an alliance.");
				return;
			}
			
			String allianceName = this.connectStrings(command, 1, command.length);
			
			if (allianceName.length() > 30) {
				this.errorText(commandSender, "Name too long:", "Alliance names can only be 32 characters long. Blame iConomy.");
				return;
			}
			
			int aposAmount = 0;
			for (int i=0; i<allianceName.length(); i++) {
				if (allianceName.charAt(i) == '\'')
					aposAmount += 1;
				if (!Character.isLetterOrDigit(allianceName.charAt(i)) && allianceName.charAt(i) != ' ' && allianceName.charAt(i) != '\'') {
					this.errorText(commandSender, "Invalid Alliance Name:", "Letters or numbers only.");
					return;
				}
			}
			
			if (aposAmount > 1) {
				this.errorText(commandSender, "Too many apostrophes:", ":/");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			for(Alliance alliance : nations.allianceManager.getAlliances().values()) {
				if(alliance.getName().equalsIgnoreCase(allianceName)) {
					this.errorText(commandSender, "An alliance with that name already exists!", null);
					return;
				}
			}
			
			return;
			/*
			if(user != null) {
				if(nations.nationManager.getNationByUserID(user.getID()) != null) {
					this.errorText(commandSender, null, "You are already a member of a nation. You must leave that nation " +
							"before you can found a new one!");
					return;						
				}
			}
			Nation nation = nations.nationManager.createNation(nationName);
			if(nation != null) {
				nations.notifyAll("Nation: " + nationName + " created!");
			} else {
				this.errorText(commandSender, "Couldn't create nation.", null);
				return;
			}
			if(user != null) {
				if(nation.addMember(user, nations.rankManager.getFounderRank())) {
					this.successText(commandSender, null, "Added you as a founder of "+nation.getName());
				}
			}
			return;
			*/
		}
		
		// -alliance leave
		if(command[0].equalsIgnoreCase("leave")) {
			if(command.length == 2 && command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/alliance leave", "Leaves the alliance you're in.");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			return;
			/*
			
			Nation nation = nations.nationManager.getNationByUserID(user.getID());
			if(user != null) {
				if(nation == null) {
					this.errorText(commandSender, null, "You aren't in a nation!");
					return;						
				}
			}
			
			if(nation.removeMember(user)) {
				this.successText(commandSender, null, "Removed you from "+nation.getName());
				if(nation.getMembers(null) == null || nation.getMembers(null).isEmpty()) {
					if(nations.nationManager.delete(nation)) {
						nations.notifyAll("The nation of " + nation.getName() + " was lost to the sands of time!");
					}
				}
			} else {
				this.errorText(commandSender, "Couldn't remove you from the nation.", null);
			}
			
			return;
			*/
		}
		
		// -alliance invite [String: nationName]
		if(command[0].equalsIgnoreCase("invite")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/alliance invite [nation name]", "invites a nation to your alliance.");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			return;
			/*
			
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
			*/
		}
	}
}
