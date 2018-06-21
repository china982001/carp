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

import org.carp.impl.CarpQueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * oracle 数据库的sql生成类
 * @author zhou
 * @since 0.1
 */
public class OracleCarpSql extends AbstractSql {
	private static final Logger logger = LoggerFactory.getLogger(OracleCarpSql.class);
	@Override
	public String getPageSql(String sql){
		StringBuffer str = new StringBuffer("select * from ( select t.*, rownum carp_row_num from ( ");
		str.append(sql);
		str.append(") t ) where carp_row_num > ? and carp_row_num <= ?");
		sql = str.toString();
		if(logger.isDebugEnabled()) 
			logger.debug(sql);
		return sql;
	}

	public int offset() {
		return 0;
	}

	public String getSequenceSql(String seq) {
		return "select "+seq+".nextval as nextid from dual";
	}

	public void setQueryParameters(CarpQueryImpl query) throws SQLException {
		PreparedStatement ps = query.getStatement();
		ps.setInt(query.getParameters().count()+1, query.getFirstIndex());
		ps.setInt(query.getParameters().count()+2,  query.getFirstIndex() + query.getMaxCount());
	}
}
