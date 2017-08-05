package cn.orgid.message.domain.dao.platform;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.platform.Manager;

public interface ManagerDAO extends JpaRepository<Manager, Long> {

	//Manager findByPhoneAndPasswd(String phone, String passwd);

	Manager findByPhone(String phone);

}
