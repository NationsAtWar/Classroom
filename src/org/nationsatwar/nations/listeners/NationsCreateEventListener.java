package org.nationsatwar.nations.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.events.NationsCreateEvent;

public class NationsCreateEventListener implements Listener {
	private Nations plugin = null;
	
	public NationsCreateEventListener(Nations instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onNationsCreateEvent(NationsCreateEvent event) {
		switch(event.getType()) {
		case NEW:
			plugin.getOrgManager(event.getOrgType()).addOrg(event.getPlayer(), event.getOrgName(), event.getType());
			break;
		default:
			break;
		}
	}
}
