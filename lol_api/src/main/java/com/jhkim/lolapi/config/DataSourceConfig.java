package com.jhkim.lolapi.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(basePackages= {"com.jhkim.lolapi"})
@EnableTransactionManagement(proxyTargetClass = true)
public class DataSourceConfig {

	 public static final String PORTAL_DATASOURCE = "portalDS";
	 
	 public static final String GIS_DATASOURCE = "gisDS";
	 
	 
	 @Value("${spring.datasource.driverClassName}")
	 private String className;
	 
	 @Value("${spring.datasource.url}")
	 private String dbUrl;
	 
	 @Value("${spring.datasource.username}")
	 private String userName;
	 
	 @Value("${spring.datasource.password}")
	 private String password;
	 
	 @Value("${spring.datasource.dbcp2.test-on-borrow}")
	 private Boolean testOnBorrow;
	 
	 @Value("${spring.datasource.dbcp2.validation-query}")
	 private String validationQuery;	 
	 
	 @Bean
	 public DataSource portalDataSourceOne() {	
		 
		 DataSource dataSource = new DataSource();

		 dataSource.setDriverClassName(className);		 
		 
		 dataSource.setUrl(dbUrl);
		 
		 dataSource.setUsername(userName);
		 
		 dataSource.setPassword(password);
		 
		 dataSource.setTestOnBorrow(testOnBorrow);
		 
		 dataSource.setValidationQuery(validationQuery);
		 
		 return dataSource;
	 }	 
	 
	 @Bean
	 public SqlSessionFactory portalSqlSessionFactory(DataSource db1DataSource, ApplicationContext applicationContext) throws Exception {

	        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

	        sqlSessionFactoryBean.setDataSource(db1DataSource);

	        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/sql/**/*.xml"));

	        return sqlSessionFactoryBean.getObject();
	    }

	
}
