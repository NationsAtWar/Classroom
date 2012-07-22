package org.nationsatwar.nations.commands;

import org.bukkit.command.CommandSender;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Nation;
import org.nationsatwar.nations.objects.User;
import org.nationsatwar.nations.objects.Invite.InviteType;

public class NationCommand extends NationsCommand {

	public NationCommand(CommandSender commandSender, String commandLabel, String[] command) {
		super(commandSender, commandLabel, command);
	} // Nation()
	
	@Override
	public void run() {
		//double nationPrice = plugin.getConfig().getDouble("nation_price");
		
		// -nation || nation help
		if(command.length == 0 || command[0].equalsIgnoreCase("help")) {
			this.helpText(commandSender, "i.e. '/nation [found|invite|leave]", "Nation manipulation commands. Use /nation [subcommand] for more help.");
			return;
		}
		
		// -nation found
		if(command[0].equalsIgnoreCase("found")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/nation found [nation name]", "Forms a nation.");
				return;
			}
			
			String nationName = this.connectStrings(command, 1, command.length);
			
			if (nationName.length() > 30) {
				this.errorText(commandSender, "Name too long:", "Nation names can only be 32 characters long. Blame iConomy.");
				return;
			}
			
			int aposAmount = 0;
			for (int i=0; i<nationName.length(); i++) {
				if (nationName.charAt(i) == '\'')
					aposAmount += 1;
				if (!Character.isLetterOrDigit(nationName.charAt(i)) && nationName.charAt(i) != ' ' && nationName.charAt(i) != '\'') {
					this.errorText(commandSender, "Invalid Nation Name:", "Letters or numbers only.");
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
			
			for(Nation nation : nations.nationManager.getNations().values()) {
				if(nation.getName().equalsIgnoreCase(nationName)) {
					this.errorText(commandSender, "A Nation with that name already exists!", null);
					return;
				}
			}
			
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
		}
		
		// -nation leave
		if(command[0].equalsIgnoreCase("leave")) {
			if(command.length == 2 && command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/nation leave", "Leaves the nation you're in.");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
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
		}
		
		// -nation invite [String: userName]
		if(command[0].equalsIgnoreCase("invite")) {
			if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
				this.helpText(commandSender, "i.e. '/nation invite [player name]", "invites a player to your nation.");
				return;
			}
			
			if(!(plugin instanceof Nations)) {
				return;
			}
			Nations nations = (Nations) plugin;
			
			
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

	}
}
