package cn.orgid.message.client;


public class SdpInfo {
	
	
	private Long sessionId;
	
	private String sdp;
	
	private Long fromClientId;
	
	private Long toClientId;

	public String getSdp() {
		return sdp;
	}

	public void setSdp(String sdp) {
		this.sdp = sdp;
	}
	

	public Long getFromClientId() {
		return fromClientId;
	}

	public void setFromClientId(Long fromClientId) {
		this.fromClientId = fromClientId;
	}

	public Long getToClientId() {
		return toClientId;
	}

	public void setToClientId(Long toClientId) {
		this.toClientId = toClientId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
