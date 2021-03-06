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

import org.carp.beans.CarpBean;
import org.carp.beans.PrimarysMetadata;
import org.carp.exception.CarpException;
import org.carp.factory.BeansFactory;
import org.carp.impl.CarpQueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlServerCarpSql extends AbstractSql {
	private static final Logger logger = LoggerFactory.getLogger(SqlServerCarpSql.class);
	
	@Override
	public String getPageSql(Class<?> clazz) throws CarpException {
		CarpBean bean = BeansFactory.getBean(clazz);
		String queryPageSql = selectPageMap.get(bean.getTable());
		if(queryPageSql == null){
			String orderby = "";
			for(PrimarysMetadata pm : bean.getPrimarys()){
				orderby += "," + pm.getColName();	
			}
			queryPageSql =  this.getPageSql(this.getQuerySql(clazz) +" ORDER BY " + orderby.substring(1)); //this.getPageSql(this.getQuerySql(clazz)); 
			selectPageMap.put(bean.getTable(), queryPageSql);
		}
		return queryPageSql; 
	}


	/**
	 * 取得执行分页查询时，select查询的分页sql语句
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPageSql(String sql) {
		StringBuffer carpSQL = new StringBuffer(sql);
		carpSQL.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
		sql = carpSQL.toString();
		logger.debug(sql);
		return sql;
	}


	public int offset() {
		return 0;
	}

	public String getSequenceSql(String seq) throws CarpException {
		throw new CarpException("SQLServer unsupported sequence.");
	}

	public void setQueryParameters(CarpQueryImpl query) throws SQLException {
		PreparedStatement ps = query.getStatement();
		ps.setInt(query.getParameters().count()+1, query.getFirstIndex());
		ps.setInt(query.getParameters().count()+2, query.getMaxCount());
	}
}
