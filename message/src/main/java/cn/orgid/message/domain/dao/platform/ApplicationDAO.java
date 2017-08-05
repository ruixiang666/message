package cn.orgid.message.domain.dao.platform;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.orgid.message.domain.model.platform.Application;

public interface ApplicationDAO extends JpaRepository<Application,Long> {

	Application findByAppIdAndAppSecret(String appid, String appSecret);

	@Query("from Application a where a.token.token=?1")
	Application findByToken(String appToken);

}
