package springbook.user.sqlservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractConcurrentHashMapSqlRegistryTest {
	
	UpdatableSqlRegistry sqlRegistry;

	@Before
	public void setUp() {
//		sqlRegistry = new ConCurrentHashMapSqlRegistry();
		sqlRegistry = createUpdatatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}
	
	abstract protected UpdatableSqlRegistry createUpdatatableSqlRegistry();
	
	@Test
	public void find() {
		checkFindResult("SQL1", "SQL2", "SQL3");
	}
	
	protected void checkFindResult(String exp1, String exp2, String exp3) {
		assertThat(sqlRegistry.findSql("KEY1"), is(exp1));
		assertThat(sqlRegistry.findSql("KEY2"), is(exp2));
		assertThat(sqlRegistry.findSql("KEY3"), is(exp3));
	}
	
	@Test(expected=SqlNotFoundException.class)
	public void unknownKey() {
		sqlRegistry.findSql("SQL901902");
	}
	
	@Test
	public void updateSingle() {
		sqlRegistry.updateSql("KEY2", "Modifued2");
		checkFindResult("SQL1", "Modifued2", "SQL3");
	}
	
	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<>();
		sqlmap.put("KEY1", "Modifued1");
		sqlmap.put("KEY3", "Modifued3");
		
		sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modifued1", "SQL2", "Modifued3");
	}
	
	@Test(expected=SqlUpdateFailureException.class)
	public void updateWithNotExistingKey() {
		sqlRegistry.updateSql("SQL123123", "Modifued2");
	}
}
