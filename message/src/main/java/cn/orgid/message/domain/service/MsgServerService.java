package cn.orgid.message.domain.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import cn.orgid.message.domain.config.Config;
import cn.orgid.message.domain.dao.mq.MsgServerDAO;
import cn.orgid.message.domain.model.mq.MsgServer;

@Service
public class MsgServerService {	

	@Autowired
	MsgServerDAO msgQueueSubscriberDAO;	

	@Autowired
	Config config;

	@Scheduled(cron = "1 * * * * ?")
	public void registerServer() {

		try {
			String serverId = config.getServerId();
			String serverUrl = config.getServerUrl();
			MsgServer subscriber = msgQueueSubscriberDAO.findByServerId(serverId);
			if (subscriber == null) {
				subscriber = new MsgServer();
			}
			subscriber.setServerId(serverId);
			subscriber.setServerUrl(serverUrl);
			subscriber.setRegisterTime(new Date());
			msgQueueSubscriberDAO.save(subscriber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "2 * * * * ?")
	public void unRegisterServer() {
		
		List<MsgServer> subscriberList = msgQueueSubscriberDAO.findAll();
		for (MsgServer msgServer : subscriberList) {
			if(!msgServer.active()){
				msgQueueSubscriberDAO.delete(msgServer);
			}
		}

	}

	public List<MsgServer> getMsgServerList() {

		return msgQueueSubscriberDAO.findAll();

	}

}
