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

public class H2CarpSql extends AbstractSql {
	private static final Logger logger = LoggerFactory.getLogger(H2CarpSql.class);
	/**
	 * 取得执行分页查询时，select查询的分页sql语句兴汉龙腾三国兵锋 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPageSql(String sql){
		StringBuffer carpSQL = new StringBuffer(sql);
		carpSQL.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
		sql = carpSQL.toString();
		if(logger.isDebugEnabled())
			logger.debug(sql);
		return sql;
	}

	public int position() {
		// TODO Auto-generated method stub
		return 2;
	}

	public String getSequenceSql(String seq) throws CarpException {
		throw new CarpException("HSQLDB not support sequences!");
	}

	public void setQueryParameters(CarpQueryImpl query) throws SQLException {
		PreparedStatement ps = query.getStatement();
		ps.setInt(1, query.getFirstIndex());
		ps.setInt(2, query.getMaxCount());
	}
}
