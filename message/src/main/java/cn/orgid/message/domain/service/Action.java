package cn.orgid.message.domain.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.orgid.common.spring.BeansFactory;
import cn.orgid.message.domain.service.MessageApplicationFacade.ResultBase;
import io.netty.channel.Channel;

public  abstract class Action {

	public static enum Type {
		RegisterClient, Notify,RegisterPlatformClient, Report, Acknowledge,ConnectionCheck, PublishCandidate, PublishSDP;
	}

	
	Channel channel;
	
	String request;
	


	public static Action unknowAction(String msg){
		return  new UnknowAction(msg);
	}
	
	
	public static Action create(String msg,Channel channel) {
		
		Action action = null;
		try {
			JSONObject o = JSON.parseObject(msg);
			//注册一般客户端
			if (Type.RegisterClient.name().equalsIgnoreCase(o.getString("action"))) {
				action= new RegisterClientAction(msg);
			}
			//注册平台客户端
			if (Type.RegisterPlatformClient.name().equalsIgnoreCase(o.getString("action"))) {
				action= new RegisterPlatformClientAction(msg);
				
			}
			
			//定向通知
			if (Type.Notify.name().equalsIgnoreCase(o.getString("action"))) {
				action= new NotifyAction(msg);
				
			}
			//确认已经接收到通知消息
			if (Type.Acknowledge.name().equalsIgnoreCase(o.getString("action"))) {
				action= new AcknowledgeAction(msg);
			}
			
			//上报信息
			if (Type.Report.name().equalsIgnoreCase(o.getString("action"))) {
				action= new ReportAction(msg);
			}
			if(Type.ConnectionCheck.name().equalsIgnoreCase(o.getString("action"))){
				action= new ConnectionCheckAction();
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(action==null){
			action= new UnknowAction(msg);
		}
		action.channel=channel;
		action.request=msg;
		return  action;
		
	}
	
	

	public MessageApplicationFacade getMessageApplicationFacade() {

		return BeansFactory.get(MessageApplicationFacade.class);

	}

	public abstract String execute();
	
	
	public static class ConnectionCheckAction extends Action {

		@Override
		public String execute() {
			Map<String, String> m = new HashMap<String, String>();
			m.put("action", Action.Type.ConnectionCheck.name());
			ResultBase r = ResultBase.success(m);
			return r.toJson();
		}
		
	}
	
	

	/**
	 * 注册客户端
	 * 
	 * @author freedog
	 *
	 */
	public static class RegisterClientAction extends Action {

		private String request;

		public RegisterClientAction(String request) {
			this.request = request;
		}
		
		

		@Override
		public String execute() {
			
			return getMessageApplicationFacade().registerClient(request, channel);

		}

	}
	
	public static class AcknowledgeAction extends Action {

		private String request;

		public AcknowledgeAction(String request) {
			this.request = request;
		}

		@Override
		public String execute() {

			return getMessageApplicationFacade().acknowledge(request);

		}

	}

	public static class NotifyAction extends Action {

		private String request;

		public NotifyAction(String request) {
			this.request = request;
		}

		@Override
		public String execute() {
			
			//publishMsgIfNeed();
			boolean reliable=channel!=null;
			return getMessageApplicationFacade().notify(request,reliable);

		}

	}

	

	public static class ReportAction extends Action {

		private String request;

		public ReportAction(String request) {
			this.request = request;
		}

		@Override
		public String execute() {
			
			return getMessageApplicationFacade().report(request);

		}

	}

	public static class RegisterPlatformClientAction extends Action {

		private String request;

		public RegisterPlatformClientAction(String request) {
			this.request = request;
		}

		@Override
		public String execute() {
			
			return getMessageApplicationFacade().registerPlatformClient(request, channel);

		}

	}
	
	
	
	
	

	public static class UnknowAction extends Action {

		private String request;

		public UnknowAction(String request) {
			this.request = request;
		}

		@Override
		public String execute() {
			ResultBase r = ResultBase.error();
			r.setMsg("错误请求:" + request);
			return r.toJson();
		}

	}

	

}
