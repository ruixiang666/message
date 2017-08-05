package cn.orgid.message.domain.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.orgid.common.exception.ApplicationException;
import cn.orgid.message.domain.dao.platform.ApplicationDAO;
import cn.orgid.message.domain.model.platform.Application;

/**
 * @Description 应用平台业务处理类
 * @date 2016年12月7日 下午3:46:47
 * @version 1.0
 */
@Service
public class ApplicationService {

	Logger log = Logger.getLogger(ApplicationService.class);

	@Autowired
	ApplicationDAO applicationDAO;

	/**
	 * 获取全部连接应用平台信息
	 * 
	 * @return
	 */
	public List<Application> queryApplicationList() {
		return applicationDAO.findAll();
	}

	/**
	 * 创建连接应用平台
	 * 
	 * @param app
	 */
	public void createApp(Application app) {
		app.build();
		app.refreshToken();
		applicationDAO.save(app);
	}

	/**
	 * 验证连接密钥
	 * 
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	public boolean validateApp(String appId, String appSecret) {
		log.debug("[应用平台连接]- appId：" + appId + ",appSecret：" + appSecret);
		Application app = applicationDAO.findByAppIdAndAppSecret(appId, appSecret);
		if (app != null) {
			log.info(app.toString());
			return true;
		}
		return false;
	}

	/**
	 * 根据密钥获取应用平台连接信息
	 * 
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	public Application findApplicationByAppIdAndSecret(String appId, String appSecret) {
		Application app = applicationDAO.findByAppIdAndAppSecret(appId, appSecret);
		return app;
	}

	/**
	 * 保存应用平台连接信息
	 * 
	 * @param app
	 */
	public void saveApp(Application app) {
		app.build();
		app.refreshToken();
		applicationDAO.save(app);
	}

	/**
	 * 验证应用平台连接凭证
	 * 
	 * @param appToken
	 * @return
	 */
	public boolean validateAppToken(String appToken) {
		Application app = applicationDAO.findByToken(appToken);
		if (app != null)
			return true;
		return false;
	}

	/**
	 * 根据id删除应用平台信息
	 * 
	 * @param id
	 */
	public void delApp(Long id) {
		applicationDAO.delete(id);
	}

	public static void main(String[] args) {

		//System.out.println(JSON.toJSONString("{id:'1'}"));
	}

	public void verifyApp(String appId, String appSecret) {
		
		Application app = findApplicationByAppIdAndSecret(appId, appSecret);
		if(app==null){
			throw new ApplicationException("app 验证失败");
		}
		
	}

}
