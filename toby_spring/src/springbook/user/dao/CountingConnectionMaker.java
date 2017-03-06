package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection 카운트에 대한 관심만 있다
 * 실제 컨넥션은 구현 클래스에서 처리한다.
 * @author wonseok
 */
public class CountingConnectionMaker implements ConnectionMaker {
	/**
	 * 호출 횟수
	 */
	int counter = 0;
	/**
	 * 실제 커넥션
	 */
	private ConnectionMaker realConnectionMaker;

	/**
	 * 생성자 
	 * @param realConnectionMaker 실제 컨넥션 클래스 HCo... or MCo...
	 */
	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		super();
		this.realConnectionMaker = realConnectionMaker;
	}

	/**
	 * counter++ 이후 
	 * 실 구현 클래스의 makeConnection()을 가져온다.
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
