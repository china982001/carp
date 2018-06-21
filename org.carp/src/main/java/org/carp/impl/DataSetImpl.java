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
import java.util.List;

import org.carp.DataSet;
import org.carp.engine.metadata.ColumnInfo;
import org.carp.engine.metadata.MetaData;
import org.carp.engine.result.RSProcessor;

/**
 * DataSet接口实现类 
 * @author Administrator
 *
 */
public class DataSetImpl  implements DataSet{
	private List<List<Object>> row = new ArrayList<List<Object>>();
	private int dataCount;
	private int index = -1;
	private String[] columns;
	private Class<?>[] columnsType;
	
	public DataSetImpl(CarpQueryImpl query, MetaData cqmd,ResultSet rs)throws Exception{
		try{
			RSProcessor rsp = new RSProcessor(query,null,cqmd,rs);
			rsp.createDataSet(row);
			dataCount = row.size()-1;
			this.columns = cqmd.getColumnsMap().keySet().toArray(new String[0]);
			columnsType = new Class<?>[cqmd.getColumnsMap().size()];
			for(ColumnInfo col : cqmd.getColumnsMap().values()){
				columnsType[col.getIdx()] = col.getJavaType();
			}
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

	public Object getData(int idx)throws Exception{
		if(index == -1)
			throw new RuntimeException("Execute the next method to determine if there is data.");
		int col_index = this.columns.length;
		if(idx < 0 || idx >=col_index){
			throw new ArrayIndexOutOfBoundsException("Index out of range. Index should be >= 0 and < "+col_index+", but idx = "+idx);
		}
		return row.get(index).get(idx);
	}
	
	public List<List<Object>> getData() {
		return row;
	}
	public String[] getColumns() {
		return 	this.columns;
	}
	public List<Object> getRowData(int index) {
		return row.get(index);
	}

	public Class<?>[] getColumnsType() {
		return this.columnsType;
	}
	
	public int count(){
		return dataCount + 1;
	}
}
