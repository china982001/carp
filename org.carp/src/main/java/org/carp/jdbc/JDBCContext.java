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
package org.carp.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.carp.cfg.CarpSetting;
import org.carp.jdbc.provider.ConnectionProvider;
import org.carp.transaction.JDBCTransaction;
import org.carp.transaction.JTATransaction;
import org.carp.transaction.Transaction;
/**
 * JDBC连接类
 * 负责连接数据库，关闭数据库连接
 * 取得事务对象
 * @author zhou
 * @since 0.1
 */
public class JDBCContext {
	private Connection conn;
	private ConnectionProvider context;
	private boolean isClose = false;
	public JDBCContext(ConnectionProvider context){
		this.context = context;
	}
	
	/**
	 * 取得数据库连接
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException  {
		if(conn !=null && !conn.isClosed())
			return conn;
		conn = context.getDataSource().getConnection();
		return conn;
	}
	
	public void setConnection(Connection conn){
		this.conn = conn;
	}
	
	/**
	 * 关闭数据库连接
	 * @throws SQLException
	 */
	public void close() throws SQLException{
		if(conn != null && !conn.isClosed())
			try{
//				conn.rollback();
//				conn.setAutoCommit(true);
				conn.close();				
			}catch(Exception e){throw new SQLException("关闭连接失败！");}
		conn = null;
		this.isClose = true;
	}
	
	public boolean isClose(){
		return this.isClose;
	}
	
	/**
	 * 获取事务
	 * @return
	 * @throws Exception
	 */
	public Transaction getTransaction() throws Exception{
		if(this.context.getConfig().getCarpTransaction().equalsIgnoreCase("jdbc"))
			return new JDBCTransaction(this);
		else
			return new JTATransaction(this);
	}
	
	/**
	 * 取得上下文
	 * @return
	 */
	public ConnectionProvider getContext() {
		return context;
	}
	
	/**
	 * Retrieve Carp Configuration information
	 * @return
	 */
	public CarpSetting getConfig(){
		return this.context.getConfig();
	}
}
