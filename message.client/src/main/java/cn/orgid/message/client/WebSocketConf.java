package cn.orgid.message.client;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import cn.orgid.common.http.client.HttpClientComponent;
import cn.orgid.common.http.client.HttpClientComponent.RequestParameters;
import cn.orgid.common.http.client.JsonResponseHandler;

public class WebSocketConf {

	
	
	
	public final static String WS_APP_ID="appId";
	
	public final static String WS_APP_SECRET="appSecret";

	public static final String WS_CLIENT_NAME = "name";

	public static final String WS_CLIENT_TAG = "tag";

	public static final String WS_APP_TOKEN = "appToken";

	public static final String WS_CLIENT_ID = "clientId";

	public static final String WS_TALK_SESSION_ID = "sessionId";
	
	
	
	
	private String wsHttpServiceBaseUrl;
	
	private String wsCreateClientUri;
	
	private String wsClientTokenUrl;
	
	private String wsCreateTalkSessionUri="/service/create_p2p_talk_session.htm";
	
	private String wsAddPeerConnectionToTalkSessionUri="/service/add_connection_to_talk_session.htm";
	
	
	private String wsHost;
	
	private String wsUrl;
	
	
	private MsgServerList msgServerList;
	private Random r=new Random();
	
	
	
	private String wsAppId;
	
	private String wsSecret;
	
	private String uploadFileUri="/service/upload_file.htm";
	
	private String serverListUrl="/service/get_server_list.htm";
	
	private  int checkPongTimeInterval = 1000;

	private  int pingTimeInterval = 1000;
	
	private  int pongTimeOut = 15*1000;
	
	private boolean debug;
	
	private String tempFilePath="./tmp";

	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}

	public String getWsHost() {
		return wsHost;
	}

	public void setWsHost(String wsHost) {
		this.wsHost = wsHost;
	}

	public String getWsHttpServiceBaseUrl() {
		return wsHttpServiceBaseUrl;
	}

	public void setWsHttpServiceBaseUrl(String wsHttpServiceBaseUrl) {
		this.wsHttpServiceBaseUrl = wsHttpServiceBaseUrl;
	}

	

	public String getWsCreateClientUri() {
		return wsCreateClientUri;
	}

	public void setWsCreateClientUri(String wsCreateClientUri) {
		this.wsCreateClientUri = wsCreateClientUri;
	}

	public String getWsClientTokenUrl() {
		return wsClientTokenUrl;
	}

	public void setWsClientTokenUrl(String wsClientTokenUrl) {
		this.wsClientTokenUrl = wsClientTokenUrl;
	}
	
	

	public String getWsCreateTalkSessionUri() {
		return wsCreateTalkSessionUri;
	}

	public void setWsCreateTalkSessionUri(String wsCreateTalkSessionUri) {
		this.wsCreateTalkSessionUri = wsCreateTalkSessionUri;
	}

	public String getWsAddPeerConnectionToTalkSessionUri() {
		return wsAddPeerConnectionToTalkSessionUri;
	}

	public void setWsAddPeerConnectionToTalkSessionUri(String wsAddPeerConnectionToTalkSessionUri) {
		this.wsAddPeerConnectionToTalkSessionUri = wsAddPeerConnectionToTalkSessionUri;
	}

	public String getWsUrl() {
		
		if(StringUtils.isNotBlank(serverListUrl)){
			String url=getWsHttpServiceBaseUrl()+serverListUrl;
			RequestParameters parameters = new RequestParameters();
			MsgServerList list =HttpClientComponent.executePost(parameters , url, JsonResponseHandler.createResponseHandler(MsgServerList.class));
			this.msgServerList=list;
		}
		if(msgServerList!=null){
			MsgServer s=msgServerList.randomServer();
			if(s!=null){
				return s.serverUrl;
			}
		}
		return wsUrl;
		
		
	}

	public void setWsUrl(String wsUrl) {
		
			this.wsUrl = wsUrl;
	}

	public String getWsAppId() {
		return wsAppId;
	}

	public void setWsAppId(String wsAppId) {
		this.wsAppId = wsAppId;
	}

	public String getWsSecret() {
		return wsSecret;
	}

	public void setWsSecret(String wsSecret) {
		this.wsSecret = wsSecret;
	}

	public int getCheckPongTimeInterval() {
		return checkPongTimeInterval;
	}

	public void setCheckPongTimeInterval(int checkPongTimeInterval) {
		this.checkPongTimeInterval = checkPongTimeInterval;
	}

	public int getPingTimeInterval() {
		return pingTimeInterval;
	}
	
	

	public String getUploadFileUri() {
		return uploadFileUri;
	}

	public void setUploadFileUri(String uploadFileUri) {
		this.uploadFileUri = uploadFileUri;
	}

	public void setPingTimeInterval(int pingTimeInterval) {
		this.pingTimeInterval = pingTimeInterval;
	}

	public int getPongTimeOut() {
		return pongTimeOut;
	}

	public void setPongTimeOut(int pongTimeOut) {
		this.pongTimeOut = pongTimeOut;
	}	

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getServerListUrl() {
		return serverListUrl;
	}

	public void setServerListUrl(String serverListUrl) {
		
		
		this.serverListUrl = serverListUrl;
	}
	
	public void init(){
		
	}
	
	public static class MsgServerList{
		
		private List<MsgServer> servers;
		
		private final static Random r = new Random();

		public List<MsgServer> getServers() {
			return servers;
		}

		public void setValue(List<MsgServer> servers) {
			this.servers = servers;
		}
		
		public MsgServer randomServer(){
			
			if(servers!=null&&servers.size()>0){
				int i =r.nextInt(servers.size());
				return servers.get(i);
			}
			return null;
			
		}
		
	}
	
	public static class MsgServer{
		
		private String serverId;
		
		private String serverUrl;

		public String getServerId() {
			return serverId;
		}

		public void setServerId(String serverId) {
			this.serverId = serverId;
		}

		public String getServerUrl() {
			return serverUrl;
		}

		public void setServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
		}
		
		
		
		
	}
	
	
	
	
}
