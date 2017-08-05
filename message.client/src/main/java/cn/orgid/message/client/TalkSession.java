package cn.orgid.message.client;

import java.util.List;


public  class TalkSession {
	
	public enum SessionType {
		
		P2P,Broadcast
		
	}
	
	private Long id;
	
	private SessionType sessionType;
	
	private Integer currentPeerConnectionAmount;
	
	
	private List<StunServer> stunServers;
	

	public SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	public Integer getCurrentPeerConnectionAmount() {
		return currentPeerConnectionAmount;
	}

	public void setCurrentPeerConnectionAmount(Integer currentPeerConnectionAmount) {
		this.currentPeerConnectionAmount = currentPeerConnectionAmount;
	}

	public List<StunServer> getStunServers() {
		return stunServers;
	}

	public void setStunServers(List<StunServer> stunServers) {
		this.stunServers = stunServers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
