package org.nationsatwar.nations.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NationsCreateEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private String player;
	private String orgName;
	private String orgType;
	private NationsCreateEventType type;
	
	public NationsCreateEvent(String eventPlayer, String eventOrgName, String eventOrgType, NationsCreateEventType eventType) {
		this.player = eventPlayer;
		this.orgName = eventOrgName;
		this.orgType = eventOrgType;
		this.type = eventType;
	}
	
	public String getPlayer() {
		return this.player;
	}
	
	public String getOrgName() {
		return this.orgName;
	}
	
	public String getOrgType() {
		return this.orgType;
	}
	
	public NationsCreateEventType getType() {
		return this.type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public enum NationsCreateEventType {
		NEW
	}
}
