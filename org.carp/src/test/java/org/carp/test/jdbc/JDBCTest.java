package org.carp.test.jdbc;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;

public class JDBCTest {

	public static void testSession(CarpSessionBuilder  builder) throws Exception {
		System.out.println();
		System.out.println();
		System.out.println("Begin OPEN Session ....");
		CarpSession s = builder.getSession();
		CarpSession s1 = builder.getSession();
		org.junit.Assert.assertNotEquals("not same session", s,s1);
		org.junit.Assert.assertNotEquals("not same Ccnnection", s.getConnection(),s1.getConnection());
		org.junit.Assert.assertEquals("s.getConnection same Ccnnection", s.getConnection(),s.getConnection());
		org.junit.Assert.assertEquals("s1.getConnection same Ccnnection", s1.getConnection(),s1.getConnection());
		s.close();
		s1.close();
		System.out.println("End OPEN Session .... SUCCESS!");
	}

}
