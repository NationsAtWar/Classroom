package org.nationsatwar.nations.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NationsDestroyEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private String orgName;
	private String orgType;
	private NationsDestroyEventType type;
	
	public NationsDestroyEvent(String eventOrgName, String eventOrgType, NationsDestroyEventType eventType) {
		this.orgName = eventOrgName;
		this.type = eventType;
		this.orgType = eventOrgType;
	}
	
	public String getOrgName() {
		return this.orgName;
	}
	
	public String getOrgType() {
		return this.orgType;
	}
	
	public NationsDestroyEventType getType() {
		return this.type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public enum NationsDestroyEventType {
		EMPTY
	}
}
