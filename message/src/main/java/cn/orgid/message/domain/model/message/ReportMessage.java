package cn.orgid.message.domain.model.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;

@Entity
@Table(name = "t_report_message")
public class ReportMessage extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Long clientId;
	
	private String appId;

	@Column(length = 2000)
	private String content;
	
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
