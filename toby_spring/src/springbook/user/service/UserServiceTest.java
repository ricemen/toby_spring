package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMMENT_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;
	
	@Autowired
	UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("bumjin" , "박범진", "p1@gmail.com", "p1" , Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0) ,
			new User("joytouch" , "강명성", "p2@nate.com", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0) ,
			new User( "erwins", "신승한", "p3@naver.com", "p3", Level.SILVER, 60, MIN_RECCOMMENT_FOR_GOLD-1),
			new User("madnite1" , "이상호", "p4@hanmail.com", "p4" , Level.SILVER, 60 , MIN_RECCOMMENT_FOR_GOLD) ,
			new User( "green", "오민규", "p5@postvisual.com", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
		);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
		
		checkLevelUpgrade(users.get(0), false);
		checkLevelUpgrade(users.get(1), true);
		checkLevelUpgrade(users.get(2), false);
		checkLevelUpgrade(users.get(3), true);
		checkLevelUpgrade(users.get(4), false);
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}
	
	@Test
	public void upgradeAllOrNoting() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
		testUserService.setTransactionManager(transactionManager);
		
		userDao.deleteAll();
		
		for(User user : users) { 
			userDao.add(user); 
		}
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {			
		}
		
		checkLevelUpgrade(users.get(1), false);
		
	}
	
	
	static class TestUserService extends UserService {
		
		private String id;

		private TestUserService(String id) {
			this.id = id;
		}
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6951044591013051063L;
		
	}
	
	private void checkLevelUpgrade(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	private void checkLevel(User user, Level exectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(exectedLevel));
	}
}
