package springbook.learningtest.jdk;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

public class ReplectionTest {

	@Test
	public void invokeMethod() throws Exception {
		
		String name = "wonseok";
		
		assertThat(name.length(), is(7));
		
		Method lengthMethod = String.class.getMethod("length");
		assertThat((Integer)lengthMethod.invoke(name), is(7));
		
		assertThat(name.charAt(0), is('w'));
		
		Method charAtMethod = String.class.getMethod("charAt", int.class);
		assertThat((Character)charAtMethod.invoke(name, 0), is('w'));
	}
}
