package org.carp.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JunitTest {
	
	@Before
	public void init(){
		System.out.println(" init ");
	}
	
	@Test
	public void run1(){
		System.out.println(" run 1 ");
		
	}
	@Test
	public void run2(){
		System.out.println(" run 2 ");
		
	}
	
	@After
	public void close(){
		System.out.println(" close ");
		
	}
}
