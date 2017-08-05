package cn.orgid.message.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {
	
	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void println(String msg,boolean debug){
		
		if(debug){
			
			//System.out.println(sf.format(new Date())+" ws.debug:"+msg);
		}
		
	}
	
}
