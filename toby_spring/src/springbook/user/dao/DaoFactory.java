/**
 * 
 */
package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * @author wonseok
 *
 */
@Configuration
public class DaoFactory {
	
//	private ConnectionMaker connectionMaker;
	
	@Bean 
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/study");
		dataSource.setUsername("ricemen");
		dataSource.setPassword("-1010232");
		
		return dataSource;		
	}
	
	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDao = new UserDaoJdbc();
		userDao.setDataSource(dataSource());
		return userDao;
	}
}
