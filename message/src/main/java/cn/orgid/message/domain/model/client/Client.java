package cn.orgid.message.domain.model.client;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;
import cn.orgid.message.domain.service.MessageApplicationException;
import cn.orgid.message.domain.util.MD5;

/**
 * @Description 连接客户端类
 * @date 2016年12月7日 下午3:49:51
 * @version 1.0
 */
@Entity
@Table(name = "t_client")
public class Client extends EntityBase {

	public static final String PlatformClientTag = "Platform";

	private static final long serialVersionUID = 1L;
	/** 连接密钥 */
	private String appId;
	/** 连接端名称 */
	private String name;
	/** 连接凭证 */
	private String token;
	/** 连接凭证时间 */
	private Date tokenTime;
	/** 连接对象类型 */
	private String tag;
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenTime() {
		return tokenTime;
	}

	public void setTokenTime(Date tokenTime) {
		this.tokenTime = tokenTime;
	}

	public void refreshToken() {

		this.token = MD5.md5(UUID.randomUUID().toString());
		this.tokenTime = new Date();

	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		if (PlatformClientTag.equals(tag)) {
			throw new MessageApplicationException("不能以常规方式设置客户端为平台客户端");
		}
		this.tag = tag;
	}

	public void makePlatformClient() {
		this.tag = PlatformClientTag;
	}

	public boolean isTokenValid(String connectionToken) {
		return connectionToken != null && connectionToken.equalsIgnoreCase(token);
	}

	@Override
	public String toString() {
		return "Client [appId=" + appId + ", name=" + name + ", token=" + token + ", tokenTime=" + tokenTime + ", tag="
				+ tag + "]";
	}
	
	public String toClientInfo() {
		return "连接客户端 [连接对象密钥：" + appId + ", 连接对象名称：" + name + ", 连接凭证：" + token + ", 连接凭证时间：" 
				+ tokenTime + ", 对象类型："+ tag + "]";
	}

}
