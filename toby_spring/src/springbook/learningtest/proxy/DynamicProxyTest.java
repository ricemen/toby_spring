package springbook.learningtest.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

public class DynamicProxyTest {
	
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("wonseok"), is("Hello wonseok"));
		assertThat(hello.sayHi("wonseok"), is("Hi wonseok"));
		assertThat(hello.sayThankYou("wonseok"), is("Thank u wonseok"));
		
		/** 
		 * proxyedHello : 다이내믹 프록시
		 * Proxy.newProxyInstance : 프록시 
		 */
		Hello proxyedHello = (Hello) Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] { Hello.class },		//  구현 클래스(HelloTarget)를 만들어준다.
				new UppercaseHandler(new HelloTarget())
				);
		assertThat(proxyedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxyedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxyedHello.sayThankYou("Toby"), is("THANK U TOBY"));
	}
	
	/**
	 * 
	 * @author wonseok
	 */
	static class UppercaseHandler implements InvocationHandler {
		Object target;

		private UppercaseHandler(Object target) {
			this.target = target;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Object ret = method.invoke(target, args); // 위임
//			if(ret instanceof String) {
//				return ((String)ret).toUpperCase();
//			} else {
//				return ret;
//			}
//			return ((String)ret).toUpperCase();
			if (ret instanceof String && method.getName().startsWith("say")) {
				return ((String)ret).toUpperCase();
			}
			else {
				return ret;
			}
		}
	}
	static class HelloUppercase implements Hello {
		Hello hello;
		
		public HelloUppercase(Hello hello) {
			this.hello = hello;
		}

		public String sayHello(String name) {
			return hello.sayHello(name).toUpperCase();
		}

		public String sayHi(String name) {
			return hello.sayHi(name).toUpperCase();
		}

		public String sayThankYou(String name) {
			return hello.sayThankYou(name).toUpperCase();
		}
		
	}
	/**
	 * 타깃
	 * @author wonseok
	 *
	 */
	public class HelloTarget implements Hello {
		@Override
		public String sayHello(String name) {
			return "Hello " + name; 
		}
		@Override
		public String sayHi(String name) {
			 return "Hi " + name;
		}
		@Override
		public String sayThankYou(String name) {
			return "Thank u " + name;
		}
	}
	/**
	 * 타깃 인터페이스
	 * @author wonseok
	 *
	 */
	static interface Hello {
		String sayHello(String name);
		String sayHi(String name);
		String sayThankYou(String name);
	}

}
