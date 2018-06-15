package org.carp.test.blob;

import java.nio.CharBuffer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.util.List;

import org.carp.Carp;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.test.pojo.CarpBlob;
import org.carp.transaction.Transaction;
import org.junit.Assert;

public class TestBlobClob {
	public static void main(String[] strs)throws Exception{
		CarpSessionBuilder builder = CarpSessionBuilder.getSessionBuilder();
		CarpSession s = builder.getSession();
		Connection conn = s.getConnection();
		Blob blob = conn.createBlob();
		System.out.println(blob);
		s.close();
	}
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testInsertBlob(builder);
		testUpdateBlob(builder);
	}
	
	public static void testInsertBlob(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin BLOB Insert case...");
		CarpSession s=builder.getSession();	
		Transaction tx = s.beginTransaction();
		CarpBlob c = new CarpBlob();
		c.setCatId(1);
//		c.setCatImage(Carp.createBlob("Blob大对象类型测试".getBytes()));
//		c.setCatSpec(Carp.createClob("CLob数据类型测试"));
		c.setCatImage("Blob大对象类型测试".getBytes());
		c.setCatSpec("CLob数据类型测试");
		s.save(c);
		tx.commit();
		s.close();
		s=builder.getSession();
		List<?> list = s.creatQuery(CarpBlob.class).list();
		Assert.assertEquals("cat count +1  ", 1,list.size());
		CarpBlob carp = (CarpBlob)list.get(0);
//		Blob blob = carp.getCatImage();
//		byte[] b = blob.getBytes(1, (int)blob.length());
		Assert.assertEquals("BLOB 类型测试: ", "Blob大对象类型测试",new String(carp.getCatImage()));
//		Clob clob = carp.getCatSpec();
//		System.out.println("clob _len = "+clob.length());
//		String str = clob.getSubString(1, (int)clob.length());
		Assert.assertEquals("CLOB 类型测试: ", "CLob数据类型测试",carp.getCatSpec());
		s.close();
		System.out.println("End BLOB Insert case. SUCCESS!");
	}
	public static void testUpdateBlob(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin BLOB UPDATE  case...");
		CarpSession s=builder.getSession();	
		Transaction tx = s.beginTransaction();
		CarpBlob c = new CarpBlob();
		c.setCatId(1);
//		c.setCatImage(Carp.createBlob("Blob大对象类型测试".getBytes()));
//		c.setCatSpec(Carp.createClob("CLob数据类型测试"));
		c.setCatImage("Blob大对象类型测试-Update".getBytes());
		c.setCatSpec("CLob数据类型测试-Update");
		s.update(c);
		tx.commit();
		s.close();
		s=builder.getSession();
		List<?> list = s.creatQuery(CarpBlob.class).list();
		Assert.assertEquals("cat count +1  ", 1,list.size());
		CarpBlob carp = (CarpBlob)list.get(0);
//		Blob blob = carp.getCatImage();
//		byte[] b = blob.getBytes(1, (int)blob.length());
		Assert.assertEquals("BLOB 类型测试: ", "Blob大对象类型测试-Update",new String(carp.getCatImage()));
//		Clob clob = carp.getCatSpec();
//		System.out.println("clob _len = "+clob.length());
//		String str = clob.getSubString(1, (int)clob.length());
		Assert.assertEquals("CLOB 类型测试: ", "CLob数据类型测试-Update",carp.getCatSpec());
		s.close();
		System.out.println("End BLOB UPDATE case. SUCCESS!");
	}
}
