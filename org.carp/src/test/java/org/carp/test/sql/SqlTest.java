package org.carp.test.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carp.script.SQL;
import org.carp.script.SQLFactory;
import org.carp.script.SQLParam;
import org.carp.test.pojo.CarpCat;

public class SqlTest {

	/**
	 *  test base sql
	 * @throws Exception 
	 */
	public static void testBaseSql() throws Exception{
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("aa", "aaaaaaaaa");
		map.put("cc", 2);
		CarpCat cat = new CarpCat();
		cat.setCatName("cat-222");
		map.put("cat", cat);
		List<String> dd = new ArrayList<String>();
		dd.add("abc1");
		dd.add("abc2");
		dd.add("abc3");
		dd.add("abc4");
		dd.add("abc5");
		map.put("list", dd);
		
		SQL sql = SQLFactory.getSQL("m1/s1",map);
		System.out.println(sql.getSql());
		List<Object> list = sql.getParamValues();
		for(Object o : list){
			SQLParam p = (SQLParam)o;
			System.out.println(p.getName()+" - "+p.getValue());
		}
		
	}
}
