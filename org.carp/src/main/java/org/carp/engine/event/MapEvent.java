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
package org.carp.engine.event;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carp.engine.ParametersProcessor;
import org.carp.engine.statement.CarpStatement;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将Map对象的值保存的对应的数据库表中
 * @author zhou
 * @since 0.1
 */
public class MapEvent{
	private static final Logger logger = LoggerFactory.getLogger(MapEvent.class);
	private static final Map<String,Table> tableMap = new HashMap<String,Table>();
	private CarpSessionImpl session;
	private String table;
	private Map<String,Object> map;
	private Object[] columns;
	private Table tab;
	
	
	private String sql;
	
	class Table{
		private List<String> columns = new ArrayList<String>();
		private List<Integer> types = new ArrayList<Integer>();
		public String getColumn(int index) {
			return columns.get(index);
		}
		public int getColumnIndex(String column){
			return this.columns.indexOf(column);
		}
		public void addColumn(String column) {
			this.columns.add(column);
		}
		public int getType(int index) {
			return types.get(index);
		}
		public void addType(int type) {
			this.types.add(type);
		}
	}
	
	public MapEvent(CarpSessionImpl session,String table, Map<String,Object> map) throws Exception{
		this.session = session;
		this.table = table;
		this.map = map;
		columns = this.map.keySet().toArray();
		tab = initTable();
	}
	
	/**
	 * 提取table元数据
	 * @return
	 * @throws Exception
	 */
	private synchronized Table initTable() throws Exception{
		Table t = tableMap.get(table);
		if(t == null){
			t = initTableColumns(table);
			if(t.columns.isEmpty())
				t = initTableColumns(table.toUpperCase());
			tableMap.put(table, t);
		}
		return t;
	}
	
	private Table initTableColumns(String table)throws Exception{
		Connection conn = this.session.getConnection();
		DatabaseMetaData dmd = conn.getMetaData();
		ResultSet rs = dmd.getColumns(this.session.getJdbcContext().getConfig().getCatalog(),
				this.session.getJdbcContext().getConfig().getSchema(), table, "%");
		Table t = new Table();
		logger.debug("Get Table: {}, ColumnsInfo.",table);
		while(rs.next()){
			String column = rs.getString(4).toUpperCase();
			t.addColumn(column);
			t.addType(rs.getInt(5));
			logger.debug("Column:"+column+" , ColumnType:"+rs.getInt(5));
		}
		return t;
	}
	
	/**
	 * 构建Insert sql语句
	 * @throws CarpException
	 */
	protected void buildSql() throws CarpException{
		StringBuilder builder = new StringBuilder("INSERT INTO ");
		builder.append(table).append(" (");
		String values = "";
		for(int i = 0, count = this.columns.length; i < count; ++i){
			if(i != 0){
				builder.append(",");
				values += ",";
			}
			builder.append(this.columns[i]);
			values += "?";
		}
		builder.append(") VALUES (").append(values).append(")");
		sql = builder.toString();
		displaySql();
	}
	
	/**
	 * 设置Statement需要的参数
	 * @param psProcess
	 * @throws Exception
	 */
	protected void processStatmentParameters() throws Exception{
		ParametersProcessor psp = new ParametersProcessor(this.session.getStatement());
		for(int i = 0, count = this.columns.length; i < count; ++i){
			String col = (String)this.columns[i];
			int type = this.tab.getType(this.tab.columns.indexOf(col.toUpperCase()));
			logger.debug("ParamColumn:{}; ColumnType:{}; ColumnIndex:{}",col,type,i);
			psp.setStatementParameters(this.map.get(col), type, i+1);
		}
	}
	
	/**
	 * 创建需要的Statement对象
	 * @throws Exception
	 */
	private void buildStatement()throws Exception{
		new CarpStatement(this.session).createSessionStatement(sql, Event.INSERT_EVENT_TYPE);
	}
	/**
	 * execute insert operation
	 * @return insert row count
	 * @throws Exception
	 */
	private int executeStatement()throws Exception{
		return this.session.getStatement().executeUpdate();
	}
	
	private void displaySql(){
		logger.debug(sql);
		if(session.getJdbcContext().getConfig().isShowSql()){
			System.out.println("Carp SQL : "+sql);
		}
	}
	
	/**
	 * 对象的事件处理方法
	 * @throws Exception
	 */
	public int execute()throws Exception{
		buildSql();
		buildStatement();
		processStatmentParameters();
		return executeStatement();
	}
}
