package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class CountingDaoFactory {

	@Bean 
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("");
		dataSource.setUrl("ricemen");
		dataSource.setPassword("-1010232");
		return dataSource;		
	}
	
	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDao = new UserDaoJdbc();
		userDao.setDataSource(dataSource());
		return userDao;		
//		return new UserDao(connectionMaker());
	}
	
	/**
	 * ���� ���� Ŭ������ �ٷ� DI �ϴ°� �ƴ϶� CountingConnectionMaker�� �ѱ��
	 * @return
	 */
	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new MConnectionMaker();
	}
}
