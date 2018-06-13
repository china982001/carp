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
import java.util.Set;

import org.carp.engine.metadata.ColumnInfo;
import org.carp.engine.metadata.MetaData;
import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;
import org.carp.sql.CarpSql;
import org.carp.sql.CarpSql.PageSupport;

/**
 * java.sql.ResultSet 结果集记录的对象组装类
 * 把结果集的记录转换封装成对象(class object)、装在list集合中返回
 * @author zhou
 *
 */
public class ResultSetProcessor {
	private CarpQueryImpl query;
	private Class<?> cls;
	private ResultSet rs;
	private MetaData cqmd;
	
	/**
	 * 构造函数
	 * @param query 抽象查询对象
	 * @param cqmd  结果集元数据
	 * @param rs  结果集
	 * @throws CarpException
	 */
	public ResultSetProcessor(CarpQueryImpl query,MetaData cqmd,ResultSet rs) throws CarpException{
		this(query.getCls(),cqmd,rs);
		this.query = query;
	}
	
	/**
	 * 构造函数
	 * @param cls 封装对象类
	 * @param cqmd  结果集元数据
	 * @param rs  结果集
	 * @throws CarpException
	 */
	public ResultSetProcessor(Class<?> cls,MetaData cqmd,ResultSet rs) throws CarpException{
		this.cls = cls;
		this.rs = rs;
		this.cqmd = cqmd;
	}
	
	/**
	 * 取得记录转换成对象的集合
	 * @return 对象集合
	 * @throws Exception
	 */
	public Object get()throws Exception{
		Object o = null;
		while(rs.next()){
			o = processResultSet();
			break;
		}
		return o;
	}
	
	/**
	 * 将返回的结果集记录转换为DataSet对象
	 * @param row
	 * @throws Exception
	 */
	public void createDataSet(List<List<Object>> row)throws Exception{
		PageSupport mode = this.query.getCarpSql().pageMode();
		if(mode == CarpSql.PageSupport.COMPLETE){// 支持分页
			while(rs.next())
				processResultSetToDataSet(row);
		}else{
			scrollResutSetCursor();
			if(mode == CarpSql.PageSupport.PARTIAL)//  部分分页
				while(rs.next())
					processResultSetToDataSet(row);
			else{ // 不支持分页
				int c = 0;
				while(rs.next() && c<this.query.getMaxCount()){
					processResultSetToDataSet(row);
					++c;
				}
			}
		}
	}
	
	/**
	 * 将一条记录存储到list列表中
	 * @param row
	 * @throws Exception
	 */
	private void processResultSetToDataSet(List<List<Object>> row) throws Exception{
		List<Object> data = new ArrayList<Object>();
		Set<String> cols = cqmd.getColumnsMap().keySet();
		for(String col : cols){
			ColumnInfo info = cqmd.getColumnsMap().get(col);
			info.getAssemble().setValue(rs, data,col);
			
		}
		row.add(data);
	}
	
	/**
	 * 取得记录转换成对象的集合
	 * @return 对象集合
	 * @throws Exception
	 */
	public List<Object> list()throws Exception{
		List<Object> list = new ArrayList<Object>();
		PageSupport mode = this.query.getCarpSql().pageMode();
		if(mode == CarpSql.PageSupport.COMPLETE){// mode = 0 支持分页
			while(rs.next())
				list.add(processResultSet());
		}else{
			scrollResutSetCursor();
			if(mode == CarpSql.PageSupport.PARTIAL)// mode = 1 部分分页
				while(rs.next())
					list.add(processResultSet());
			else{ //mode = 2 //不支持分页
				int c = 0;
				while(rs.next() && c<this.query.getMaxCount()){
					list.add(processResultSet());
					++c;
				}
			}
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
		
		for(String col: cqmd.getColumnsMap().keySet())
		{
			if(col.equals("CARP_ROW_NUM"))
				continue;
			ColumnInfo info = cqmd.getColumnsMap().get(col);
			info.getAssemble().setFieldValue(rs, vo, info.getField(),col);
		}
		return vo;
	}
	
	/**
	 * 如果不支持分页和部分支持，结果集为滚动结果集，则移动光标
	 * @throws SQLException
	 */
	private void scrollResutSetCursor() throws SQLException{
		if(this.query.getFirstIndex() != -1){
			if(this.query.getCarpSql().enableScrollableResultSet())
				rs.absolute(this.query.getFirstIndex());
			else{
				int c = 0;
				while(rs.next()){
					if((c++)>=this.query.getFirstIndex())
						break;
				}
			}
		}
	}
}
