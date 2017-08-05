package cn.orgid.message.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class SendingMessage {

	public static enum ActionCode {
		RegisterClient, BroadCast, Notify, CreateChatChannel, 
		SendMessage, RegisterPlatformClient,
		Report, ConnectionCheck, Acknowledge, Inform,
		PublishCandidate,PublishSDP
	}

	public static enum ClientKey {
		id, token
	}

	public static enum Key {
		appId, appSecret, toClientTag, content, fromClientId, toClientId, checkTime, msgKey
	}

	public static enum ClientTag {
		Driver, Member
	}

	public static SendingMessage RegisterClientMessage(Long clientId,
			String clientToken) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.RegisterClient.name());
		message.add(ClientKey.id.name(), clientId);
		message.add(ClientKey.token.name(), clientToken);
		
		return message;

	}
	
	
	public static SendingMessage PublishCandidateMessage(Long clientId,String clientToken,IceCandidateInfo info) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.PublishCandidate.name());
		message.add(ClientKey.id.name(), clientId);
		message.add(ClientKey.token.name(), clientToken);
		message.add("sessionId", info.getSessionId());
		message.add("sdp", info.getSdp());
		message.add("clientId", info.getClientId());
		message.add("sdpMid", info.getSdpMid());
		message.add("sdpMLineIndex", info.getSdpMLineIndex());
		return message;

	}
	
	public static SendingMessage PublishSdpMessage(Long clientId,String clientToken,SdpInfo info) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.PublishSDP.name());
		message.add(ClientKey.id.name(), clientId);
		message.add(ClientKey.token.name(), clientToken);
		message.add("sessionId", info.getSessionId());
		message.add("sdp", info.getSdp());
		message.add("fromClientId", info.getFromClientId());
		message.add("toClientId", info.getToClientId());
		return message;

	}
	

	public static SendingMessage RegisterPlatformClientMessage(String appId,
			String appSecret) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.RegisterPlatformClient.name());
		message.add(Key.appId.name(), appId);
		message.add(Key.appSecret.name(), appSecret);
		return message;

	}

	public static SendingMessage ReportMessage(Long clientId,
			String clientToken, String content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.Report.name());
		message.add(ClientKey.id.name(), clientId);
		message.add(ClientKey.token.name(), clientToken);
		message.add(Key.content.name(), content);
		return message;

	}

	public static SendingMessage BroadCastMessage(String appId,
			String appSecret, String tag, String content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.BroadCast.name());
		message.add(Key.appId.name(), appId);
		message.add(Key.appSecret.name(), appSecret);
		message.add(Key.toClientTag.name(), tag);
		message.add(Key.content.name(), content);
		return message;

	}

	public static SendingMessage NotifyMessage(String appId, String appSecret,
			Long clientId, String content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.Notify.name());
		message.add(Key.appId.name(), appId);
		message.add(Key.appSecret.name(), appSecret);
		message.add(Key.toClientId.name(), clientId);
		message.add(Key.content.name(), content);
		return message;

	}

	public static SendingMessage CreateChannelMessage(String appId,
			String appSecret, Long fromClientId, Long toClientId) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.CreateChatChannel.name());
		message.add(Key.appId.name(), appId);
		message.add(Key.appSecret.name(), appSecret);
		message.add(Key.fromClientId.name(), fromClientId);
		message.add(Key.toClientId.name(), toClientId);
		return message;

	}

	public static SendingMessage ChatMessage(Long fromClientId,
			Long toClientId, String content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.SendMessage.name());
		message.add(Key.fromClientId.name(), fromClientId);
		message.add(Key.toClientId.name(), toClientId);
		message.add(Key.content.name(), content);
		return message;

	}

	public static SendingMessage ConnectionCheckMessage() {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.ConnectionCheck.name());
		message.add(Key.checkTime.name(), new Date().getTime());
		return message;

	}
	
	public static SendingMessage AcknowledgeMessage(String k) {
		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.Acknowledge.name());
		message.add(Key.msgKey.name(), k);
		return message;
	}

	private String action;

	private Map<String, Object> paras;
	
	

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Object> getParas() {

		return paras;

	}

	public void add(String key, Object value) {

		if (paras == null)
			paras = new HashMap<String, Object>();
		paras.put(key, value);

	}
	
	
	

	public void setParas(Map<String, Object> paras) {
		this.paras = paras;
	}

	public String toString() {
		return toJson();
	}

	public String toJson() {
		//SerializerFeature f =SerializerFeature.UseSingleQuotes;
		return JSON.toJSONString(this);

	}

	

	public boolean isRegisterMsg() {

		if (ActionCode.RegisterClient.name().equalsIgnoreCase(action)
				|| ActionCode.RegisterPlatformClient.name().equalsIgnoreCase(
						action)) {
			return true;
		}
		return false;

	}
	
	public static class Test{
		
		private String aa;

		public String getAa() {
			return aa;
		}

		public void setAa(String aa) {
			this.aa = aa;
		}
		
	}
	public static void main(String[] args) {
		Test t =new Test();
		t.aa="wqwq";
		SendingMessage s =SendingMessage.BroadCastMessage("1", "2", "2", t.toString());
		//System.out.println(s.toJson());
		JSONObject obj =JSON.parseObject(s.toJson());
		//System.out.println(obj.getJSONObject("paras").get("content"));
		
	}

	

}
