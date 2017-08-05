package message.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.orgid.message.domain.dao.platform.ApplicationDAO;
import cn.orgid.message.domain.model.client.Client;
import cn.orgid.message.domain.model.client.ClientConnection;
import cn.orgid.message.domain.model.platform.Application;
import cn.orgid.message.domain.service.ClientService;
import cn.orgid.message.domain.service.ApplicationService;
import cn.orgid.message.domain.service.MessageApplicationException;
import cn.orgid.message.domain.service.MessageApplicationFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={ "classpath*:/springContext.xml" })
public class TestBase {
	
//	@Autowired
//	ApplicationService platformService;
//	
//	@Autowired
//	ClientService clientService;
//	
//	@Autowired
//	MessageApplicationFacade applicationFacade;
//	
//	@Autowired
//	ApplicationDAO applicationDAO;
//	
//	private Application app;
//	
//	private Client client;
	
	
	@Test
	public void createApp(){
		
		String s="{'action':'BroadCast','paras':{'appId':'59c2b964383540df92cbc430cd2843c1','appSecret':'a4749057794746d5b953dc2493a3403f','content':{'name':'1111'},'toClientTag':'Driver'},'registerMsg':false}";
//		applicationFacade.broadCast(s);
		//applicationFacade.registerPlatformClient(requestMsg, channel)
		
		
	}
	
	@Test
	public void createApplication(){
		Application a = new Application();
//		a.build();
//		a.refreshToken();
//		applicationDAO.save(a);
	}
	
	
	
//	@Test
//	public void createClient(){
//		client = new Client();
//		applicationFacade.createClient("463bd38e86ba4ae0b3bf97dc22896382", "81e62312eecf476e85d6ab472ce438fe", client);
//	}
//	
	
}