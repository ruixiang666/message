package message.test;

import org.springframework.beans.factory.annotation.Autowired;
import cn.orgid.message.domain.dao.message.ReportMessageDAO;

public class Test extends TestBase{
	
	
	
	@Autowired
	ReportMessageDAO rptMsgDao;
	
	@org.junit.Test
	public void test(){
		
		rptMsgDao.findTop200ByOrderByIdAsc();
		
	}

}
