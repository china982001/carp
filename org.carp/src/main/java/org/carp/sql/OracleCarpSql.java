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

import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.annotation.CarpAnnotation.Generate;
import org.carp.beans.ColumnsMetadata;
import org.carp.beans.PrimarysMetadata;
import org.carp.impl.CarpQueryImpl;

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

	public int position() {
		return 0;
	}

	/**
	 * 取得执行insert操作时的insert sql语句
	 * @param table
	 * @return
	 */
	public String getInsertSql(){
		String insertSql = getInsertmap().get(getBeanInfo().getTable());
		if(insertSql == null){
			List<ColumnsMetadata> cms = getBeanInfo().getColumns();
			List<PrimarysMetadata> pks = getBeanInfo().getPrimarys();
			StringBuilder sql = new StringBuilder("insert into ");
			if(getBeanInfo().getSchema() !=null && !getBeanInfo().getSchema().trim().equals(""))
				sql.append(getBeanInfo().getSchema()).append(".");
			else if(getSchema() !=null && !getSchema().trim().equals(""))
				sql.append(getSchema()).append(".");
			sql.append(getBeanInfo().getTable()).append("(");
			for(int i=0,count = cms.size(); i<count; ++i){
				if(i!=0)
					sql.append(",");
				sql.append(cms.get(i).getColName());
			}
			for(PrimarysMetadata pk:pks){
				if(pk.getBuild() != Generate.auto){
					sql.append(",");
					sql.append(pk.getColName());
				}
			}
			sql.append(") values(");
			for(int i=0,count = cms.size(); i<count; ++i){
				if(i!=0)
					sql.append(",");
				if(cms.get(i).getFieldType().equals(Blob.class)){
					sql.append("empty_blob()");
				}else if(cms.get(i).getFieldType().equals(Clob.class)){
					sql.append("empty_clob()");
				}else
					sql.append("?");
				
			}
			
			for(int i=0, count = pks.size(); i < count; ++i){
				PrimarysMetadata pk = pks.get(i);
				if(pk.getBuild() != Generate.auto){
					sql.append(",");
					sql.append("?");
				}
			}
			sql.append(")");
			insertSql = sql.toString();
			getInsertmap().put(getBeanInfo().getTable(), insertSql);
		}
		return insertSql;
	}
	
	/**
	 * 取得执行update时候update sql语句
	 * @param cls
	 * @param table
	 * @return
	 */
	public String getUpdateSql(){
		String updateSql = getUpdatemap().get(getBeanInfo().getTable());
		if(updateSql == null){
			List<ColumnsMetadata> cms = getBeanInfo().getColumns();
			List<PrimarysMetadata> pks = getBeanInfo().getPrimarys();
			StringBuilder sql = new StringBuilder("update ");
			if(getBeanInfo().getSchema() !=null && !getBeanInfo().getSchema().trim().equals(""))
				sql.append(getBeanInfo().getSchema()).append(".");
			else if(getSchema() !=null && !getSchema().trim().equals(""))
				sql.append(getSchema()).append(".");
			sql.append(getBeanInfo().getTable()).append(" set ");
			for(int i = 0, count = cms.size(); i < count; ++i){
				if(i!=0) sql.append(",");
				sql.append(cms.get(i).getColName());
				if(cms.get(i).getFieldType().equals(Blob.class)){
					sql.append("= empty_blob()");
				}else if(cms.get(i).getFieldType().equals(Clob.class)){
					sql.append("= empty_clob()");
				}else
					sql.append("=?");
			}
			sql.append(" where ");
			for(int i = 0, count = pks.size(); i < count; ++i){
				if(i!=0)
					sql.append(" and ");
				sql.append(pks.get(i).getColName());
				sql.append("=?");
			}
			updateSql = sql.toString();
			getUpdatemap().put(getBeanInfo().getTable(), updateSql);
		}
		return updateSql;
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
