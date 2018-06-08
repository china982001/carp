package org.carp.test.query;


import java.sql.ResultSet;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.DataSet;
import org.carp.test.base.BaseCarp;
import org.junit.Assert;

public class DatasetQueryCase {
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testQueryBySqlNoCondition(builder);
		testQueryBySqlPage(builder);
		testQueryBySqlWithCondition(builder);
		testDataSetByID(builder);
	}
	
	
	public static void testQueryBySqlNoCondition(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DataSetQuery No Condition ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpQuery q = s.creatDataSetQuery("select * from carp_cat");
		Assert.assertEquals("record count:", q.dataSet().count(),count);
		s.close();
		System.out.println("End DataSetQuery No Condition.  SUCCESS!");
	}
	public static void testQueryBySqlPage(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DataSetQuery Page ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpQuery q = s.creatDataSetQuery("select * from carp_cat");
		q.setFirstIndex(0);
		q.setMaxCount(20);
		Assert.assertEquals("record count:", q.dataSet().count(),20);
		s.close();
		System.out.println("End DataSetQuery Page.  SUCCESS!");
	}
	public static void testQueryBySqlWithCondition(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DataSetQuery SQL Condition ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpQuery q = s.creatDataSetQuery("select * from carp_cat where cat_id > ?");
		q.setInt(1, 10);
		DataSet ds = q.dataSet();
		Assert.assertEquals("record count:", ds.count(),count-10);
		s.close();
		System.out.println("End DataSetQuery SQL Condition.  SUCCESS!");
	}
	
	public static void testDataSetByID(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DataSetQuery By ID ....");
		CarpSession s=builder.getSession();
		CarpQuery q = s.creatDataSetQuery("select * from carp_cat where cat_id = ?");
		q.setInt(1, 20);
		DataSet ds = q.dataSet();
		while(ds.next()){
			String name = ""+ds.getData("cat_name");
			Assert.assertEquals("cat_name value:", "名称--20",name);
		}
		Assert.assertEquals("record count:", 1,ds.count());
		s.close();
		System.out.println("End DataSetQuery By ID.  SUCCESS!");
	}
	
}
