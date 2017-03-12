package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMMENT_FOR_GOLD;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
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
	ApplicationContext context;
	
	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	MailSender mailSender;
	
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
	public void mockUpgradeLevels() {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessage = mailMessageArg.getAllValues();
		assertThat(mailMessage.get(0).getTo()[0] , is(users.get(1).getEmail()));
		assertThat(mailMessage.get(1).getTo()[0] , is(users.get(3).getEmail()));
		
		
	}
	
	@Test
	public void upgradeLevels() throws Exception {
//		userDao.deleteAll();
//		for(User user : users) userDao.add(user);
		
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
		
		
//		checkLevelUpgrade(users.get(0), false);
//		checkLevelUpgrade(users.get(1), true);
//		checkLevelUpgrade(users.get(2), false);
//		checkLevelUpgrade(users.get(3), true);
//		checkLevelUpgrade(users.get(4), false);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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
	@DirtiesContext
	public void upgradeAllOrNoting() throws Exception {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(mailSender);
/*		
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(testUserService);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern("upgradeLevels");
		
		UserService txUserService = (UserService)Proxy.newProxyInstance (
				getClass().getClassLoader(),
				new Class[] { UserService.class }, 
				txHandler);*/
		
//		UserServiceTx txUserService = new UserServiceTx();
//		txUserService.setTransactionManager(transactionManager);
//		txUserService.setUserService(testUserService);
		
		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();
		
		userDao.deleteAll();
		
		for(User user : users) { 
			userDao.add(user); 
		}
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {			
		}
		
		checkLevelUpgrade(users.get(1), false);
		
	}
	
	
	static class TestUserService extends UserServiceImpl {
		
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
	
	static class MockMailSender implements MailSender {
		
		private List<String> requests = new ArrayList<>();

		public List<String> getRequests() {
			return requests;
		}

		public void setRequests(List<String> requests) {
			this.requests = requests;
		}

		@Override
		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		@Override
		public void send(SimpleMailMessage[] arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	static class MockUserDao implements UserDao {
		
		private List<User> users;
		private List<User> updated = new ArrayList<>();
		
		public MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated() {
			return updated;
		}
		
		@Override
		public List<User> getAll() {
			return users;
		}

		@Override
		public void update(User user) {
			updated.add(user);
		}

		@Override
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			throw new UnsupportedOperationException();
		}



		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}
	}
}
