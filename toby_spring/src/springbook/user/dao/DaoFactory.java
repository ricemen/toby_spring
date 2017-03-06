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
		userDao.setConnectionMaker(connectionMaker());  // ���� ������Ʈ
		return userDao;
//		return new UserDao(connectionMaker());
	}
	@Bean
	public ConnectionMaker connectionMaker() {
		return new MConnectionMaker();
	}
}
