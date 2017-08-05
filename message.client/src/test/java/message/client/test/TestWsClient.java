package message.client.test;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.orgid.message.client.AppClient;
import cn.orgid.message.client.IceCandidateInfo;
import cn.orgid.message.client.JsonCommonResult;
import cn.orgid.message.client.SdpInfo;
import cn.orgid.message.client.SendingMessage;
import cn.orgid.message.client.TalkSession;
import cn.orgid.message.client.WebSocketPlatformClient;
import cn.orgid.message.client.WebSocketHttpClient;


public class TestWsClient extends TestBase{

	Logger logger = Logger.getLogger(TestWsClient.class);
	
	@Autowired
	WebSocketPlatformClient socketClient;
	
	@Autowired
	WebSocketHttpClient httpClient;
	

	@Test
	public void test() throws IOException{
		
		logger.info("111");
		AppClient c = new AppClient();
		c.setName("zy");
		c.setTag(AppClient.Tag.Driver.name());
		
		//创建一个消息客户端
		AppClient c1= httpClient.createClient(c);
		
		AppClient c2 = new AppClient();
		c.setName("zy2");
		c.setTag(AppClient.Tag.Driver.name());
		c2=httpClient.createClient(c2);
		//根据客户端id查找客户端最新信息
		c1=httpClient.getClientToken(c1.getId());
		
		FileInputStream fis = 
				new FileInputStream("./test_file/20106635319267.jpg");
		byte[] bs = new byte[fis.available()];
		fis.read(bs);
		String s = Base64.getEncoder().encodeToString(bs);
		System.out.println(s);
		
		socketClient.sendNotifyMessage(11L, s);
		
		//关联客户端和物理链接
		socketClient.sendRegisterClientMessage(c1.getId(), c1.getToken());
		socketClient.sendRegisterClientMessage(c2.getId(), c2.getToken());
		TalkSession t = httpClient.createP2PTalkSession(c1.getId());
		httpClient.addPeerConnectionToTalkSession(t.getId(),c2.getId());
		IceCandidateInfo info = new IceCandidateInfo();
		info.setSdp("SDP");
		info.setClientId(c2.getId());
		
		info.setSdpMid("111");
		info.setSdpMLineIndex(1);
		info.setSessionId(t.getId());
		SdpInfo info2 = new SdpInfo();
		info2.setFromClientId(c1.getId());
		info2.setSdp("sdp");
		info2.setSessionId(t.getId());
		//info2.setToClientId(c2.getId());
		
		
		
		socketClient.send(SendingMessage.PublishCandidateMessage(c2.getId(), c2.getToken(), info));
		socketClient.send(SendingMessage.PublishSdpMessage(c1.getId(), c1.getToken(), info2));
		long t1=System.currentTimeMillis();
		long t2=System.currentTimeMillis();
		
		//socketClient.sendReportMessage(c1.getId(), c1.getToken(), "{name:\""+s+"\"}");
//		try {
//			Thread.currentThread().sleep(1000*600);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("t2-t1:" +(System.currentTimeMillis()-t1));
		
		
	}
	
	
	
	public static class  T1{
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	
	
	
	
	
}
