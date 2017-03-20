package springbook.user.anno;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceTest.TestUserService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="springbook.user")
//@Import({SqlServiceContext.class, AppContext.TestAppContext.class, AppContext.ProductionAppContext.class})
@Import(SqlServiceContext.class)
@PropertySource("/database.properties")
public class AppContext {
	
//	@Autowired
//	Environment env;
	
	@Value("${db.driverClass}") Class<? extends java.sql.Driver> driverClass;
	@Value("${db.url}") String url;
	@Value("${db.username}") String username;
	@Value("${db.password}") String password;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placheholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * DataSource
	 */
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
//		ds.setDriverClass(Driver.class);
//		ds.setUrl("jdbc:mysql://localhost/study");
//		ds.setUsername("ricemen");
//		ds.setPassword("-1010232");
		/*try {
			ds.setDriverClass((Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass"))) ;
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		ds.setUrl(env.getProperty("db.url"));
		ds.setUsername(env.getProperty("db.username"));
		ds.setPassword(env.getProperty("db.password"));*/
		
		ds.setDriverClass(driverClass);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		
		return ds;
	}
	
	@Bean
	public DataSource h2DataSource() {
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.h2.Driver.class);
		ds.setUrl("jdbc:h2:~/spring");
		ds.setUsername("spring");
		ds.setPassword("book");
		return ds;		
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager dtm = new DataSourceTransactionManager();
		dtm.setDataSource(dataSource());
		return dtm;
	}
	
	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		@Bean 
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("localhost");
			return mailSender;
		}
	}
	
	@Configuration
	@Profile("test")
	public static class TestAppContext {
		
		@Bean
		public UserService testUserService() {
			return new TestUserService();
			
		}
		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
	}
}
