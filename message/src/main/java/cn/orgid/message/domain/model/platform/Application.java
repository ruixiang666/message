package cn.orgid.message.domain.model.platform;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;

/**
 * @Description 连接应用类
 * @date 2016年12月7日 下午3:43:11
 * @version 1.0
 */
@Entity
@Table(name = "t_app")
public class Application extends EntityBase {

	private static final long serialVersionUID = 1L;
	/** 密钥id */
	private String appId;
	/** 密钥 */
	private String appSecret;
	/** 联系人电话 */
	private String contactPhone;
	/** 联系人邮箱 */
	private String contactEmail;
	/** 联系人姓名 */
	private String contactName;
	/** 组织名称 */
	private String orgName;
	/** 组织地址 */
	private String orgAddress;

	private ApplicationToken token;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public ApplicationToken getToken() {
		return token;
	}

	public void setToken(ApplicationToken token) {
		this.token = token;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public void build() {
		this.appId = UUID.randomUUID().toString().replace("-", "");
		this.appSecret = UUID.randomUUID().toString().replace("-", "");
	}

	public void refreshToken() {
		if (token == null) {
			token = new ApplicationToken();
		}
		token.setToken(UUID.randomUUID().toString().replace("-", ""));
		token.setCreateTime(new Date());
		token.setExpire(-1);
	}

	@Override
	public String toString() {
		return "[应用平台连接消息服务器] 联系人电话：" + contactPhone + ", 联系人邮箱：" + contactEmail + ", 联系人姓名：" + contactName + ", 组织名称："
				+ orgName + ", 组织地址：" + orgAddress;
	}

}
