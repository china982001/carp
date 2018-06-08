package org.carp.test.delete;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.cfg.CarpConfig;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.carp.transaction.Transaction;
import org.junit.Assert;

public class DeleteCase {
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testDeleteByObject(builder);
		testDeleteById(builder);
		testDeleteByIdUnCommit(builder);
	}
	
	
	public static void testDeleteByObject(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Delete ObjectByObject ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		CarpCat c = new CarpCat();
		c.setCatId(10);
		s.delete(c);
		tx.commit();
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count-1, afterCount);
		System.out.println("Begin Delete ObjectByObject .  SUCCESS!");
	}
	public static void testDeleteById(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Delete ObjectById ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		s.delete(CarpCat.class, 11);
		tx.commit();
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count-1, afterCount);
		System.out.println("Begin Delete ObjectById .  SUCCESS!");
	}
	
	public static void testDeleteByIdUnCommit(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Delete ObjectById UnCommit Case....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		s.delete(CarpCat.class, 12);
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count, afterCount);
		System.out.println("Begin Delete ObjectById UnCommit Case....  SUCCESS!");
	}

}
