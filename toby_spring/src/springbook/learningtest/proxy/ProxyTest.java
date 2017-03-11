package springbook.learningtest.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProxyTest {

	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("wonseok"), is("Hello wonseok"));
		assertThat(hello.sayHi("wonseok"), is("Hi wonseok"));
		assertThat(hello.sayThankYou("wonseok"), is("Thank u wonseok"));
	}
	
	@Test
	public void proxyTest() {
		Hello hello = new HelloUppercase(new HelloTarget());
		assertThat(hello.sayHello("wonseok"), is("HELLO WONSEOK"));
		assertThat(hello.sayHi("wonseok"), is("HI WONSEOK"));
		assertThat(hello.sayThankYou("wonseok"), is("THANK U WONSEOK"));
	}
}
