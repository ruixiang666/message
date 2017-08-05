package cn.orgid.message.domain.model.platform;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.common.util.PasswordUtil;
import cn.orgid.message.domain.model.EntityBase;

@Entity
@Table(name="t_manager")
public class Manager extends EntityBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String phone;
	
	private String passwd;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean isPasswdValid(String initPasswd) {
		
		return PasswordUtil.match(initPasswd, passwd);
		
	}
	
	
	
	

}
