package cn.orgid.message.client;

public class IceCandidateInfo  {

	private Long sessionId;
	
	private Long clientId;
	
	private String sdpMid;
	
	private Integer sdpMLineIndex;
	
	private String sdp;

	public String getSdpMid() {
		return sdpMid;
	}

	public void setSdpMid(String sdpMid) {
		this.sdpMid = sdpMid;
	}

	public Integer getSdpMLineIndex() {
		return sdpMLineIndex;
	}

	public void setSdpMLineIndex(Integer sdpMLineIndex) {
		this.sdpMLineIndex = sdpMLineIndex;
	}

	public String getSdp() {
		return sdp;
	}

	public void setSdp(String sdp) {
		this.sdp = sdp;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	
	

}
