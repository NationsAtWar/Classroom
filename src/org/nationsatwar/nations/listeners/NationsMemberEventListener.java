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
			plugin.getOrgManager(event.getOrgType()).addMember(event.getOrgName(), event.getPlayer());
			break;
		case REMOVE:
			plugin.getOrgManager(event.getOrgType()).removeMember(event.getOrgName(), event.getPlayer());
			break;
		default:
			break;
		}
	}
}
