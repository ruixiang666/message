package cn.orgid.message.client;

import org.apache.http.client.ResponseHandler;

import com.alibaba.fastjson.JSON;

import cn.orgid.common.http.client.HttpClientComponent;
import cn.orgid.common.http.client.HttpClientComponent.RequestParameters;
import cn.orgid.common.http.client.JsonResponseHandler;

public class WebSocketHttpClient {
	
	
	private WebSocketConf socketConf;
	
	
	public WebSocketConf getSocketConf() {
		return socketConf;
	}

	public void setSocketConf(WebSocketConf socketConf) {
		this.socketConf = socketConf;
	}

	public AppClient createClient(AppClient appClient){
		
		String url=socketConf.getWsHttpServiceBaseUrl()+socketConf.getWsCreateClientUri();
		RequestParameters parameters =new RequestParameters();
		parameters.add(WebSocketConf.WS_CLIENT_NAME, appClient.getName());
		parameters.add(WebSocketConf.WS_CLIENT_TAG, appClient.getTag());
		parameters.add(WebSocketConf.WS_APP_ID,socketConf.getWsAppId());
		parameters.add(WebSocketConf.WS_APP_SECRET,socketConf.getWsSecret());
		ResponseHandler<AppClient> responseHandler = JsonResponseHandler.createResponseHandler(AppClient.class);
		return HttpClientComponent.executePost(parameters, url, responseHandler);
		
	}
	
	public AppClient getClientToken(Long clientId){
		
		String url=socketConf.getWsHttpServiceBaseUrl()+socketConf.getWsClientTokenUrl();
		RequestParameters parameters =new RequestParameters();
		parameters.add(WebSocketConf.WS_CLIENT_ID, String.valueOf(clientId));
		parameters.add(WebSocketConf.WS_APP_ID,socketConf.getWsAppId());
		parameters.add(WebSocketConf.WS_APP_SECRET,socketConf.getWsSecret());
		ResponseHandler<AppClient> responseHandler = JsonResponseHandler.createResponseHandler(AppClient.class);
		return HttpClientComponent.executePost(parameters, url, responseHandler);
		
	}
	
	public TalkSession createP2PTalkSession(Long clientId){
		
		String url=socketConf.getWsHttpServiceBaseUrl()+socketConf.getWsCreateTalkSessionUri();
		RequestParameters parameters =new RequestParameters();
		parameters.add(WebSocketConf.WS_APP_ID,socketConf.getWsAppId());
		parameters.add(WebSocketConf.WS_APP_SECRET,socketConf.getWsSecret());
		parameters.add(WebSocketConf.WS_CLIENT_ID,String.valueOf(clientId));
		
		ResponseHandler<JsonCommonResult> responseHandler = 
				(ResponseHandler<JsonCommonResult>) JsonResponseHandler.createResponseHandler( JsonCommonResult.class);
		JsonCommonResult r=		HttpClientComponent.executePost(parameters, url, responseHandler);
		TalkSession t = JSON.parseObject(r.getValue().toString(), TalkSession.class);
		return t;
		
	}
	
	public void addPeerConnectionToTalkSession(Long sessionId,Long clientId){
		
		String url=socketConf.getWsHttpServiceBaseUrl()+socketConf.getWsAddPeerConnectionToTalkSessionUri();
		RequestParameters parameters =new RequestParameters();
		parameters.add(WebSocketConf.WS_TALK_SESSION_ID, String.valueOf(sessionId));
		parameters.add(WebSocketConf.WS_CLIENT_ID, String.valueOf(clientId));
		parameters.add(WebSocketConf.WS_APP_ID,socketConf.getWsAppId());
		parameters.add(WebSocketConf.WS_APP_SECRET,socketConf.getWsSecret());
		ResponseHandler<JsonCommonResult> responseHandler = JsonResponseHandler.createResponseHandler(JsonCommonResult.class);
		HttpClientComponent.executePost(parameters, url, responseHandler);
	}
	
	
	
}
