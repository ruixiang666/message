package cn.orgid.message.client;

import cn.orgid.message.client.SendingMessage.ActionCode;
import cn.orgid.message.client.SendingMessage.Key;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;




public class WebSocketPlatformClient extends WebSocketClient{
	
	
	
	public void setSocketConf(WebSocketConf socketConf) {
		
		super.setSocketConf(socketConf);
		
	}
	
	public  void sendBroadCastMessage(String tag, String content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.BroadCast.name());
		message.add(Key.appId.name(), getSocketConf().getWsAppId());
		message.add(Key.appSecret.name(), getSocketConf().getWsSecret());
		message.add(Key.toClientTag.name(), tag);
		message.add(Key.content.name(), content);
		send(message);

	}
	
	

	public  void sendNotifyMessage(Long clientId, String content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.Notify.name());
		message.add(Key.appId.name(), getSocketConf().getWsAppId());
		message.add(Key.appSecret.name(), getSocketConf().getWsSecret());
		message.add(Key.toClientId.name(), clientId);
		message.add(Key.content.name(), content);
		send(message);
		
	}

	public  void sendChannelMessage(Long fromClientId, Long toClientId) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.CreateChatChannel.name());
		message.add(Key.appId.name(), getSocketConf().getWsAppId());
		message.add(Key.appSecret.name(), getSocketConf().getWsSecret());
		message.add(Key.fromClientId.name(), fromClientId);
		message.add(Key.toClientId.name(), toClientId);
		send(message);
		
	}
	
	public void sendRegisterPlatformClientMessage() {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.RegisterPlatformClient.name());
		message.add(Key.appId.name(), getSocketConf().getWsAppId());
		message.add(Key.appSecret.name(), getSocketConf().getWsSecret());
		send(message);
		registerMessage=message;
		
		
	}

	
	public void init() {
		
		super.init();
		sendRegisterPlatformClientMessage();
		
	}

	public void sendInformMessage(Long clientId, String content) {
		
		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.Inform.name());
		message.add(Key.appId.name(), getSocketConf().getWsAppId());
		message.add(Key.appSecret.name(), getSocketConf().getWsSecret());
		message.add(Key.toClientId.name(), clientId);
		message.add(Key.content.name(), content);
		send(message);
		
	}
	
	
	
	
	
	
}
