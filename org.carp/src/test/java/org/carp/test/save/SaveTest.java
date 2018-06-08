package org.carp.test.save;


import java.util.Date;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.cfg.CarpConfig;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.carp.transaction.Transaction;
import org.junit.Assert;



public class SaveTest {
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testSaveObject(builder);
		testSaveObjectUnCommit(builder);
		testSaveObjectUnTransaction(builder);
	}
	
	
	public static void testSaveObject(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Object Save Transaction  case...");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();		
		Transaction tx = s.beginTransaction();
		CarpCat c = new CarpCat();
		c.setCatId(10);
		c.setCatName("名称--"+10);
		c.setCatAge((short)10);
		c.setCatWeight(10);
		c.setStsTime(new Date());
		s.save(c);
		tx.commit();
		CarpCat o = (CarpCat)s.get(CarpCat.class, 10);
		Assert.assertNotNull(o);
		Assert.assertEquals("cat age： ", o.getCatAge(), new Short((short)10));
		s.close();
		int aftercount = BaseCarp.count(builder);
		Assert.assertEquals("after insert object count: ", count+1, aftercount);
		
		System.out.println("End Object Save Transaction case. SUCCESS!");
	}
	
	public static void testSaveObjectUnCommit(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Object Save UnCommit  case...");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();		
		Transaction tx = s.beginTransaction();
		CarpCat c = new CarpCat();
		c.setCatId(1001);
		c.setCatName("名称--"+1001);
		c.setCatAge((short)1001);
		c.setCatWeight(1001);
		c.setStsTime(new Date());
		s.save(c);
		s.close();
		s=builder.getSession();	
		CarpCat o = null;
		try{
			o = (CarpCat)s.get(CarpCat.class, 1001);
		}catch(Exception e){}
		Assert.assertNull(o);
		s.close();
		int aftercount = BaseCarp.count(builder);
		Assert.assertEquals("after insert object count: ", count, aftercount);
		System.out.println("End Object Save UnCommit case. SUCCESS!");
	}
	
	public static void testSaveObjectUnTransaction(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Object Save UnTransaction  case...");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();		
		CarpCat c = new CarpCat();
		c.setCatId(33);
		c.setCatName("名称--"+33);
		c.setCatAge((short)33);
		c.setCatWeight(33);
		c.setStsTime(new Date());
		s.save(c);
		s.close();
		s=builder.getSession();		
		CarpCat o = (CarpCat)s.get(CarpCat.class, 33);
		Assert.assertNotNull(o);
		s.close();
		int aftercount = BaseCarp.count(builder);
		Assert.assertEquals("after insert object count: ", count+1, aftercount);
		
		System.out.println("End Object Save UnTransaction case. SUCCESS!");
	}
}
