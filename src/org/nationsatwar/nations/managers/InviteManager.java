package org.nationsatwar.nations.managers;

import java.util.ArrayList;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.objects.Invite;
import org.nationsatwar.nations.objects.User;

public class InviteManager extends NationsManagement {
	private ArrayList<Invite> inviteList = new ArrayList<Invite>();

	public InviteManager(PluginBase plugin) {
		super(plugin);
	}

	public boolean addInvite(Invite newInvite) {
		if(!inviteList.contains(newInvite)) {
			this.inviteList.add(newInvite);
			return true;
		}
		return false;
	}

	public ArrayList<Invite> getInvites(User user) {
		ArrayList<Invite> newList = new ArrayList<Invite>();
		for(Invite inv : this.inviteList) {
			if(inv.getInvitee() == user) {
				newList.add(inv);
			}
		}
		return newList;
	}

}
