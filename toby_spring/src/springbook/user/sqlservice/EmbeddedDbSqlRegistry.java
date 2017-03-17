package springbook.user.sqlservice;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
	
	SimpleJdbcTemplate jdbc;
	TransactionTemplate transactionTemplate;

	/**
	 * EmbeddedDatabase 는 DataSource 인터페이스를 상속하고 있다
	 * EmbeddedDatabase 로 DI 받지 않은 이유는 인터페이스 분리 원칙을 치키기 위해서이다.
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbc = new SimpleJdbcTemplate(dataSource);
		transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
	}

	@Override
	public void registerSql(String key, String sql) {
		jdbc.update("insert into sqlmap(key_, sql_) values(?,?)", key, sql); 
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		
		try {
			return jdbc.queryForObject("select sql_ from sqlmap where key_ = ? ", String.class, key);
		} catch(EmptyResultDataAccessException e) {
			throw new SqlNotFoundException(key + "를 찾을수 없습니다.");
		}
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		int affected = jdbc.update("update sqlmap set sql_= ? where key_ = ?", sql, key);
		if(affected == 0) throw new SqlUpdateFailureException(key + "를 찾을수 없습니다.");
		
	}

	@Override
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				for(Map.Entry<String, String> entry : sqlmap.entrySet()) {
					updateSql(entry.getKey(), entry.getValue());
				}
			}
		});
	}
}
