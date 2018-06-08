/**
 * Copyright 2009-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.carp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.impl.CarpQueryImpl;

public class DB2CarpSql extends AbstractSql {
	private static final Logger logger = LoggerFactory.getLogger(DB2CarpSql.class);
	/**
	 * 取得执行分页查询时，select查询的分页sql语句兴汉龙腾三国兵锋 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPageSql(String sql) {
		StringBuffer str = new StringBuffer("select * from ( select rownumber() over() as carp_row_num,t.* from (");
		str.append(sql);
		str.append(") t) t where t.carp_row_num > ? and t.carp_row_num <= ?");
		sql = str.toString();
		if(logger.isDebugEnabled())
			logger.debug(sql);
		return sql;
	}

	public int position() {
		return 0;
	}

	public String getSequenceSql(String seq) {
		return "select nextval for "+seq+"  from sysibm.sysdummy1";
	}

	/**
	 * 使用DB2的rownumber函数进行分页查询
	 */
	public void setQueryParameters(CarpQueryImpl query) throws SQLException {
		PreparedStatement ps = query.getStatement();
		ps.setInt(query.getParameters().count()+1, query.getFirstIndex());
		ps.setInt(query.getParameters().count()+2,  query.getFirstIndex() + query.getMaxCount());
	}
}
