package org.nationsatwar.nations.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.events.NationsMemberEvent;

public class NationsMemberEventListener implements Listener {
	private Nations plugin = null;
	
	public NationsMemberEventListener(Nations instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onNationsMemberEvent(NationsMemberEvent event) {
		switch(event.getType()) {
		case ADD:
			plugin.orgManager.addMember(event.getOrg(), event.getMember());
			break;
		case REMOVE:
			plugin.orgManager.removeMember(event.getOrg(), event.getMember());
			break;
		default:
			break;
		}
	}
}
