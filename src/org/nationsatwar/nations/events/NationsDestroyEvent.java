package org.nationsatwar.nations.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NationsDestroyEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private String member;
	private String org;
	private NationsDestroyEventType type;
	private int orgLevel;
	
	public NationsDestroyEvent(String eventMember, String eventOrg, NationsDestroyEventType eventType, int eventLevel) {
		this.member = eventMember;
		this.org = eventOrg;
		this.type = eventType;
		this.orgLevel = eventLevel;
	}
	
	public String getMember() {
		return this.member;
	}
	
	public String getOrg() {
		return this.org;
	}
	
	public NationsDestroyEventType getType() {
		return this.type;
	}
	
	public int getLevel() {
		return this.orgLevel;
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
