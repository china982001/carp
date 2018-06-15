package org.carp.test.dynamicsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.script.SQLFactory;
import org.carp.test.base.BaseCarp;
import org.carp.test.pojo.CarpCat;
import org.junit.Assert;

public class DynamicSqlTest {
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
//		testLoopTime(builder);
		testNoConditionNode(builder);
		testWithWhereIfNode(builder);
		testWithIFNode(builder);
		testUseDiffCondition(builder);
		testLoopConditionByList(builder);
		testLoopCondWithIntArray(builder);
		testLoopCondWithIntegerArray(builder);
		testLoopCondWithStringArray(builder);//注意，这里在postgresql数据库上测试不通过，因为postgre不能隐式的自动转换类型，因此会抛出异常
		testLoopCondWithSet(builder);
		testLoopCond_In(builder);
		testAssignNode(builder);
	}
	public static void testLoopTime(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin testLoopTime   Condition String[]  ....");
		long d = System.currentTimeMillis();
		System.out.println();
		System.out.print("循环100万次花费的时间: ");
		for(int i=0; i<100000; ++i){
			String[] ints = {"1","2","3","4","5","6","7","8","9","10"};
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("catid", ints);
			SQLFactory.getSQL("m1/s4",map);
		}
		long e = System.currentTimeMillis();
		System.out.println((e-d)+" 毫秒\r\n");
		System.out.println("End testLoopTime  Condition  String[] .....  SUCCESS!");
	}
	
	
	public static void testNoConditionNode(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query No Condition ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s1"));
		Assert.assertEquals("record count:", q.list().size(),count);
		s.close();
		System.out.println("End DynamicSQL Query  No Condition.  SUCCESS!");
	}
	public static void testWithWhereIfNode(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query With WhereIFNode Condition ....");
		CarpSession s=builder.getSession();
		CarpCat cat = new CarpCat();
		cat.setCatWeight(0);
		cat.setCatAge((short) -1);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("po", cat);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s2",map));
		Assert.assertEquals("record count:", q.list().size(),47);
		s.close();
		System.out.println("End DynamicSQL Query  With WhereIFNode Condition.  SUCCESS!");
	}
	public static void testWithIFNode(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query With IFNOde Condition ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpCat cat = new CarpCat();
		cat.setCatWeight(0);
		cat.setCatId(1);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("po", cat);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s3",map));
		Assert.assertEquals("record count:", q.list().size(),1);
		cat.setCatWeight(-1);
		map.put("po", cat);
		q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s3",map));
		Assert.assertEquals("record count:", q.list().size(),count);
		
		s.close();
		System.out.println("End DynamicSQL Query  With IFNOde Condition.  SUCCESS!");
	}
	public static void testUseDiffCondition(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE DIFF  Condition ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		CarpCat cat = new CarpCat();
		cat.setCatWeight(-1);
		cat.setCatId(1);
		cat.setCatAge((short) 1);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("po", cat);
		System.out.println("map---------"+map);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s2",map));
		Assert.assertEquals("record count:", q.list().size(),47);
		
		cat = new CarpCat();
		cat.setCatWeight(-1);
		cat.setCatId(1);
		cat.setCatAge((short) -1);
		map.put("po", cat);
		q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s2",map));
		Assert.assertEquals("record count:", q.list().size(),count);
		
		
		s.close();
		System.out.println("End DynamicSQL Query  USE DIFF  Condition.  SUCCESS!");
	}
	
	public static void testLoopConditionByList(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE LOOP By List ....");
		CarpSession s=builder.getSession();
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("catid", list);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s4",map));
		Assert.assertEquals("record count:", q.list().size(),5);// id=10的记录已被删除了，所以应该是7
		
		s.close();
		System.out.println("End DynamicSQL Query  USE LOOP  By LIST......  SUCCESS!");
	}
	public static void testLoopCondWithIntArray(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE LOOP  Condition int[]  ....");
		CarpSession s=builder.getSession();
		int[] ints = {1,2,3,4,5,6,7,8,9,10};
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("catid", ints);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s4",map));
		Assert.assertEquals("record count:", q.list().size(),5);// id=10的记录已被删除了，所以应该是7
		
		s.close();
		System.out.println("End DynamicSQL Query  USE LOOP  Condition  int[] .....  SUCCESS!");
	}
	public static void testLoopCondWithIntegerArray(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE LOOP  Condition Integer[]  ....");
		CarpSession s=builder.getSession();
		Integer[] ints = {1,2,3,4,5,6,7,8,9,10};
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("catid", ints);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s4",map));
		Assert.assertEquals("record count:", q.list().size(),5);// id=10的记录已被删除了，所以应该是7
		
		s.close();
		System.out.println("End DynamicSQL Query  USE LOOP  Condition  Integer[] .....  SUCCESS!");
	}
	public static void testLoopCondWithStringArray(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE LOOP  Condition String[]  ....");
		CarpSession s=builder.getSession();
		String[] ints = {"1","2","3","4","5","6","7","8","9","10"};
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("catid", ints);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s4",map));
		Assert.assertEquals("record count:", q.list().size(),5);// id=10的记录已被删除了，所以应该是7
		
		s.close();
		System.out.println("End DynamicSQL Query  USE LOOP  Condition  String[] .....  SUCCESS!");
	}
	public static void testLoopCondWithSet(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE LOOP  Condition Set  ....");
		int count = BaseCarp.count(builder);
		CarpSession s=builder.getSession();
		Set<Integer> p = new HashSet<>();
		p.add(1);
		p.add(2);
		p.add(3);
		p.add(4);
		p.add(5);
		p.add(6);
		p.add(7);
		p.add(8);
		p.add(9);
		p.add(10);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("catid", p);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s4",map));
		Assert.assertEquals("record count:", q.list().size(),5);// id=10的记录已被删除了，所以应该是7
		
		s.close();
		System.out.println("End DynamicSQL Query  USE LOOP  Condition  Set .....  SUCCESS!");
	}
	public static void testLoopCond_In(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Query USE LOOP  Condition Integer[]  ....");
		CarpSession s=builder.getSession();
		Integer[] ints = {1,2,3,4,5,6,7,8,9,10};
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("catid", ints);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s5",map));
		Assert.assertEquals("record count:", q.list().size(),6);// id=10的记录已被删除了，所以应该是7
		
		s.close();
		System.out.println("End DynamicSQL Query  USE LOOP  Condition  Integer[] .....  SUCCESS!");
	}
	
	public static void testAssignNode(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DynamicSQL Assign   ....");
		CarpSession s=builder.getSession();
		CarpCat cat = new CarpCat();
		Map<String,Object> map =new HashMap<String,Object>();
		cat.setCatName("cat");
		map.put("po", cat);
		CarpQuery q = s.creatQuery(CarpCat.class,SQLFactory.getSQL("m1/s6",map));
		Assert.assertEquals("record count:", q.list().size(),20);// like匹配查询
		
		s.close();
		System.out.println("End DynamicSQL    Assign .....  SUCCESS!");
	}
}
