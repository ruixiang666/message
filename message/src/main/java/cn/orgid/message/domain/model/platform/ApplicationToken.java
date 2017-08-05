package cn.orgid.message.domain.model.platform;

import java.util.Date;

@javax.persistence.Embeddable
public class ApplicationToken {
	
	private String token;
	
	private Date createTime;
	
	private int expire;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}
	
	
}
