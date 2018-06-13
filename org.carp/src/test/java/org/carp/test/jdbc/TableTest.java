package org.carp.test.jdbc;

import java.sql.ResultSet;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.sql.CarpSql;
import org.carp.sql.DB2CarpSql;
import org.carp.sql.DerbyCarpSql;
import org.carp.sql.H2CarpSql;
import org.carp.sql.HSQLCarpSql;
import org.carp.sql.MySqlCarpSql;
import org.carp.sql.OracleCarpSql;
import org.carp.sql.PostgreSqlCarpSql;

public class TableTest {
	
	private static String catSql ="create table carp_cat(cat_id integer not null primary key, cat_name varchar(30), cat_age integer, cat_weight float, sts_time timestamp)";
	private static String lobSql ="create table carp_blob ( cat_id int not null primary key,  cat_image blob, cat_spec clob )";
	private static String catTable = "carp_cat".toUpperCase();
	private static String lobTable = "carp_blob".toUpperCase();
	
	public static void test(CarpSessionBuilder  builder)throws Exception{
		createSQL(builder);
		testDropTable(builder);
		testCreateTable(builder);
	}
	
	
	public static void createSQL(CarpSessionBuilder  builder)throws Exception{
		Class<? extends CarpSql> clz = builder.getConfig().getDatabaseDialect();
		if(clz.equals(HSQLCarpSql.class)){
			
		}else if(clz.equals(MySqlCarpSql.class)){
			catSql= "create table carp_cat(cat_id integer not null primary key, cat_name varchar(30), cat_age integer, cat_weight float, sts_time timestamp)";
			lobSql = "create table carp_blob ( cat_id int not null primary key,  cat_image blob, cat_spec text )";
		}else if(clz.equals(OracleCarpSql.class)){
			catSql= "create table carp_cat(cat_id integer not null primary key, cat_name varchar2(30), cat_age integer, cat_weight float, sts_time timestamp)";
		}else if(clz.equals(DB2CarpSql.class)){
			catSql= "create table carp_cat(cat_id integer not null primary key, cat_name varchar(30), cat_age integer, cat_weight float, sts_time timestamp)";
			lobSql = "create table carp_blob ( cat_id int not null primary key,  cat_image blob, cat_spec clob )";
		}else if(clz.equals(DerbyCarpSql.class)){
			catSql= "create table carp_cat(cat_id integer not null primary key, cat_name varchar(30), cat_age integer, cat_weight float, sts_time timestamp)";
			lobSql = "create table carp_blob ( cat_id int not null primary key,  cat_image blob, cat_spec clob )";
		}else if(clz.equals(H2CarpSql.class)){
			catSql= "create table carp_cat(cat_id integer not null primary key, cat_name varchar(30), cat_age integer, cat_weight float, sts_time timestamp)";
			lobSql = "create table carp_blob ( cat_id int not null primary key,  cat_image blob, cat_spec clob )";
		}else if(clz.equals(PostgreSqlCarpSql.class)){
			catSql= "create table carp_cat(cat_id integer not null primary key, cat_name varchar(30), cat_age integer, cat_weight float, sts_time timestamp)";
			lobSql = "create table carp_blob ( cat_id int not null primary key,  cat_image bytea, cat_spec text )";
			catTable = catTable.toLowerCase();
			lobTable = lobTable.toLowerCase();
		}else{
			throw new Exception("不受支持的数据库类型");
		}
	}
	
	public static void testDropTable(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin  Drop Test Table：carp_cat and carp_blob Case ....");
		dropTable(builder,catTable);
		dropTable(builder,lobTable);
		System.out.println("End  Drop Test Table：carp_cat  and carp_blob Case .    SUCCESS!");
	}
	
	public static void testCreateTable(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin  Create Test Table：carp_cat and carp_blob Case ....");
		createTable(builder,catSql);
		createTable(builder,lobSql);
		System.out.println("End  Create Test Table：carp_cat and carp_blob Case .    SUCCESS!");
	}
	
	private static void dropTable(CarpSessionBuilder  builder,String tablename)throws Exception{
		CarpSession s = builder.getSession();
		ResultSet rs = s.getConnection().getMetaData().getTables(null, null, tablename, null);
		boolean flag = false;
		while(rs.next())
			flag = true;
		System.out.println(tablename+" ============  "+flag);
		if(flag)
			s.creatUpdateQuery("DROP TABLE " + tablename).executeUpdate();
		s.close();
	}
	
	private static void createTable(CarpSessionBuilder  builder ,String sql)throws Exception{
		CarpSession s = builder.getSession();
		s.creatUpdateQuery(sql).executeUpdate();
		s.close();
	}
}
