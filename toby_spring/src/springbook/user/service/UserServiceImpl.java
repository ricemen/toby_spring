package springbook.user.service;


import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserServiceImpl implements UserService {
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMMENT_FOR_GOLD = 30;
	
	private UserDao userDao;
	
	private MailSender mailSender;
	
	private UserLevelUpgradePolicy userLevelUpgradePolicy;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	public void upgradeLevels() {
		
		// jdbc ����
//		TransactionSynchronizationManager.initSynchronization(); // �ʱ�ȭ
//		Connection c = DataSourceUtils.getConnection(dataSource);
//		c.setAutoCommit(false);
		
		// ������ ���� Ʈ������ �߻�ȭ ���
//		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		// �и�
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
//			// jdbc ����
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
		mailMessage.setSubject("Upgrade �ȳ�");
		mailMessage.setText("����� ����� " + user.getLevel() +" �� ���׷��̵� �Ǿ����ϴ�.");
		
		mailSender.send(mailMessage);
	}

	/**
	 * ���� ���ø� ����
	 * �׽�Ʈ�� ����
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
//			message.setSubject("Upgrade �ȳ�");
//			message.setText("����� ����� " + user.getLevel() +" �� ���׷��̵� �Ǿ����ϴ�.");
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