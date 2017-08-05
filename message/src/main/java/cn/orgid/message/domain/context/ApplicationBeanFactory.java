package cn.orgid.message.domain.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationBeanFactory implements ApplicationContextAware{

	
	private static ApplicationContext applicationContext;
	
	
	
	public  static ApplicationContext getApplicationContext(){
		
		return applicationContext;
		
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		ApplicationBeanFactory.applicationContext=applicationContext;
		
	}
	
	public static <T>  T  get(Class<T> clazz){
		
		return applicationContext.getBean(clazz);
		
	}

}
