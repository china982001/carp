package org.carp.test.procedure;

import java.sql.Types;
import java.util.List;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.DataSet;
import org.carp.test.pojo.CarpBlob;
import org.carp.test.pojo.CarpCat;
import org.junit.Assert;



public class TestProcedure {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void test(CarpSessionBuilder  builder) throws Exception {
		testInParameter(builder);
		testInOutParameter(builder);
		testInOutParameter(builder);
		testInOutParamWithResultSet(builder);
		testInOutParamWith2ResultSet(builder);
		testRsAndDs(builder);
	}
	
	
	
	public static void testRsAndDs(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin testInOutParameter ....");
		CarpSession s=builder.getSession();
		CarpQuery query = s.createProcedureQuery("call test_proc5(?,?)",CarpBlob.class);
		query.setInt(1, 2);
		query.setString(2, "InOutParam");
		query.registerOutParameter(2, Types.VARCHAR);
		List list = query.listProcedureRs();
		Assert.assertEquals("has ResultSet: ", 2, list.size());
		List<?> lobs = (List<?>)list.get(0);
		DataSet cats = (DataSet)list.get(1);
		Assert.assertEquals("ResultSet has 70 recode: ", 67, cats.count());
		Assert.assertEquals("Blob ResultSet: ", "CLob数据类型测试", ((CarpBlob)lobs.get(0)).getCatSpec());
		Assert.assertEquals("Has Out Parameter : ", 1, query.getOutParameter().count());
		Assert.assertEquals("Parameter Value : ", "InOutParam", query.getOutParameter().getString(2));
		query.closeStatement();
		System.out.println("Begin testInOutParameter .  SUCCESS!");
	}
	
	public static void testInOutParamWith2ResultSet(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin testInOutParameter ....");
		CarpSession s=builder.getSession();
		CarpQuery query = s.createProcedureQuery("call test_proc4(?,?)",CarpCat.class,CarpBlob.class);
		query.setInt(1, 2);
		query.setString(2, "InOutParam");
		query.registerOutParameter(2, Types.VARCHAR);
		List list = query.listProcedureRs();
		Assert.assertEquals("has ResultSet: ", 2, list.size());
		List<?> cats = (List<?>)list.get(0);
		List<?> lobs = (List<?>)list.get(1);
		Assert.assertEquals("ResultSet has 70 recode: ", 67, cats.size());
		Assert.assertEquals("Blob ResultSet: ", "CLob数据类型测试", ((CarpBlob)lobs.get(0)).getCatSpec());
		Assert.assertEquals("Has Out Parameter : ", 1, query.getOutParameter().count());
		Assert.assertEquals("Parameter Value : ", "InOutParam", query.getOutParameter().getString(2));
		query.closeStatement();
		System.out.println("Begin testInOutParameter .  SUCCESS!");
	}
	
	public static void testInOutParamWithResultSet(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin testInOutParameter ....");
		CarpSession s=builder.getSession();
		CarpQuery query = s.createProcedureQuery("call test_proc3(?,?)",CarpCat.class);
		query.setInt(1, 2);
		query.setString(2, "InOutParam");
		query.registerOutParameter(2, Types.VARCHAR);
		List list = query.listProcedureRs();
		Assert.assertEquals("has ResultSet: ", 1, list.size());
		List<?> cats = (List<?>)list.get(0);
		Assert.assertEquals("ResultSet has 70 recode: ", 70, cats.size());
		Assert.assertEquals("Has Out Parameter : ", 1, query.getOutParameter().count());
		Assert.assertEquals("Parameter Value : ", "InOutParam", query.getOutParameter().getString(2));
		query.closeStatement();
		System.out.println("Begin testInOutParameter .  SUCCESS!");
	}
	
	public static void testInOutParameter(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin testInOutParameter ....");
		CarpSession s=builder.getSession();
		CarpQuery query = s.createProcedureQuery("call test_proc2(?,?)");
		query.setInt(1, 2);
		query.setString(2, "InOutParam");
		query.registerOutParameter(2, Types.VARCHAR);
		List list = query.listProcedureRs();
		Assert.assertEquals("No ResultSet: ", 0, list.size());
		Assert.assertEquals("Has Out Parameter : ", 1, query.getOutParameter().count());
		Assert.assertEquals("Parameter Value : ", "InOutParam", query.getOutParameter().getString(2));
		query.closeStatement();
		System.out.println("Begin testInOutParameter .  SUCCESS!");
	}
	
	public static void testInParameter(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin testInParameter ....");
		CarpSession s=builder.getSession();
		CarpQuery query = s.createProcedureQuery("call test_proc1(?,?)");
		query.setInt(1, 2);
		query.setString(2, "store procedure");
		List list = query.listProcedureRs();
		Assert.assertEquals("No ResultSet: ", 0, list.size());
		Assert.assertEquals("No Out Parameter : ", 0, query.getOutParameter().count());
		query.closeStatement();
		System.out.println("Begin testInParameter .  SUCCESS!");
	}
}
