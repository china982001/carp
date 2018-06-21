package org.carp.test.metadata;

import java.sql.ResultSet;
import java.util.List;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.DataSet;
import org.carp.test.pojo.CarpCat;

public class MetadataTest {

	public static void main(String[] args) throws Exception {
//		resultSetMetadata();
//		dataSetMetadata();
//		clzMetadata();
	}
	public static void test(CarpSessionBuilder  builder)throws Exception{
		resultSetMetadata(builder);
		dataSetMetadata(builder);
		clzMetadata(builder);
	}
	public static void resultSetMetadata(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin ResultSet MetaData ....");
		CarpSession s=builder.getSession();
		CarpQuery q = s.createQuery("select * from carp_cat");
		q.setMaxCount(2);
		q.setFirstIndex(0);
		ResultSet rs = q.resultSet();
		rs.close();
		System.out.println(q.getReturnNames().length);
		System.out.println("End ResultSet MetaData ....  SUCCESS!");
	}
	
	public static void dataSetMetadata(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin DataSet MetaData ....");
		CarpSession s=builder.getSession();
		CarpQuery q = s.createQuery("select * from carp_cat");
		q.setMaxCount(3);
		q.setFirstIndex(0);
		DataSet ds = q.dataSet();
		System.out.println(q.getReturnNames().length);
		System.out.println("End DataSet MetaData ....  SUCCESS!");
	}
	
	public static void clzMetadata(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin ClassQuery MetaData by id ....");
		CarpSession s=builder.getSession();
		CarpQuery q = s.createQuery("select * from carp_cat",CarpCat.class);
		q.setMaxCount(3);
		q.setFirstIndex(0);
		List ds = q.list();
		System.out.println("记录数："+ds.size());
		System.out.println(q.getReturnNames().length);
		
		System.out.println("End ClassQuery MetaData ....  SUCCESS!");
	}

}
