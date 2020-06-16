package com.heeexy.example.config.dataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import java.util.List;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.filter.Filter;
 
@Configuration
public class DruidConfig {
	
	@Value("${spring.datasource.url}")
	private String dbUrl;
 
	@Value("${spring.datasource.username}")
	private String username;
 
	@Value("${spring.datasource.password}")
	private String password;
 
	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;
 
	@Value("${spring.datasource.initialSize}")
	private int initialSize;
 
	@Value("${spring.datasource.minIdle}")
	private int minIdle;
 
	@Value("${spring.datasource.maxActive}")
	private int maxActive;
 
	@Value("${spring.datasource.maxWait}")
	private int maxWait;
 
	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;
 
	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;
 
	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;
 
	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;
 
	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;
 
	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;
 
	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;
 
	@Value("${spring.datasource.filters}")
	private String filters;
	@Value("${spring.datasource.logSlowSql}")
	private String logSlowSql;
	
	@Bean
	@Primary
	public DataSource dataSource(){
		//@Primary 注解作用是当程序选择dataSource时选择被注解的这个
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(dbUrl);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		// filter
		List<Filter> filterArrayList = new ArrayList<Filter>();
		filterArrayList.add(wallFilter());
		datasource.setProxyFilters(filterArrayList);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasource;
	}
	
	  @Bean
	    public ServletRegistrationBean druidServlet() {
	        ServletRegistrationBean reg = new ServletRegistrationBean();
	        reg.setServlet(new StatViewServlet());
	        reg.addUrlMappings("/druid/*");
	        reg.addInitParameter("loginUsername", username);
	        reg.addInitParameter("loginPassword", password);
	        reg.addInitParameter("logSlowSql", logSlowSql);
	        return reg;
	    }
 
	    @Bean
	    public FilterRegistrationBean filterRegistrationBean() {
	        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	        filterRegistrationBean.setFilter(new WebStatFilter());
	        filterRegistrationBean.addUrlPatterns("/*");
	        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
	        filterRegistrationBean.addInitParameter("profileEnable", "true");
	        return filterRegistrationBean;
	    }
	@Bean
	public WallConfig wallConfig(){
		WallConfig wallConfig = new WallConfig();
		wallConfig.setMultiStatementAllow(true);//允许一次执行多条语句
		wallConfig.setNoneBaseStatementAllow(true);//允许一次执行多条语句
		return wallConfig;
	}
	@Bean
	public WallFilter wallFilter(){
		WallFilter wallFilter = new WallFilter();
		wallFilter.setConfig(wallConfig());
		return wallFilter;
	}

}
