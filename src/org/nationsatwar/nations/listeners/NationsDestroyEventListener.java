package org.nationsatwar.nations.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.events.NationsDestroyEvent;

public class NationsDestroyEventListener implements Listener {
	private Nations plugin = null;
	
	public NationsDestroyEventListener(Nations instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onNationsDestroyEvent(NationsDestroyEvent event) {
		switch(event.getType()) {
		case EMPTY:
			plugin.getOrgManager(event.getOrgType()).removeOrg(event.getOrgName(), event.getType());
			break;
		default:
			break;
		}
	}
}
