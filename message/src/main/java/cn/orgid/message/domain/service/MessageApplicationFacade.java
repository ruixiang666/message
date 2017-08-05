package cn.orgid.message.domain.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.orgid.common.exception.ApplicationException;
import cn.orgid.message.domain.model.client.Client;
import cn.orgid.message.domain.model.message.NotifyMessage;
import cn.orgid.message.domain.model.message.ReliableMessage.AcknowledgeInfo;
import cn.orgid.message.domain.model.message.ReportMessage;
import cn.orgid.message.domain.model.platform.Application;
import io.netty.channel.Channel;

@Service
public class MessageApplicationFacade {

	Logger log = Logger.getLogger(MessageApplicationFacade.class);

	@Autowired
	MessageService messageService;

	@Autowired
	ClientService clientService;

	@Autowired
	ApplicationService applicationService;
	
	

	/**
	 * 创建客户端
	 * @param appId
	 * @param appSecret
	 * @param client
	 * @return
	 */
	public Client createClient(String appId, String appSecret, Client client) {
		
		boolean b = applicationService.validateApp(appId, appSecret);
		if (!b) {
			throw new MessageApplicationException("app token invalid");
		}
		client.setAppId(appId);
		clientService.createClient(client);
		return client;
	}

	/**
	 * 获取客户端信息
	 * @param appId
	 * @param appSecret
	 * @param clientId
	 * @return
	 */
	public Client getClientForToken(String appId, String appSecret, Long clientId) {
		boolean b = applicationService.validateApp(appId, appSecret);
		if (!b) {
			throw new MessageApplicationException("appid or secret  invalid");
		}
		Client c = clientService.getClientForToken(clientId);
		if (c == null || c.getAppId() == null || !c.getAppId().equals(appId)) {
			throw new MessageApplicationException("  client not found for app");
		}
		return c;
	}

	/**
	 * 确认
	 * 
	 * @param requestMsg
	 * @param channel
	 * @return
	 */
	public String acknowledge(String requestMsg) {

		try {
			JSONObject obj = JSON.parseObject(requestMsg);
			AcknowledgeInfo info = obj.getObject("paras", AcknowledgeInfo.class);
			messageService.acknowledge(info.getMsgKey());
			Map<String, String> m = new HashMap<String, String>();
			m.put("action", Action.Type.Acknowledge.name());
			ResultBase r = ResultBase.success(m);
			return r.toJson();
		} catch (Throwable e) {
			e.printStackTrace();
			ResultBase r = ResultBase.error();
			r.setMsg(e.getMessage());
			return r.toJson();
		}

	}

	/**
	 * 注册客户端链接
	 * 
	 * @param requestMsg{action:"RegisterClient",
	 *            paras:{id:1,token:""}}
	 * @param channel
	 * @return { success:true}
	 * 
	 */
	public String registerClient(String requestMsg, Channel channel) {

		log.info(requestMsg);
		try {
			JSONObject obj = JSON.parseObject(requestMsg);
			Client client = obj.getObject("paras", Client.class);
			log.info(client.toClientInfo());
			clientService.registeClientConnection(client.getId(), client.getToken(), channel);
			Map<String, String> m = new HashMap<String, String>();
			m.put("action", Action.Type.RegisterClient.name());
			ResultBase r = ResultBase.success(m);
			return r.toJson();
		} catch (Throwable e) {
			e.printStackTrace();
			ResultBase r = ResultBase.error();
			r.setMsg(e.getMessage());
			return r.toJson();
		}

	}

	/**
	 * 注册平台客户端链接
	 * 
	 * @param requestMsg{action:"RegisterClient",
	 *            paras:{appId:"",appSecret:""}}
	 * @param channel
	 * @return { success:true}
	 * 
	 */
	public String registerPlatformClient(String requestMsg, Channel channel) {

		try {
			JSONObject obj = JSON.parseObject(requestMsg);
			this.validateApplication(obj);
			Application app = obj.getObject("paras", Application.class);
			clientService.registerPlatformClient(app.getAppId(), channel);
			Map<String, String> m = new HashMap<String, String>();
			m.put("action", Action.Type.RegisterPlatformClient.name());
			ResultBase r = ResultBase.success(m);
			return r.toJson();
		} catch (Throwable e) {
			ResultBase r = ResultBase.error();
			r.setMsg(e.getMessage());
			return r.toJson();
		}

	}

	/**
	 * 报告
	 * 
	 * @param request
	 *            action:"Report", paras:{id:1,token:"",content:"{}"}
	 * @return
	 */
	public String report(String request) {

		try {

			JSONObject obj = JSON.parseObject(request);
			Client client = obj.getObject("paras", Client.class);
			validateClient(client);
			ReportMessage msg = obj.getObject("paras", ReportMessage.class);
			msg.setClientId(client.getId());
			messageService.report(msg);

			Map<String, String> m = new HashMap<String, String>();
			m.put("action", Action.Type.Report.name());
			ResultBase r = ResultBase.success(m);
			return r.toJson();
		} catch (Throwable e) {
			e.printStackTrace();
			ResultBase r = ResultBase.error();
			e.printStackTrace();
			r.setMsg(e.getMessage());
			return r.toJson();
		}

	}

	/**
	 * 验证连接
	 */
	private void validateClient(Client client) {
		
		boolean b=clientService.validate(client.getId(), client.getToken());
		if(!b){
			throw new ApplicationException("clientId or clientToken error ");
		}
		
	}
	

	/**
	 * 通知
	 * 
	 * @param requestMsg
	 *            { action:"Notify", paras:{ appId:"",appSecret:""
	 *            ,toClientId:2, content:{"type":"rent_car_reply","":"","":""}
	 *            }}
	 * @param reliable 
	 * 
	 * @return { success:true}
	 */
	public String notify(String requestMsg, boolean reliable) {

		try {
			JSONObject obj = JSON.parseObject(requestMsg);
			validateApplication(obj);
			NotifyMessage msg = obj.getObject("paras", NotifyMessage.class);
			messageService.notify(msg,true);
			Map<String, String> m = new HashMap<String, String>();
			m.put("action", Action.Type.Notify.name());
			ResultBase r = ResultBase.success(m);
			return r.toJson();
		} catch (Throwable e) {
			e.printStackTrace();
			ResultBase r = ResultBase.error();
			r.setMsg(e.getMessage());
			return r.toJson();
		}

	}
	
	
	
	
	
	
	public static class ResultBase {

		public static ResultBase success() {

			ResultBase r = new ResultBase();
			r.success = true;
			return r;
		}

		public static ResultBase success(Object obj) {

			ResultBase r = new ResultBase();
			r.value = obj;
			r.success = true;
			return r;
		}

		public static ResultBase error() {

			ResultBase r = new ResultBase();
			r.success = false;
			return r;

		}

		public String toJson() {

			return JSON.toJSONString(this);

		}

		boolean success;

		String msg;

		Object value;

		private String action;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

	}

	private void validateApplication(JSONObject jsonObject) {

		Application app = jsonObject.getObject("paras", Application.class);
		boolean b = applicationService.validateApp(app.getAppId(), app.getAppSecret());
		if (!b) {
			throw new MessageApplicationException("appid or secret invalid");
		}
	}

	

	

	
	
}
