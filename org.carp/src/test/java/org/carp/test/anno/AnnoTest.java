package org.carp.test.anno;


import org.carp.beans.CarpBean;
import org.carp.factory.BeansFactory;


import org.junit.*;

public class AnnoTest {
	
	public static void test()throws Exception{
		testAnnotation();
	}
	
	public static void testAnnotation()throws Exception{
		System.out.println("\r\n**********  BEGIN  CARP FUNCTION TEST       ********************");
		CarpBean bean = BeansFactory.getBean(org.carp.test.pojo.TCarp.class);
		System.out.println("begin Annotation testcase......");
		Assert.assertEquals("table name: ", "T_CARP", bean.getTable());
		Assert.assertEquals("table column count: ", 6, bean.getColumns().size());
		Assert.assertEquals("table pk count: ", 1, bean.getPrimarys().size());
		Assert.assertEquals("table column[1] name : ", "T_NAME", bean.getColumns().get(0).getColName());
		Assert.assertEquals("table column[2] name : ", "T_CNAME", bean.getColumns().get(1).getColName());
		Assert.assertEquals("table column[6] name : ", "STS", bean.getColumns().get(5).getColName());
		
		Assert.assertEquals("table column[1]'Null : ", false, bean.getColumns().get(0).isNull());
		
		System.out.println("end Annotation testcase.  success!!!");
		
	}
	

}
