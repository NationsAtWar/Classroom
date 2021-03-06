package org.nationsatwar.nations.managers;

import java.util.Map;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.datasource.Database;
import org.nationsatwar.nations.events.NationsCreateEvent.NationsCreateEventType;
import org.nationsatwar.nations.events.NationsDestroyEvent;
import org.nationsatwar.nations.events.NationsDestroyEvent.NationsDestroyEventType;
import org.nationsatwar.nations.objects.Organization;

public class OrganizationManager {
	private Nations plugin;
	private Database database;
	//Do we need to keep these in memory or can we just get them as needed?
	private Map<String, Organization> orgs;
	//Sure why not. It's easier for now.
	//private ArrayList<String> orgs;
	private String type; //name set in config
	private int orgLevel;
	private boolean hasPlots;
	private boolean removeEmpty;
	
	public OrganizationManager(Nations instance, String name, int level, boolean hasPlots, boolean removeEmpty) {
		this.plugin = instance;
		this.type = name;
		this.orgLevel = level;
		this.hasPlots = hasPlots;
		this.removeEmpty = removeEmpty;
		
		this.database = new Database(instance, name);
	}

	public void loadAll() {
		for(String s : this.database.getOrgNames()) {
			this.orgs.put(s, this.database.getOrganization(s));
		}		
	}

	public void saveAll() {
		for(String s : this.orgs.keySet()) {
			this.database.save(s);
		}
	}

	private Organization getOrganization(String org) {
		return this.orgs.get(org);
	}

	public boolean addMember(String org, String member) {
		return this.getOrganization(org).addMember(member);
	}

	public boolean removeMember(String memberOrg, String member) {
		Organization org = this.getOrganization(memberOrg);
		if(org.removeMember(member)) {
			if(org.getMembers().isEmpty() && this.removeEmpty) {
				NationsDestroyEvent event = new NationsDestroyEvent(org.getName(), this.type, NationsDestroyEventType.EMPTY);
				plugin.getServer().getPluginManager().callEvent(event);
			}
			return true;
		}
		return false;
	}

	public boolean removeOrg(String orgName, NationsDestroyEventType eventType) {
		switch(eventType) {
		case EMPTY:
			this.plugin.messageAll("There are no longer any souls to light the torches in " +orgName + ". It has fallen.");
		}
		
		if((this.orgs.remove(orgName) != null) && !this.database.removeOrganization(orgName)) {
			return false;
		}
		
		return true;
	}

	public Organization addOrg(String orgName, String member, NationsCreateEventType eventType) {
		this.orgs.put(orgName, new Organization(orgName, member, this.orgLevel));
		return this.getOrganization(orgName);
	}
}
