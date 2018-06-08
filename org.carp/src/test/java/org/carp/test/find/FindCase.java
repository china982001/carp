package org.carp.test.find;


import java.util.HashMap;
import java.util.Map;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.test.pojo.CarpCat;
import org.junit.Assert;

public class FindCase {

	public static void test(CarpSessionBuilder  builder)throws Exception{
		findById(builder);
		findByMap(builder);
	}
	public static void findById(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Find Object by id ....");
		CarpSession s=builder.getSession();
		CarpCat o = (CarpCat)s.get(CarpCat.class, 4);
		Assert.assertEquals(o.getCatId().intValue(), 4);
		System.out.println(o+"\r\n"+o.getCatName());
		System.out.println("End Find Object by id ....  SUCCESS!");
	}
	
	public static void findByMap(CarpSessionBuilder  builder)throws Exception{
		System.out.println();
		System.out.println();
		System.out.println("Begin Find Object by MAP ....");
		Map map = new HashMap();
		map.put("cat_id",5);
		CarpSession s=builder.getSession();
		CarpCat o = (CarpCat)s.get(CarpCat.class, map);
		Assert.assertEquals(o.getCatId().intValue(), 5);
		System.out.println(o+"\r\n"+o.getCatName());
		System.out.println("End Find Object by MAP ....  SUCCESS!");
	}
}
