package org.nationsatwar.nations.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.Invite;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Invite.InviteType;
import org.nationsatwar.nations.objects.User;

public class InviteManager extends NationsManagement {
	private HashMap<Integer, Invite> inviteMap = new HashMap<Integer, Invite>();

	public InviteManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(plugin instanceof Nations) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		inviteMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new Invite(0, null, 0, 0))) {
			Invite object = (Invite) obj;
			if (!inviteMap.containsKey(object.getID()))
				inviteMap.put(object.getID(), object);
		}
	}

	@Override
	public void saveAll() {
		if(plugin instanceof Nations) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Invite object : inviteMap.values()) {
			nations.database.save(object);
		}
	}

	@Override
	public void deleteAll() {
		if(plugin instanceof Nations) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Invite object : inviteMap.values()) {
			nations.database.delete(object);
		}
		inviteMap.clear();
	}
	
	public boolean createInvite(InviteType playernation, User inviter, User invitee) {
		int newKey = Collections.max(inviteMap.keySet())+1;
		Invite newInvite = new Invite(newKey, InviteType.PLAYERNATION, inviter.getID(), invitee.getID());
		if(this.addInvite(newInvite)) {
			return true;
		}
		return false;
	}

	private boolean addInvite(Invite newInvite) {
		if(!inviteMap.containsKey(newInvite.getID())) {
			this.inviteMap.put(newInvite.getID(), newInvite);
			return true;
		}
		return false;
	}

	public ArrayList<Invite> getInvites(User user) {
		ArrayList<Invite> newList = new ArrayList<Invite>();
		for(Invite inv : this.inviteMap.values()) {
			if(inv.getInvitee() == user.getID()) {
				newList.add(inv);
			}
		}
		return newList;
	}

}
