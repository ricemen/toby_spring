package springbook.user.anno;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.EmbeddedDbSqlRegistry;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

@Configuration
public class SqlServiceContext {
	/**
	 * sql
	 */
//	@Resource EmbeddedDatabase embeddedDatabase;
	
	@Autowired
	SqlMapConfig sqlMapConfig;
	
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
//		sqlService.setSqlmap(new ClassPathResource("sqlmap.xml", UserDao.class));
		sqlService.setSqlmap(sqlMapConfig.getSqlMapResource());
		sqlService.setSqlRegistry(sqlRegistry());
		return sqlService;
	}
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry emdb = new EmbeddedDbSqlRegistry();
		emdb.setDataSource(embeddedDatabase());
		return emdb;
	}
	
	@Bean
	public Jaxb2Marshaller unmarshaller() {
		Jaxb2Marshaller ms = new Jaxb2Marshaller();
		ms.setContextPath("springbook.user.sqlservice.jaxb");
		return ms;
	}
	
	@Bean 
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
			.setName("embeddedDatabase")
			.setType(EmbeddedDatabaseType.HSQL)
			.addScript("classpath:springbook/user/sqlservice/sqlRegistrySchema.sql")
			.build();
	}
}
