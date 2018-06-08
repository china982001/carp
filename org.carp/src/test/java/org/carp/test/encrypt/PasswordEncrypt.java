package org.carp.test.encrypt;

import org.carp.CarpSessionBuilder;
import org.carp.cfg.CarpConfig;
import org.carp.security.PasswordDecryptor;
import org.junit.Assert;

public class PasswordEncrypt {
	public static void test()throws Exception{
		testPaswordJJM();
		testCustPaswordJJM();
	}
	
	public static void testPaswordJJM()throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Password  Encryptor Decryptor Case ....");
		PasswordDecryptor decryptor = new PasswordDecryptor();
		String password = "my test password";
		String mwpassword = decryptor.encrypt(password);
		System.out.println("\r\n加密后的密文为：  "+mwpassword);
		String newPassword = decryptor.decrypt(mwpassword);
		Assert.assertEquals("record count:", password,newPassword);
		System.out.println("End Password  Encryptor Decryptor Case.  SUCCESS!");
	}
	public static void testCustPaswordJJM()throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin CustPassword Decryptor Case ....");
		CarpConfig config =new CarpConfig();
		config.setDecryptor(new CustDecryptor());
		CarpSessionBuilder builder = config.getSessionBuilder();
		builder.getSession().close();;
		System.out.println("End CustPassword  Decryptor Case.  SUCCESS!");
	}
}
