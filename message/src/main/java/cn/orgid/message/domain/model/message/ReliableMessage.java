package cn.orgid.message.domain.model.message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.orgid.common.model.BooleanConverter;
import cn.orgid.message.domain.model.EntityBase;

@Entity
@Table(name = "t_reliable_msg")
public class ReliableMessage extends EntityBase {

	private static final int RECEIVED_TIME_OUT = 3000;

	public static final int MAX_RETRY_COUNT = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String msgKey;

	@Column(length = 2000)
	private String content;

	private int sendCount;

	private int retryCount;

	@Convert(converter = BooleanConverter.class)
	private boolean acknowledge;

	private Date sendTime;

	private Long toClientId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void acknowledge() {

		this.acknowledge = true;

	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public void send(int count) {

		setSendTime(new Date());
		retryCount++;

	}

	public String getMsgKey() {

		if (StringUtils.isBlank(msgKey)) {
			msgKey = UUID.randomUUID().toString();
		}
		return msgKey;

	}

	public void setMsgKey(String msgKey) {

		this.msgKey = msgKey;

	}

	public boolean needNotify() {

		if(sendTime==null)
			return true;
		if (acknowledge  ) {
			return false;
		}
		long t1 = sendTime.getTime();
		long t2 = RECEIVED_TIME_OUT * retryCount;
		long t3 = System.currentTimeMillis() ;
		return (t3-t1)>t2;

	}

	public String getContentWithMsgKey() {

		JSONObject obj = JSON.parseObject(content);
		obj.put("msgKey", getMsgKey());
		return obj.toJSONString();

	}

	public Long getToClientId() {
		return toClientId;
	}

	public void setToClientId(Long toClientId) {
		this.toClientId = toClientId;
	}

	public static class AcknowledgeInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String msgKey;

		public String getMsgKey() {
			return msgKey;
		}

		public void setMsgKey(String msgKey) {
			this.msgKey = msgKey;
		}

	}

	public boolean overstepMaxRetry() {

		return retryCount >= MAX_RETRY_COUNT;

	}

}
