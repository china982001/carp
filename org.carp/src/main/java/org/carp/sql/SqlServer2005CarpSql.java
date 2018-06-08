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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;

public class SqlServer2005CarpSql extends AbstractSql {
	private static final Logger logger = LoggerFactory.getLogger(SqlServer2005CarpSql.class);
	/**
	 * 取得执行分页查询时，select查询的分页sql语句
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPageSql(String sql) {
		StringBuilder buf = new StringBuilder("select _t.* from (select row_number() over(");
		int orderIndex = sql.toLowerCase().indexOf(" order ");
		if(orderIndex > 0)
			buf.append(sql.substring(orderIndex +1));
		else{
			buf.append(sql.substring(sql.indexOf(" "),sql.indexOf(",")));
		}
		buf.append(") carp_row_num, ");
		if(orderIndex > 0)
			buf.append(sql.substring(6,orderIndex ));
		else
			buf.append(sql.substring(6));
		buf.append(") _t where _t.carp_row_num between ? and ?");
		sql = buf.toString();
//		
//		int index = sql.toLowerCase().indexOf("select ")+6;
//		String left = sql.trim().substring(6).trim();
//		String firstColumn = left.substring(0, left.indexOf(","));
//		StringBuilder str = new StringBuilder("select tt.* from ( select row_number() over( order by ");
//		str.append(firstColumn);
//		str.append(") row_num, ");
//		str.append(sql.substring(index));
//		str.append(") tt  where tt.row_num > ? and tt.row_num <= ?");
//		sql = str.toString();
		if(logger.isDebugEnabled())
			logger.debug(sql);
		return sql;
	}


	public int position() {
		return 0;
	}

	public String getSequenceSql(String seq) throws CarpException {
		throw new CarpException("SQLServer unsupported sequence.");
	}

	public void setQueryParameters(CarpQueryImpl query) throws SQLException {
		PreparedStatement ps = query.getStatement();
		ps.setInt(query.getParameters().count()+1, query.getFirstIndex());
		ps.setInt(query.getParameters().count()+2,  query.getFirstIndex() + query.getMaxCount());
	}
}
