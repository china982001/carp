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
import org.carp.beans.CarpBean;
import org.carp.beans.MappingMetadata;
import org.carp.beans.PrimarysMetadata;
import org.carp.exception.CarpException;
import org.carp.factory.BeansFactory;
import org.carp.impl.CarpQueryImpl;

public class SqlServer2005CarpSql extends AbstractSql {
	private static final Logger logger = LoggerFactory.getLogger(SqlServer2005CarpSql.class);
	
	
	
	@Override
	public String getPageSql(Class<?> clazz) throws CarpException {
		CarpBean bean = BeansFactory.getBean(clazz);
		if(bean == null)
			throw new CarpException("Class:"+clazz+" has not a assiosite mapping Table!");
		String querySql = selectPageMap.get(bean.getTable());
		if(querySql == null){
			StringBuilder sql = new StringBuilder("SELECT ROW_NUMBER() OVER( ORDER BY ");
			for(int i=0, count=bean.getPrimarys().size();i < count; ++i){
				PrimarysMetadata pm = bean.getPrimarys().get(i);
				if(i != 0)
					sql.append(",");
				sql.append("T0_.").append(pm.getColName());
			}
			sql.append(") AS CARP_ROW_NUM,");
			for(PrimarysMetadata pk : bean.getPrimarys())
				sql.append("T0_.").append(pk.getColName()).append(",");
			this.appendColumens(bean,sql, "T0_");
			for(int i=0,count =bean.getMaps().size(); i<count; ++i){
				MappingMetadata mapMeta = bean.getMaps().get(i);
				sql.append(",").append("T").append(i+1).append("_.");
				sql.append(mapMeta.getMapColumn()).append(" ").append(mapMeta.getMasterAlias());
			}
			sql.append(" FROM ");
			this.appendSchema(bean,sql);
			sql.append(bean.getTable()).append(" T0_");
			for(int i=0,count =bean.getMaps().size(); i<count; ++i){
				MappingMetadata mapMeta = bean.getMaps().get(i);
				sql.append(", ");
				sql.append((mapMeta.getMapSchema() != null) ? mapMeta.getMapSchema() +"." : "");
				sql.append(mapMeta.getMapTable()).append(" T").append(i+1).append("_ ");
			}
			if(!bean.getMaps().isEmpty()){
				sql.append(" WHERE ");
				for(int i=0,count = bean.getMaps().size(); i<count; ++i){
					MappingMetadata mm = bean.getMaps().get(i);
					if(i!=0)
						sql.append(" AND ");
					sql.append("T0_.").append(mm.getFkColumn()).append(" = ");
					sql.append("T").append(i+1).append("_.").append(mm.getPkColumm());
				}
			}
			querySql = sql.toString();
			selectPageMap.put(bean.getTable(), querySql);
		}
		
		return querySql;
	}


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

		if(logger.isDebugEnabled())
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
		ps.setInt(query.getParameters().count()+2,  query.getFirstIndex() + query.getMaxCount());
	}
}
