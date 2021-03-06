package springbook.user.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@Component("userService")
public class UserServiceImpl implements UserService {
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMMENT_FOR_GOLD = 30;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	public void upgradeLevels() {
		
		// jdbc 종속
//		TransactionSynchronizationManager.initSynchronization(); // 초기화
//		Connection c = DataSourceUtils.getConnection(dataSource);
//		c.setAutoCommit(false);
		
		// 스프링 제공 트랜젝션 추상화 기술
//		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		// 분리
//		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		List<User> users = userDao.getAll();
		
		for(User user : users) {
			if(canUpgradeLevel(user)) 
				upgradeLevel(user);
		}
		
//		try {
//			
//			/**
//			List<User> users = userDao.getAll();
//			
//			for(User user : users) {
//				if(canUpgradeLevel(user)) 
//					upgradeLevel(user);
//			}
//			*/
//			upgradeLevelInternal();
//			transactionManager.commit(status);
//		} catch(Exception e) {
//			transactionManager.rollback(status);
//			throw e;
//		} finally {
//			// jdbc 종속
////			DataSourceUtils.releaseConnection(c, dataSource);
////			TransactionSynchronizationManager.unbindResource(this.dataSource);
////			TransactionSynchronizationManager.clearSynchronization();
//		}
	}
	
//	private void upgradeLevelInternal() {
//		List<User> users = userDao.getAll();
//		
//		for(User user : users) {
//			if(canUpgradeLevel(user)) 
//				upgradeLevel(user);
//		}
//	}
	
	public boolean canUpgradeLevel(User user) {

		Level currentLevel = user.getLevel();
		switch (currentLevel) {
			case BASIC : return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER : return (user.getRecommend() >= MIN_RECCOMMENT_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknow Level: " + currentLevel);
		}
	}
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("admin@server.com");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자 등급이 " + user.getLevel() +" 로 업그레이드 되었습니다.");
		
		mailSender.send(mailMessage);
	}

	public void deleteAll() { 	userDao.deleteAll(); }
	public User get(String id) { return userDao.get(id); }
	public List<User> getAll() { return userDao.getAll(); }
	public void update(User user) { userDao.update(user); }

	/**
	 * 실제 사용시만 가능
	 * 테스트는 힘듬
	 */
//	private void sendUpgradeEMail(User user) {
//		Properties props = new Properties();
//		props.put("mail.smtp.host", "mail.gmail.com");
//		
//		Session s = Session.getInstance(props, null);
//		
//		MimeMessage message = new MimeMessage(s);
//		
//		try {
//			message.setFrom(new InternetAddress("wonseokcho@postvisual.com"));
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//			message.setSubject("Upgrade 안내");
//			message.setText("사용자 등급이 " + user.getLevel() +" 로 업그레이드 되었습니다.");
//			
//			Transport.send(message);
//		} catch (AddressException e) {
//			throw new RuntimeException(e);
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
////		} catch (UnsupportedEncodingException e) {
////			throw new RuntimeException(e);
//		}
//		
//	}	
}