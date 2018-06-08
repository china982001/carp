package org.carp.test.map;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.test.base.BaseCarp;
import org.carp.transaction.Transaction;
import org.junit.Assert;

public class MapObjectTest {

	public static void test(CarpSessionBuilder  builder)throws Exception{
		testSaveMap(builder);
		testSaveMapNoTransaction(builder);
		testSaveMapUnCommit(builder);
	}
	
	public static void testSaveMap(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Map Save Object ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		for(int i=70; i<80; i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("cat_id", i);
			map.put("cat_name", "carp_cat_"+i);
			map.put("sts_time", new Date());
			s.save("carp_cat",map);
		}
		tx.commit();
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count+10, afterCount);
		System.out.println("End Map Save Object.  SUCCESS!");
	}
	
	public static void testSaveMapNoTransaction(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Map Save Object NoTransaction ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		for(int i=80; i<90; i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("cat_id", i);
			map.put("cat_name", "carp_cat_"+i);
			map.put("sts_time", new Date());
			s.save("carp_cat",map);
		}
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count+10, afterCount);
		System.out.println("End Map Save Object NoTransaction .  SUCCESS!");
	}
	public static void testSaveMapUnCommit(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Map Save Object UnCommit ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		Transaction tx = s.beginTransaction();
		for(int i=90; i<100; i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("cat_id", i);
			map.put("cat_name", "carp_cat_"+i);
			map.put("sts_time", new Date());
			s.save("carp_cat",map);
		}
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count, afterCount);
		System.out.println("End Map Save Object UnCommit .  SUCCESS!");
	}
	
	
}
