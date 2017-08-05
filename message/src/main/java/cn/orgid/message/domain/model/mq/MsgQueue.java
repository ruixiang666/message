package cn.orgid.message.domain.model.mq;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;

/**
 * 分布式消息队列
 * @author freedog
 *
 */
@Entity
@Table(name="t_msg_queue")
public class MsgQueue extends EntityBase {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(columnDefinition="text")
	private String msgContent;
	
	private Date createDate;
	
	private Long toConsume;
	
	private Long consumed;

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getToConsume() {
		return toConsume;
	}

	public void setToConsume(Long toConsume) {
		this.toConsume = toConsume;
	}

	public Long getConsumed() {
		return consumed;
	}

	public void setConsumed(Long consumed) {
		this.consumed = consumed;
	}

	
	
	public boolean allConsumed(){
		
		return consumed>=toConsume;
		
	}
	
	public boolean expired(){
		
		return createDate==null||System.currentTimeMillis()-createDate.getTime()>1000*60;
		
	}

	public void consume() {
		
		consumed++;
		
	}

	@Override
	public String toString() {
		return "MsgQueue [msgContent=" + msgContent + ", createDate="
				+ createDate + ", toConsume=" + toConsume + ", consumed="
				+ consumed + "]";
	}
	
	
	

}
