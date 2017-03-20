/**
 * 
 */
package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.anno.AppContext;
import springbook.user.domain.Level;
import springbook.user.domain.User;

/**
 * @author wonseok
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/test-applicationContext.xml")
@ActiveProfiles("production") 
@ContextConfiguration(classes=AppContext.class)
//@DirtiesContext
public class UserDaoTest {
	
// 	@ContextConfiguration(locations="/applicationContext.xml") 에 의해 ApplicationContext 도 빈으로 자동 등록된다
//	@Autowired
//	private ApplicationContext context;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		this.user1 = new User("wonseok", "조원석", "ricemen@gmail.com", "cho", Level.BASIC,1, 0);
		this.user2 = new User("leejieun", "이지은", "lje@nate.com", "lee", Level.SILVER, 55, 10);
		this.user3 = new User("younseo", "조윤서", "choyounseo@gmail.com", "cho", Level.GOLD, 100, 40);
	}
	
	@Autowired
	DefaultListableBeanFactory bf;
	
	@Test
	public void beans() {
		for(String n : bf.getBeanDefinitionNames()) {
			System.out.println(n + " \t" + bf.getBean(n).getClass().getName());
		}
	}
	
	@Test
	public void addAndGet() throws SQLException {
		
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));
		
		userDao.add(user1);
		userDao.add(user2);
		userDao.add(user3);
		assertThat(userDao.getCount(), is(3));
		
		User userget1 = userDao.get(user1.getId());
		checkSameUser(userget1, user1);
		
		User userget2 = userDao.get(user2.getId());
		checkSameUser(userget2, user2);
		
		User userget3 = userDao.get(user3.getId());
		checkSameUser(userget3, user3);
	}
	
	@Test
	public void count() throws SQLException {
		
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));
		
		userDao.add(user1);
		assertThat(userDao.getCount(), is(1));
		
		userDao.add(user2);
		assertThat(userDao.getCount(), is(2));
		
		userDao.add(user3);
		assertThat(userDao.getCount(), is(3));
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		
		userDao.deleteAll();
		
		assertThat(userDao.getCount(), is(0));
		
		userDao.get("unknow_id");
	}
	
	@Test
	public void getAll() throws SQLException {
		// 1, 0, 2
		userDao.deleteAll();
		
		List<User> users0 = userDao.getAll();
		assertThat(users0.size(), is(0));
		
		userDao.add(user1);
		List<User> users1 = userDao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));

		userDao.add(user2);
		List<User> users2 = userDao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user2, users2.get(0));
		checkSameUser(user1, users2.get(1));

		userDao.add(user3);
		List<User> users3 = userDao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user2, users3.get(0));
		checkSameUser(user1, users3.get(1));		
		checkSameUser(user3, users3.get(2));		
	}
	
	@Test
	public void update() {
		userDao.deleteAll();
		userDao.add(user1);
		userDao.add(user2);
		user1.setName("조원철");
		user1.setPassword("cwc");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		userDao.update(user1);
		
		User userupdate = userDao.get(user1.getId());
		checkSameUser(userupdate, user1);
		User userget2 = userDao.get(user2.getId());
		checkSameUser(userget2, user2);
	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey() {
		userDao.deleteAll();
		
		userDao.add(user1);
		userDao.add(user1);
	}
	
//	@Test
//	public void sqlExceptionTranslate() {
//		dao.deleteAll();
//		
//		try {
//			dao.add(user1);
//			dao.add(user1);
//		} catch(DuplicateKeyException ex) {
//			SQLException sqlEx = (SQLException) ex.getRootCause();
//			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(dataSource);
//			assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
//		}
//	}
	
	
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getEmail(), is(user2.getEmail()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
}
