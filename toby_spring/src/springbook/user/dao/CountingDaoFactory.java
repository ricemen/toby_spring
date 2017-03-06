package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
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
