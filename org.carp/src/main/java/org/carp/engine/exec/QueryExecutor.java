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
package org.carp.engine.exec;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.carp.engine.metadata.ColumnInfo;
import org.carp.engine.metadata.MetaData;
import org.carp.impl.CarpQueryImpl;

/**
 * 查询执行器类
 * @author zhou
 * @since 0.2
 */
public class QueryExecutor extends Executor{
	private ResultSet rs;

	public QueryExecutor(CarpQueryImpl query) throws Exception{
		super(query);
	}

	@Override
	protected void executeStatement() throws Exception {
		rs = this.getQuery().getStatement().executeQuery();
	}
	
	/**
	 * 返回结果集
	 * @return
	 */
	public ResultSet getResultSet(){
		return rs;
	}
	
	/**
	 * 取得查询字段数组
	 * @return
	 */
	public String[] getReturnNames(){
		return this.getMetadata().getColumnsMap().keySet().toArray(new String[0]);
	}
	/**
	 * 取得查询字段的类型数组
	 * @return
	 */
	public Class<?>[] getReturnTypes(){
		Collection<ColumnInfo> colls = this.getMetadata().getColumnsMap().values();
		List<Class<?>> clz = new ArrayList<Class<?>>();
		for(ColumnInfo col : colls){
			clz.add(col.getJavaType());
		}
		return clz.toArray(new Class<?>[0]);
	}

	@Override
	protected void parserMetaData() throws Exception {
		this.setMetadata(new MetaData(rs));
	}
}
