package org.carp.test.update;


import java.util.Date;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.carp.transaction.Transaction;
import org.junit.Assert;


public class CarpUpdateCase {
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testUpdateObject(builder);
		testUpdateUnCommit(builder);
		testUpdateUnTransaction(builder);
	}
	
	public static void testUpdateObject(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Update Object ....");
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		CarpCat c = new CarpCat();
		c.setCatId(1);
		c.setCatAge((short) 1);
		c.setCatName("update更新-"+1);
		c.setStsTime(new Date());
		s.update(c);
		tx.commit();
		s.close();
		s=builder.getSession();
		CarpCat o = (CarpCat)s.get(CarpCat.class, 1);
		Assert.assertEquals("更新成功", o.getCatName(), "update更新-1");
		System.out.println("End Update Object.  SUCCESS!");
	}
	
	public static void testUpdateUnCommit(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Update Object UnCommit ....");
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		CarpCat c = new CarpCat();
		c.setCatId(2);
		c.setCatAge((short) 2);
		c.setCatName("update更新-"+2);
		c.setStsTime(new Date());
		s.update(c);
		s.close();
		s=builder.getSession();
		CarpCat o = (CarpCat)s.get(CarpCat.class, 1);
		Assert.assertNotEquals("更新失败", o.getCatName(), "update更新-2");
		s.close();
		System.out.println("End Update Object UnCommit.  SUCCESS!");
	}
	
	public static void testUpdateUnTransaction(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Update Object UnTransaction ....");
		CarpSession s=builder.getSession();
		CarpCat c = new CarpCat();
		c.setCatId(3);
		c.setCatAge((short) 3);
		c.setCatName("update更新-"+3);
		c.setStsTime(new Date());
		s.update(c);
		s.close();
		s=builder.getSession();
		CarpCat o = (CarpCat)s.get(CarpCat.class, 3);
		Assert.assertEquals("更新成功", o.getCatName(), "update更新-3");
		s.close();
		System.out.println("End Update Object UnTransaction.  SUCCESS!");
	}
	
	
}

