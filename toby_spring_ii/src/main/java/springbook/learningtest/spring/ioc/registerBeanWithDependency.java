package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

public class registerBeanWithDependency {
	
	@Test
	public void beginContainer() {
		// IoC �����̳� ����, ������ ���ÿ� �����̳ʷ� �����Ѵ�.
		StaticApplicationContext ac = new StaticApplicationContext();
		// Hello.class�� hello1 �̶�� �̸��� ������ ����Ѵ�.
		ac.registerSingleton("hello1", Hello.class);
		
		// test
		Hello hello1 = ac.getBean("hello1", Hello.class);
		assertThat(hello1, is(notNullValue()));
		
		// bean ��Ÿ������ ���� ������Ʈ�� �����.
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		ac.registerBeanDefinition("hello2", helloDef);
		
		Hello hello2 = ac.getBean("hello2", Hello.class);
		assertThat(hello2.sayHello(), is("Hello Spring"));
		
		assertThat(hello1, is(not(hello2)));
		
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
	}
}
