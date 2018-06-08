package org.carp.test.session;

public class SessionTest {

	public static void main(String[] args) {
		SessionUtil.getSession();
		org.junit.Assert.assertEquals("the same session", SessionUtil.getSession(),SessionUtil.getSession());
		org.junit.Assert.assertNotEquals("different session", SessionUtil.getSession(),SessionUtil.getAnotherSession());
		SessionUtil.close();
		
		
	}

}
