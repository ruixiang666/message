package cn.orgid.message.domain.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private Properties systemInfo = new Properties();
	
	private String uploadFilePath;
	
	private boolean devMode;
	
	private String systemInfoPath;
	

	public boolean isDevMode() {
		return devMode;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public String getUploadFilePath() {
		return uploadFilePath;
	}

	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
		
	}

	public String getSystemInfoPath() {
		return systemInfoPath;
	}

	public void setSystemInfoPath(String systemInfoPath) {
		
		this.systemInfoPath = systemInfoPath;
		try {
			FileInputStream fis;
			fis = new FileInputStream(systemInfoPath);
			systemInfo.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getServerId(){
		
		return systemInfo.getProperty("serverId");
		
	}
	
	public String getServerUrl(){
		
		return systemInfo.getProperty("serverUrl");
		
	}

}
