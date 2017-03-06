/**
 * 
 */
package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wonseok
 *
 */
public class SimpleConnectionMaker {

	public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		Connection c = DriverManager.getConnection("jdbc:h2:~/ricemen;DB_CLOSE_ON_EXIT=TRUE;FILE_LOCK=NO", "ricemen", "");
		return c;
	}
	
}
