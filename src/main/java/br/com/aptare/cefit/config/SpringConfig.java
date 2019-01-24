package br.com.aptare.cefit.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import br.com.aptare.cefit.util.ApplicationContextProvider;

@Configuration
public class SpringConfig
{

	@Lazy(false)
	@Bean(name = "applicationContextProvider")
	public ApplicationContextProvider applicationContextProvider(ApplicationContext applicationContext)
	{
		ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
		applicationContextProvider.setApplicationContext(applicationContext);
		return applicationContextProvider;
	}

	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

}