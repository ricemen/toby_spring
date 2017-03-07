package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	
	Calculator calculator;
	String numFilepath;
	
	@Before
	public void setUp() {
		calculator = new Calculator();
		numFilepath = getClass().getResource("number.txt").getPath();
	}

	@Test
	public void sumOfNumbers() throws IOException {
		int sum = calculator.calcSum(numFilepath);
		System.out.println("sum : " + sum);
		assertThat(sum, is(15));
	}
	
	@Test 
	public void multiplyOfNumbers() throws IOException {
		int sum = calculator.calcMultiply(numFilepath);
		System.out.println("sum : " + sum);
		assertThat(sum, is(120));
	}
}
