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
	 * 실제 구현 클래스를 바로 DI 하는게 아니라 CountingConnectionMaker로 넘긴다
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
