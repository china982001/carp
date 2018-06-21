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
package org.carp.engine.metadata;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.type.TypeMapping;
import org.carp.util.EntityUtil;

/**
 * 元数据类
 * @author zhou
 * @since 0.1
 */
public class MetaData {
	protected static final Logger logger = LoggerFactory.getLogger(MetaData.class);
	private LinkedHashMap<String,ColumnInfo> colsMap = new LinkedHashMap<String,ColumnInfo>(8);
	
	private int columnCount;
	
	public MetaData(ResultSet rs)throws Exception{
		parserResultSetMeta(rs.getMetaData());
	}
	
	/**
	 * 解析ResultSet元数据
	 * @param rsmd
	 * @throws Exception
	 */
	private void parserResultSetMeta(ResultSetMetaData rsmd)throws Exception{
		columnCount = rsmd.getColumnCount();
		for(int i = 1; i <= columnCount; ++i){
			String col = rsmd.getColumnLabel(i).toUpperCase();
			ColumnInfo info = new ColumnInfo();
			info.setColname(col);
			info.setFieldname(EntityUtil.getFieldName(col));
			info.setSqlType(rsmd.getColumnType(i));
			info.setJavaType(TypeMapping.getJavaTypeBySqlType(info.getSqlType()));
			info.setAssemble(TypeMapping.getAssembleBySqlType(info.getSqlType()));
			info.setIdx(i-1);
			colsMap.put(col, info);
			if(logger.isDebugEnabled())
				logger.debug(info.toString());
		}
	}

	public LinkedHashMap<String,ColumnInfo> getColumnsMap(){
		return colsMap;
	}
}
