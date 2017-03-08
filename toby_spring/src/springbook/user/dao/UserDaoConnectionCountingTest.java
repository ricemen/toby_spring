package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDaoConnectionCountingTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDaoJdbc dao = context.getBean("userDao", UserDaoJdbc.class);
		
		User user = new User();
		user.setId("ricemen8");
		user.setName("조원석8");
		user.setPassword("cho6");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getId() + " 조회 성공");
		System.out.println(user2.toString());
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		
		System.out.println("Connection Counter : " + ccm.getCounter());

	}
}
