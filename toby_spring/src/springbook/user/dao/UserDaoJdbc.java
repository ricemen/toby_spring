/**
 * 
 */
package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

/**
 * @author wonseok
 *
 */
public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	
	private Map<String, String> sqlMap;
	
	private RowMapper<User> userMapper = new RowMapper<User>() {

		@Override 
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueof(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		}
		
	};
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public void add(final User user) {
/*		this.jdbcTemplate.update("insert into users(id, name, email, password, level, login, recommend) values(?, ?, ?, ?, ?, ?, ?)"
				, user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());*/
		this.jdbcTemplate.update(this.sqlMap.get("add"), user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());		
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject(this.sqlMap.get("get"), new Object[] {id}, userMapper);
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query(this.sqlMap.get("getAll"), userMapper);
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));
	}
	
	public int getCount() {
		return this.jdbcTemplate.queryForInt(this.sqlMap.get("getCount"));
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlMap.get("update") , user.getName(), user.getEmail(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
	}
}
