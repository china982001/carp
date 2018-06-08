package org.carp.test.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.carp.cfg.CarpConfig;
import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
import org.junit.Assert;

public class ConfigTest {
	public static void test()throws Exception{
		testBasedXml();
		testBasedInputStream();
		testBasedProperties();
	}
	public static void testBasedXml() throws CarpException{
		System.out.println();
		System.out.println();
		System.out.println("BEGIN   config test case ...");
		CarpConfig config = new CarpConfig(new File("src/test/conf/carp_conf.xml"));
		CarpSetting s = config.getSetting();
		Assert.assertEquals("show Sql:", true,s.isShowSql());
		Assert.assertEquals("DrierClass:", "com.mysql.jdbc.Driver",s.getDriverClass());
		System.out.println(s.getExtProperty());
		System.out.println("END   config test case ...  SUCCESS!");
	}
	
	public static void testBasedInputStream() throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("BEGIN   InputStream config test case ...");
		CarpConfig config = new CarpConfig(new FileInputStream(new File("src/test/conf/carp.inputstream.xml")));
		CarpSetting s = config.getSetting();
		Assert.assertEquals("driver class:", "oracle.jdbc.driver.OracleDriver",s.getDriverClass());
		Assert.assertEquals("driver   url:", "jdbc:oracle:thin:@localhost:1521:orcl",s.getUrl());
		Assert.assertEquals("user name:", "root",s.getUserName());
		Assert.assertEquals("password:", "654321",s.getPassword());
		Assert.assertEquals("show Sql:", true,s.isShowSql());
		System.out.println("END  InputStream config test case ...  SUCCESS!");
	}
	public static void testBasedProperties() throws CarpException{
		System.out.println();
		System.out.println();
		System.out.println("BEGIN   properties config test case ...");
		Properties p = new Properties();
		p.put("carp.driver", "p_driver");
		p.put("carp.url", "p_url");
		p.put("carp.user", "user");
		p.put("carp.password", "pwd");
		p.put("show.sql", "true");
		CarpConfig config = new CarpConfig(p);
		CarpSetting s = config.getSetting();
		Assert.assertEquals("driver class:", "p_driver",s.getDriverClass());
		Assert.assertEquals("driver   url:", "p_url",s.getUrl());
		Assert.assertEquals("user name:", "user",s.getUserName());
		Assert.assertEquals("password:", "pwd",s.getPassword());
		Assert.assertEquals("show Sql:", true,s.isShowSql());
		System.out.println("END  properties config test case ...  SUCCESS!");
	}
}
