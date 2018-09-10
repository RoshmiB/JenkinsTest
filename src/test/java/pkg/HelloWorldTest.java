package pkg;

import org.junit.Test;
import static org.junit.Assert.*;


import pkg1.HelloWorld;

public class HelloWorldTest {
	HelloWorld hw = new HelloWorld();

	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAdd2() {
		assertEquals(7,hw.add(2, 5));
	}
}
