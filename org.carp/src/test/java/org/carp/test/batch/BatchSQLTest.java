package org.carp.test.batch;


import java.util.Date;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.cfg.CarpConfig;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.carp.transaction.Transaction;
import org.junit.Assert;


public class BatchSQLTest {
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
		testSqlBatchInsert(builder);
		testSqlBatchInsertNoTransaction(builder);
		testSqlBatchInsertTransactionUnCommit(builder);
	}
	
	
	public static void testSqlBatchInsert(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin BatchSQL Insert Transaction case...");
		int count = BaseCarp.count(builder);
		long begin = System.currentTimeMillis();
		System.out.println("begin time ....  "+begin);
		CarpSession s = builder.getSession();
		Transaction tx = s.beginTransaction();
		CarpQuery q = s.createQuery("insert into carp_cat values(?,?,?,?,?)");
		for(int i=40; i<50; ++i){
			q.setInt(1, i);
			q.setString(2, "名称--"+i);
			q.setInt(3, i);
			q.setFloat(4, i);
			q.setDate(5, new Date());
			q.addBatch();
		}
		q.executeBatch();
		tx.commit();
		s.close();
		int afterCount = BaseCarp.count(builder);
		Assert.assertEquals(count+10, afterCount);
		System.out.println("cost time :  "+(System.currentTimeMillis()-begin));
		System.out.println("End BatchSQL Insert Transaction case. SUCCESS!");
	}
	public static void testSqlBatchInsertNoTransaction(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin BatchSQL Insert NoTransaction case...");
		int count = BaseCarp.count(builder);
		long begin = System.currentTimeMillis();
		System.out.println("begin time ....  "+begin);
		CarpSession s = builder.getSession();
		CarpQuery q = s.createQuery("insert into carp_cat values(?,?,?,?,?)");
		for(int i=50; i<60; ++i){
			q.setInt(1, i);
			q.setString(2, "名称--"+i);
			q.setInt(3, i);
			q.setFloat(4, i);
			q.setDate(5, new Date());
			q.addBatch();
		}
		q.executeBatch();
		s.close();
		int afterCount = BaseCarp.count(builder);
		Assert.assertEquals(count+10, afterCount);
		System.out.println("cost time :  "+(System.currentTimeMillis()-begin));
		System.out.println("End BatchSQL Insert NoTransaction case. SUCCESS!");
	}
	public static void testSqlBatchInsertTransactionUnCommit(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin BatchSQL Insert Transaction UnCommit case...");
		int count = BaseCarp.count(builder);
		long begin = System.currentTimeMillis();
		System.out.println("begin time ....  "+begin);
		CarpSession s = builder.getSession();
		Transaction tx = s.beginTransaction();
		CarpQuery q = s.createQuery("insert into carp_cat values(?,?,?,?,?)");
		for(int i=60; i<70; ++i){
			q.setInt(1, i);
			q.setString(2, "名称--"+i);
			q.setInt(3, i);
			q.setFloat(4, i);
			q.setDate(5, new Date());
			q.addBatch();
		}
		q.executeBatch();
		s.close();
		int afterCount = BaseCarp.count(builder);
		System.out.println("beforeCount="+count+" ,  afterCount="+afterCount);
		Assert.assertEquals(count, afterCount);
		System.out.println("cost time :  "+(System.currentTimeMillis()-begin));
		System.out.println("End BatchSQL Insert Transaction UnCommit case. SUCCESS!");
	}
	

}
