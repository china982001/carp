package org.carp.test.jdbc;

import java.sql.ResultSet;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;

public class TableTest {

	public static void test(CarpSessionBuilder  builder)throws Exception{
		testDeleteTable(builder);
		testCreateTable(builder);
	}
	
	public static void testDeleteTable(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin  Drop Test Table：carp_cat ....");
		CarpSession s = builder.getSession();
		ResultSet rs = s.getConnection().getMetaData().getTables(null, null, "CARP_CAT", null);
		boolean flag = false;
		while(rs.next())
			flag = true;
		if(flag)
			s.creatUpdateQuery("drop table carp_cat").executeUpdate();
		s.close();
		System.out.println("End  Drop Test Table：carp_cat .    SUCCESS!");
	}
	
	public static void testCreateTable(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin  Create Test Table：carp_cat ....");
		CarpSession s = builder.getSession();
			s.creatUpdateQuery("create table carp_cat"
					+ "("
					+ " cat_id int not null primary key, "
					+ " cat_name varchar(30), "
					+ " cat_age integer, "
					+ " cat_weight float, "
					+ " sts_time datetime"
					+ " )").executeUpdate();
		s.close();
		System.out.println("End  Create Test Table：carp_cat .    SUCCESS!");
	}
}
