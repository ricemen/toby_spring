package springbook.user.sqlservice;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {

	public void updateSql(String key, String sql) throws SqlUpdateFailureException;
	public void updateDql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
