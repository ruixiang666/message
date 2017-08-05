package cn.orgid.message.domain.model.mq;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;

@Entity
@Table(name="t_msg_consum_record")
public class MsgConsumeRecord extends EntityBase{
	

	@Override
	public String toString() {
		return "MsgConsumeRecord [msgId=" + msgId + ", subscriberId="
				+ subscriberId + ", consumed=" + consumed + ", consumeTime="
				+ consumeTime + "]";
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long msgId;
	
	private Long subscriberId;
	
	private boolean consumed;
	
	
	private Date consumeTime;


	public Long getMsgId() {
		return msgId;
	}


	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}


	public Long getSubscriberId() {
		return subscriberId;
	}


	public void setSubscriberId(Long subscriberId) {
		this.subscriberId = subscriberId;
	}


	public boolean isConsumed() {
		return consumed;
	}


	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}


	public Date getConsumeTime() {
		return consumeTime;
	}


	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}


	public void consume() {
		
		this.consumed=true;
		
	}
	
	
	

}
