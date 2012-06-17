package org.nationsatwar.nations.objects;

public class Invite {
	private InviteType type;
	private Object invitee;
	private Object inviter;
	
	public Invite(InviteType newType, Object newInviter, Object newInvitee) {
		this.type = newType;
		
		if(newType == InviteType.PLAYERNATION) {
			this.invitee = newInvitee;
			this.inviter = newInviter;
		}
	}
	
	public enum InviteType {
		PLAYERNATION,NATIONALLIANCE,TOWNNATION,
	}

	public Object getInvitee() {
		return invitee;
	}

	public String getNiceType() {
		if(type == InviteType.PLAYERNATION) {
			return "Nation";
		}
		return null;
	}

	public Object getInviter() {
		return inviter;
	}

}
