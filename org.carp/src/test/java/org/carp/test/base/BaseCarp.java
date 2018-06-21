package org.carp.test.base;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.DataSet;

public class BaseCarp {

	public static int count(CarpSessionBuilder  builder)throws Exception{
		CarpSession s= builder.getSession();
		CarpQuery q = s.createQuery("select count(*) from carp_cat");
		DataSet ds = q.dataSet();
		int cnt = 0;
		cnt = Integer.parseInt(ds.getData().get(0).get(0)+"");	
		s.close();
		return cnt;
	}
}
