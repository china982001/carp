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
package org.carp.engine.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.carp.engine.metadata.ColumnInfo;
import org.carp.engine.metadata.MetaData;
import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;
import org.carp.sql.CarpSql;

/**
 * java.sql.ResultSet 结果集记录的对象组装类
 * 把结果集的记录转换封装成对象(class object)、装在list集合中返回
 * @author zhou
 *
 */
public class RSProcessor {
	private Class<?> cls;
	private ResultSet rs;
	private MetaData cqmd;
	private CarpQueryImpl query;
	private int limitCount;
	
	/**
	 * 构造函数
	 * @param cls 封装对象类
	 * @param cqmd  结果集元数据
	 * @param rs  结果集
	 * @throws CarpException
	 */
	public RSProcessor(CarpQueryImpl query, Class<?> cls, MetaData cqmd,ResultSet rs) throws CarpException{
		this.query = query;
		this.cls = cls;
		this.rs = rs;
		this.cqmd = cqmd;
		limitCount = this.query != null && this.query.getMaxCount() != -1 ?  this.query.getMaxCount() :Integer.MAX_VALUE;
	}
	
	/**
	 * 取得记录转换成对象的集合
	 * @return 对象集合
	 * @throws Exception
	 */
	public List<Object> list()throws Exception{
		List<Object> list = new ArrayList<Object>();
		moveResultSetCursor();
		int count = 0;
		while((rs.next()) && (count < limitCount)){
			list.add(processResultSet());
			count++;
		}
		return list;
	}
	
	/**
	 * 把记录转换封装成对象
	 * @return 封装的对象
	 * @throws Exception
	 */
	private Object processResultSet() throws Exception{
		Object vo = this.cls.newInstance();
		for(String col: cqmd.getColumnsMap().keySet()){
			if(col.equals("CARP_ROW_NUM"))
				continue;
			ColumnInfo info = cqmd.getColumnsMap().get(col);
			info.getAssemble().setMethodValue(rs, vo, info.getMethod().getMethod(), info.getIdx()+1);//
		}
		return vo;
	}
	
	///////////////////////////////////////////
	
	public void createDataSet(List<List<Object>> row) throws Exception{
		moveResultSetCursor();
		int count = 0;
		while((rs.next()) && (count < limitCount)){
			row.add(recodeToSingleRow());
			count ++;
		}
	}
	
	private List<Object> recodeToSingleRow() throws Exception{
		List<Object> data = new ArrayList<>();
		for(ColumnInfo col : this.cqmd.getColumnsMap().values()){
			data.add(rs.getObject(col.getIdx()+1));// column index from 1
		}
		return data;
	}
	
	/////////////////////////////////////
	
	private void moveResultSetCursor() throws SQLException{
		if(this.query == null)
			return;
		if(this.query.getSession().getDialect().pageMode() != CarpSql.PageSupport.COMPLETE 
				&& this.query.getFirstIndex() != -1){
			if(this.query.getSession().getJdbcContext().getConfig().isEnableScrollableResultSet()){
				this.rs.absolute(this.query.getFirstIndex());
			}else{
				int idx = 0;
				while(rs.next()){
					if(idx++ >=this.query.getFirstIndex())
						break;
				}
			}
		}
	}
}
