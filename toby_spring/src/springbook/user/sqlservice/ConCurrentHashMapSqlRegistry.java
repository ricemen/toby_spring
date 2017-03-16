package springbook.user.sqlservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConCurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
	
	private Map<String, String> sqlMap = new ConcurrentHashMap<>();

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null) throw new SqlNotFoundException(key + "�� �̿��� SQL�� ã���� �����ϴ�.");
		else return sql;
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		if(sqlMap.get(key) == null) throw new SqlUpdateFailureException(key + "�� �ش��ϴ� SQL�� ����.");
		else sqlMap.put(key, sql);
	}

	@Override
	public void updateDql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		for(Map.Entry<String, String> entry : sqlmap.entrySet()) {
			updateSql(entry.getKey(), entry.getValue());
		}
	}

}
