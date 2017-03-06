package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection ī��Ʈ�� ���� ���ɸ� �ִ�
 * ���� ���ؼ��� ���� Ŭ�������� ó���Ѵ�.
 * @author wonseok
 */
public class CountingConnectionMaker implements ConnectionMaker {
	/**
	 * ȣ�� Ƚ��
	 */
	int counter = 0;
	/**
	 * ���� Ŀ�ؼ�
	 */
	private ConnectionMaker realConnectionMaker;

	/**
	 * ������ 
	 * @param realConnectionMaker ���� ���ؼ� Ŭ���� HCo... or MCo...
	 */
	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		super();
		this.realConnectionMaker = realConnectionMaker;
	}

	/**
	 * counter++ ���� 
	 * �� ���� Ŭ������ makeConnection()�� �����´�.
	 */
	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		this.counter++;
		return realConnectionMaker.makeConnection();		
	}
	
	public int getCounter() {
		return counter;
	}

}
