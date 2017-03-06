/**
 * 
 */
package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author wonseok
 *
 */
public interface ConnectionMaker {
	public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
