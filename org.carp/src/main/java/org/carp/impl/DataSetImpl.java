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
package org.carp.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.carp.DataSet;
import org.carp.engine.metadata.ColumnInfo;
import org.carp.engine.metadata.MetaData;
import org.carp.engine.result.ResultSetProcessor;

/**
 * DataSet接口实现类 
 * @author Administrator
 *
 */
public class DataSetImpl  implements DataSet{
	private List<List<Object>> row = new ArrayList<List<Object>>();
	private int dataCount;
	private MetaData cqmd;
	private int index = -1;
	
	public DataSetImpl(CarpQueryImpl query, MetaData cqmd,ResultSet rs)throws Exception{
		try{
			ResultSetProcessor rsp = new ResultSetProcessor(query,cqmd,rs);
			rsp.createDataSet(row);
			this.cqmd = cqmd;
			dataCount = row.size()-1;
		}finally{
			rs.close();
		}
	}
	
	
	public boolean next(){
		if(index < dataCount){
			++index;
			return true;
		}
		return false;
	}
	
	public Object getData(String name){
		int col_index = cqmd.getColumnsMap().get(name.toUpperCase()).getIdx();
		if(col_index == -1){
			System.out.println("字段不存在："+name);
		}
		return row.get(index).get(col_index);
	}
	public Object getData(int idx){
		int col_index = cqmd.getColumnsMap().size();
		if(idx < 0 || idx >=col_index){
			System.out.println("索引超出范围："+idx);
		}
		return row.get(index).get(idx);
	}
	
	public List<List<Object>> getData() {
		return row;
	}
	public List<String> getTitle() {
		List<String> list = new ArrayList<String>();
		list.addAll(cqmd.getColumnsMap().keySet());
		return 	list;
	}
	public List<Object> getRowData(int index) {
		return row.get(index);
	}

	public List<Class<?>> getColumnType() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Collection<ColumnInfo> colls = cqmd.getColumnsMap().values();
		for(ColumnInfo col : colls){
			list.add(col.getJavaType());
		}
		return list;
	}
	
	public int count(){
		return dataCount + 1;
	}
}
