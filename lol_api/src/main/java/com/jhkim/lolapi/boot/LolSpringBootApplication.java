package com.jhkim.lolapi.boot;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages="com.jhkim")
@MapperScan(basePackages="com.jhkim.**.dao")
@EnableAsync
@EnableScheduling
public class LolSpringBootApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	    return builder.sources(LolSpringBootApplication.class);
	  }

	
	public static void main(String[] args) {
		
		new SpringApplicationBuilder(LolSpringBootApplication.class).properties(
                "spring.config.location=" +
                        "file:/home/rhdldn/lol-api/application-local.yml"
        ).run(args);		
	}

	@Override
	public void run(String... args) throws Exception {
		
		//weatherInfoMngService.createWeatherInfo();
	}		
	
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		
		dataSourceTransactionManager.setDataSource(dataSource);
		
		return dataSourceTransactionManager;
	}
	

}
