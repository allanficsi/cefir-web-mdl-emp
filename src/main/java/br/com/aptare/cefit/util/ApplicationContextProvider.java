package br.com.aptare.cefit.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public static <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		ApplicationContextProvider.applicationContext = ctx;
	}
	
	public void destroy(){
		applicationContext = null;
	}

}