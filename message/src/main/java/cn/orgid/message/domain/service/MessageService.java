package cn.orgid.message.domain.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.orgid.message.domain.component.ChannelComponent;
import cn.orgid.message.domain.dao.client.ClientConnectionDAO;
import cn.orgid.message.domain.dao.client.ClientDAO;
import cn.orgid.message.domain.dao.message.NotifyMessageDAO;
import cn.orgid.message.domain.dao.message.ReliableMessageDAO;
import cn.orgid.message.domain.dao.message.ReliableMessageQuery;
import cn.orgid.message.domain.dao.message.ReportMessageDAO;
import cn.orgid.message.domain.dao.platform.ApplicationDAO;
import cn.orgid.message.domain.model.client.Client;
import cn.orgid.message.domain.model.client.ClientConnection;
import cn.orgid.message.domain.model.message.ReliableMessage;
import cn.orgid.message.domain.model.message.ReportMessage;

@Service
public class MessageService {

	private static final int PAGE_SIZE = 200;

	Logger logger = Logger.getLogger(MessageService.class);

	@Autowired
	ClientDAO clientDAO;

	@Autowired
	ApplicationDAO applicationDAO;

	@Autowired
	ClientConnectionDAO clientConnectionDAO;

	@Autowired
	ChannelComponent channelComponent;

	@Autowired
	NotifyMessageDAO notifyMessageDAO;

	@Autowired
	ReportMessageDAO reportMessageDAO;

	@Autowired
	ReliableMessageDAO reliableMessageDAO;

	

	/**
	 * 确认已经收到消息
	 * 
	 * @param key
	 */
	public void acknowledge(String key) {

		reliableMessageDAO.deleteByMsgKey(key);

	}

	public void notify(ReliableMessage msg, boolean initMsg) {

		if (!initMsg) {
			List<ClientConnection> connections = clientConnectionDAO.findByClientId(msg.getToClientId());
			int count = 0;
			for (ClientConnection c : connections) {
				boolean b = channelComponent.sendMsg(c.getChannelId(), msg.getContentWithMsgKey());
				if (b)
					count++;
			}
			msg.send(count);
		}
		reliableMessageDAO.save(msg);
		

	}

	public void resendReliableMessage() {

		for (int i = 0;; i++) {
			Pageable pageable = new PageRequest(i, (i + 1) * PAGE_SIZE);
			Page<ReliableMessage> reliableMessagePage = reliableMessageDAO.findAll(
					ReliableMessageQuery.queryReliableMessageSpecification(ReliableMessage.MAX_RETRY_COUNT), pageable);
			
			resendNotifyMsgIfNeed(reliableMessagePage);
			if (!reliableMessagePage.hasNext()) {
				break;
			}
		}

	}

	private void resendNotifyMsgIfNeed(Page<ReliableMessage> reliableMessagePage) {

		List<ReliableMessage> messageList = reliableMessagePage.getContent();
		for (ReliableMessage notifyMessage : messageList) {
			if (notifyMessage.needNotify()) {
				try {
					this.notify(notifyMessage, false);
				} catch (Throwable e) {
					logger.warn(e.getMessage());
				}
			}
		}

	}

	@Scheduled(cron = "0/5 * * * * ?")
	public void processNotifyMessage() {

		while (true) {
			try {
				resendReliableMessage();
				Thread.sleep(3000);
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}

	}

	/**
	 * 上报消息
	 * 
	 * @param msg
	 */
	public void report(ReportMessage msg) {

		Client client = clientDAO.findOne(msg.getClientId());
		msg.setAppId(client.getAppId());
		reportMessageDAO.save(msg);

	}

	@Scheduled(cron = "1/60 * * * * ?")
	public void processReportMsg() {

		for (int i = 0;;i++) {
			try {
				Pageable pageable = new PageRequest(i, (i + 1) * PAGE_SIZE);
				Page<ReportMessage> page = reportMessageDAO.findAll(pageable);
				List<ReportMessage> messages = page.getContent();
				for (ReportMessage reportMessage : messages) {
					boolean b = channelComponent.report(reportMessage.getAppId(), reportMessage.getContent());
					if (b) {
						reportMessageDAO.delete(reportMessage.getId());
					}
				}
				Thread.sleep(1000);
				if(!page.hasNext()){
					return;
				}
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}

	}

}
