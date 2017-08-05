package cn.orgid.message.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.orgid.common.exception.ApplicationException;
import cn.orgid.common.file.FileUtil;
import cn.orgid.message.domain.config.Config;
import cn.orgid.message.domain.dao.file.FileAccessKeyDAO;
import cn.orgid.message.domain.dao.platform.ApplicationDAO;
import cn.orgid.message.domain.model.file.FileAccessKey;
import cn.orgid.message.domain.model.platform.Application;

@Service
public class UploadFileService {
	
	@Autowired
	FileAccessKeyDAO fileAccessKeyDAO;
	
	
	@Autowired
	ApplicationDAO applicationDAO;
	
	@Autowired
	Config config;
	
	public void validAccessKey(String accessKey) {
		
		FileAccessKey fileAccessKey = fileAccessKeyDAO.findByAccessKey(accessKey);
		if(fileAccessKey==null||!fileAccessKey.isValid()){
			throw new ApplicationException("accessKey error");
		}
		
	}
	
	public FileAccessKey fileAccessKey(String appId, String appSecret){
		
		verifyApp(appId,appSecret);
		FileAccessKey accessKey = new FileAccessKey();
		accessKey.initAccessKey();
		fileAccessKeyDAO.save(accessKey);
		return accessKey;
		
	}
	
	public  String uploadFile(String appId, String appSecret, String fileName, byte[] fileContent) {
		
		verifyApp(appId,appSecret);
		String uri =FileUtil.create(config.getUploadFilePath(),fileContent,fileName);
		return uri;
		
	}
	
	private void verifyApp(String appId, String appSecret) {
		
		Application app = findApplicationByAppIdAndSecret(appId, appSecret);
		if(app==null){
			throw new ApplicationException("app 验证失败");
		}
		
	}
	
	public Application findApplicationByAppIdAndSecret(String appId, String appSecret) {
		
		Application app = applicationDAO.findByAppIdAndAppSecret(appId, appSecret);
		return app;
		
	}

	public byte[] getUploadFileData(String f, String k) {
		
		validAccessKey(k);
		byte[] bs = FileUtil.getFileData(config.getUploadFilePath(),f);
		return bs;
		
	}
	
}
