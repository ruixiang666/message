package cn.orgid.message.domain.model.mq;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;

/**
 * 分布式消息队列订阅者
 * 
 * @author freedog
 *
 */
@Entity
@Table(name = "t_msg_server")
public class MsgServer extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serverId;

	private Date registerTime;

	private String serverUrl;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public boolean isSameServer(String serverId2) {

		return this.serverId != null && this.serverId.equals(serverId2);

	}

	public boolean active() {
		
		if (registerTime == null || System.currentTimeMillis() - registerTime.getTime() > 1000 * 120)
			return false;
		return true;
		

	}

}
