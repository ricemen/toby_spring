/**
 * 
 */
package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.sun.glass.ui.Application;

import springbook.user.domain.User;

/**
 * @author wonseok
 *
 */
public class UserDaoTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		
//		DaoFactory factory = new DaoFactory();
//		UserDao user1 = factory.userDao();
//		UserDao user2 = factory.userDao();
//		
//		System.out.println(user1);
//		System.out.println(user2);
		
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
//		UserDao user3 = context.getBean("userDao", UserDao.class);	
//		UserDao user4 = context.getBean("userDao", UserDao.class);	
//		
//		System.out.println(user3);
//		System.out.println(user4);
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		
		User user = new User();
		user.setId("ricemen9");
		user.setName("조원석9");
		user.setPassword("cho9");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getId() + " 조회 성공");
		System.out.println(user2.toString());		
		
	}
	
//	public static void main2(String[] args) throws ClassNotFoundException, SQLException {
//		
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//		UserDao dao = context.getBean("userDao", UserDao.class);
//		
////		UserDao dao = new DaoFactory().userDao();
//		
//		User user = new User();
//		user.setId("ricemen7");
//		user.setName("조원석7");
//		user.setPassword("cho6");
//		
//		dao.add(user);
//		
//		System.out.println(user.getId() + " 등록 성공");
//		
//		User user2 = dao.get(user.getId());
//		System.out.println(user2.getId() + " 조회 성공");
//		System.out.println(user2.toString());
//	}
}
