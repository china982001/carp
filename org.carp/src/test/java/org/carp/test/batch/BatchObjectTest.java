package org.carp.test.batch;


import java.util.Date;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.carp.transaction.Transaction;
import org.junit.Assert;


public class BatchObjectTest {
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testBatchSaveObject(builder);
		testBatchSaveObjectUnCommit(builder);
		testBatchSaveObjectUnTransaction(builder);
	}
	
	public static void testBatchSaveObject(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Object Insert TransactionCommit  case...");
		int cnt = BaseCarp.count(builder);//当前记录数， == 0
		Assert.assertEquals(0, cnt);
		CarpSession s= builder.getSession();
		Transaction tx = s.beginTransaction();
		long begin = System.currentTimeMillis();
		System.out.println("begin time ....  "+begin);
		for(int i=0; i<20; ++i){
			CarpCat c = new CarpCat();
			c.setCatId(i);
			c.setCatName("名称--"+i);
			c.setCatAge((short)i);
			c.setCatWeight(i);
			c.setStsTime(new Date());
			s.save(c);
		}
		tx.commit();
		s.close();
		System.out.println("cost time :  "+(System.currentTimeMillis()-begin));
		int afterCnt = BaseCarp.count(builder);//当前记录数， == 20
		Assert.assertEquals(cnt+20, afterCnt);
		System.out.println("End Object Insert TransactionCommit case. SUCCESS!");
	}
	
	public static void testBatchSaveObjectUnCommit(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("End Object Batch Insert Transaction  Uncommit case...");
		int org_count = BaseCarp.count(builder);
		CarpSession s= builder.getSession();
		Transaction tx = s.beginTransaction();
		long begin = System.currentTimeMillis();
		System.out.println("begin time ....  "+begin);
		for(int i=20; i<30; ++i){
			CarpCat c = new CarpCat();
			c.setCatId(i);
			c.setCatName("名称-------------"+i);
			c.setCatAge((short)i);
			c.setCatWeight(i);
			c.setStsTime(new Date());
			s.save(c);
		}
		s.close();
		System.out.println("cost time :  "+(System.currentTimeMillis()-begin));
		int after_count = BaseCarp.count(builder);
		Assert.assertEquals(org_count, after_count);
		if(org_count == after_count)
			System.out.println("End Object Batch Insert Transaction  Uncommit case. SUCCESS!");
		else
			System.out.println("End Object Insert Transaction Uncommit case. Failed!");
	}
	
	public static void testBatchSaveObjectUnTransaction(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Object Insert UnTransaction  case...");
		int count = BaseCarp.count(builder);
		long begin = System.currentTimeMillis();
		System.out.println("begin time ....  "+begin);
		CarpSession s= builder.getSession();
		for(int i=20; i<30; ++i){
			CarpCat c = new CarpCat();
			c.setCatId(i);
			c.setCatName("名称--"+i);
			c.setCatAge((short)i);
			c.setCatWeight(i);
			c.setStsTime(new Date());
			s.save(c);
		}
		s.close();
		System.out.println("cost time :  "+(System.currentTimeMillis()-begin));
		int after_count = BaseCarp.count(builder);
		Assert.assertEquals(count+10, after_count);
		System.out.println("End Object Insert UnTransaction case. SUCCESS!");
	}
	

}
