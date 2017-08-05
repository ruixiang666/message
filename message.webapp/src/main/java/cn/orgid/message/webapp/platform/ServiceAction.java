package cn.orgid.message.webapp.platform;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.orgid.common.exception.ApplicationException;
import cn.orgid.common.file.FileUtil;
import cn.orgid.message.domain.config.Config;
import cn.orgid.message.domain.model.client.Client;
import cn.orgid.message.domain.model.client.ClientConnection;
import cn.orgid.message.domain.model.file.FileAccessKey;
import cn.orgid.message.domain.model.mq.MsgServer;
import cn.orgid.message.domain.service.ApplicationService;
import cn.orgid.message.domain.service.ClientService;
import cn.orgid.message.domain.service.MessageApplicationFacade;
import cn.orgid.message.domain.service.MsgServerService;
import cn.orgid.message.domain.service.UploadFileService;
import cn.orgid.message.webapp.JsonCommonResult;

@Controller
@RequestMapping("/service")
public class ServiceAction {

	Logger log = Logger.getLogger(ServiceAction.class);

	@Autowired
	ClientService clientService;
	
	@Autowired
	MsgServerService msgQueueService;

	@Autowired
	MessageApplicationFacade applicationFacade;
	
	@Autowired
	ApplicationService appService;
	
	@Autowired
	Config config;
	
	@Autowired
	UploadFileService uploadFileService;
	
	
	
	
	@RequestMapping("get_online_app")
	public String getOnlineApp(String clientTag, ModelMap map) {
		List<ClientConnection> list = clientService.getOnlineAPP(clientTag);
		map.put("json", JSON.toJSONString(list));
		return "/json/json";
	}

	@RequestMapping("test_network")
	public String testNetwork(ModelMap map) {

		JsonCommonResult r = JsonCommonResult.success();
		map.put("json", r);
		return "/json/json";

	}

	@RequestMapping("create_client")
	public String createClient(String appId, String appSecret, Client client, ModelMap map) {

		try {
			Client c = applicationFacade.createClient(appId, appSecret, client);
			log.info(c.toString());
			map.put("json", JSON.toJSONString(new ClientWrapper(c)));
		} catch (RuntimeException e) {
			e.printStackTrace();
			JsonCommonResult r = JsonCommonResult.error(e.getMessage());
			map.put("json", r);
		}
		return "/json/json";

	}

	@RequestMapping("get_client_token")
	public String getClientToken(String appId, String appSecret, Long clientId, ModelMap map) {

		try {
			Client c = applicationFacade.getClientForToken(appId, appSecret, clientId);
			map.put("json", JSON.toJSONString(new ClientWrapper(c)));
		} catch (RuntimeException e) {
			JsonCommonResult r = JsonCommonResult.error(e.getMessage());
			map.put("json", r);
		}
		return "/json/json";

	}
	
	@RequestMapping("upload_file")
	public String uploadFile(String appId, String appSecret,@RequestParam("file") MultipartFile file,ModelMap map){
		
		try {
			String fileName=file.getOriginalFilename();
			byte[] fileContent=file.getBytes();
			String uri = uploadFileService.uploadFile(appId, appSecret, fileName, fileContent);
			map.put("json", JsonCommonResult.success(uri));
		} catch (RuntimeException e) {
			JsonCommonResult r = JsonCommonResult.error(e.getMessage());
			map.put("json", r);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/json/json";
		
		
	}

	
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("get_file_access_key")
	public String getFileAccessKey(String appId, String appSecret,ModelMap map){
		
		try{
			FileAccessKey accessKey = uploadFileService.fileAccessKey(appId, appSecret);
			map.put("json", JsonCommonResult.success(accessKey));
		}catch(Throwable e){
			JsonCommonResult r = JsonCommonResult.error(e.getMessage());
			map.put("json", r);
		}
		return "/json/json";
		
	}
	
	@RequestMapping("get_file")
	public void getFile(String f,String k,HttpServletResponse response){
		
		try{
			byte[] bs = uploadFileService.getUploadFileData(f,k);
			response.setContentType(FileUtil.getMimeTypeName(f));
			response.getOutputStream().write(bs);
		}catch(Throwable e){
			e.printStackTrace();
			try {
				response.getOutputStream().write(new byte[0]);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	@RequestMapping("get_server_list")
	public String getServerList(ModelMap map){
		
		try{
			List<MsgServer> servers = msgQueueService.getMsgServerList();
			map.put("json", JsonCommonResult.success(servers));
		}catch(ApplicationException e){
			map.put("json", JsonCommonResult.error(e.getMessage()));
		}
		return "/json/json";
		
	}

	public static class ClientWrapper extends JsonCommonResult {
		private Client c;

		public ClientWrapper(Client client) {
			c = client;
			this.setSuccess(true);
		}

		public String getAppId() {
			return c.getAppId();
		}

		public void setAppId(String appId) {
			c.setAppId(appId);
		}

		public String getName() {
			return c.getName();
		}

		public Long getId() {
			return c.getId();
		}

		public void setName(String name) {
			c.setName(name);
		}

		public String getToken() {
			return c.getToken();
		}

		public void setToken(String token) {
			c.setToken(token);
		}

		public void setId(Long id) {
			c.setId(id);
		}

		public Date getTokenTime() {
			return c.getTokenTime();
		}

		public Integer getVersion() {
			return c.getVersion();
		}

		public void setTokenTime(Date tokenTime) {
			c.setTokenTime(tokenTime);
		}

		public void setVersion(Integer version) {
			c.setVersion(version);
		}

		public void refreshToken() {
			c.refreshToken();
		}

		public String getTag() {
			return c.getTag();
		}

		public void setTag(String tag) {
			c.setTag(tag);
		}

	}

}
