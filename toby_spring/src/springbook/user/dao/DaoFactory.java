/**
 * 
 */
package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wonseok
 *
 */
@Configuration
public class DaoFactory {
	
//	private ConnectionMaker connectionMaker;
	
	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());  // 의존 오브젝트
		return userDao;
//		return new UserDao(connectionMaker());
	}
	@Bean
	public ConnectionMaker connectionMaker() {
		return new MConnectionMaker();
	}
}
