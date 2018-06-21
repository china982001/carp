package org.carp.test.query;


import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.junit.Assert;


public class ClassQueryCase {
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testQueryByClassNoCondition(builder);
		testQueryByClassPage(builder);
		testQueryByClassSql(builder);
	}
	
	public static void testQueryByClassNoCondition(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Query No Condition ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpQuery q = s.createQuery(CarpCat.class);
		Assert.assertEquals("record count:", q.list().size(),count);
		s.close();
		System.out.println("End Query No Condition.  SUCCESS!");
	}
	public static void testQueryByClassPage(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Query Page ....");
		CarpSession s=builder.getSession();
		CarpQuery q = s.createQuery(CarpCat.class);
		q.setFirstIndex(0);
		q.setMaxCount(20);
		Assert.assertEquals("record count:", q.list().size(),20);
		s.close();
		System.out.println("End Query Page.  SUCCESS!");
	}
	public static void testQueryByClassSql(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Query SQL Condition ....");
		CarpSession s=builder.getSession();
		CarpQuery q = s.createQuery("select * from carp_cat where cat_id > ?",CarpCat.class);
		q.setInt(1, 10);
		Assert.assertEquals("record count:", q.list().size(),58);
		s.close();
		System.out.println("End Query SQL Condition.  SUCCESS!");
	}
	
	
	
	
}
