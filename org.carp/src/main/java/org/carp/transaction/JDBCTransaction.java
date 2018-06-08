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
package org.carp.transaction;


import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.exception.CarpException;
import org.carp.jdbc.JDBCContext;

/**
 * JDBC事务接口实现类
 * @author zhou
 * @since 0.1
 */
public class JDBCTransaction implements Transaction {

	private static final Logger logger = LoggerFactory.getLogger(JDBCTransaction.class);
	private Connection conn;
	private boolean begin = false;
	private JDBCContext context;
	private int	srcTransIsolation = -1;
	
	/**
	 * 设置事务隔离级别
	 * @param context
	 * @throws Exception
	 */
	public JDBCTransaction(JDBCContext context) throws Exception {
		conn = context.getConnection();
		this.context = context;
	}

	/**
	 * 提交事务
	 */
	public void commit() throws CarpException {
		if(!begin)
			throw new CarpException("Transaction could not commited! cause: Transaction not started");
		if(conn == null)
			throw new CarpException("Transaction could not commited! cause: connection is null");
		try{
			conn.commit();
			if(logger.isDebugEnabled())
				logger.debug("Transaction commit！");
		}catch(Exception ex){
			throw new CarpException("事务提交失败！",ex);
		}
	}
	
	public boolean isBegin(){
		return this.begin;
	}
	
	public void close()throws CarpException{
		try {
			this.conn.rollback();
			this.conn.setAutoCommit(true);
			this.begin = false;
		} catch (SQLException e) {
			throw new CarpException("Close transaction failed. Cause:"+e,e);
		}
	}
	
	/**
	 * 回滚事务
	 */
	public void rollback() throws CarpException {
		if(!begin)
			throw new CarpException("Transaction could not rollback! cause: Transaction not started");
		if(conn == null)
			throw new CarpException("Transaction could not rollback! cause: connection is null");
		try{
			conn.rollback();
			if(logger.isDebugEnabled())
				logger.debug("Transaction rollback!");
		}catch(Exception ex){
			throw new CarpException("事务回滚失败！",ex);
		}
	}

	/**
	 * 启动事务
	 */
	public void begin() throws CarpException {
		try {
			if(begin)
				return;
		    if (conn == null)
		    	throw new CarpException("Transaction could not started. Cause:  connection is null");
		    // AutoCommit
		    if (conn.getAutoCommit()) {
		    	conn.setAutoCommit(false);
		    }
		    begin = true;
		    if(logger.isDebugEnabled())
		    	logger.debug("Transaction begin......");
		}catch(Exception ex){
			throw new CarpException("Transaction start fail！",ex);
		}
	}
	public void restoreTransactionIsolationLevel()  throws SQLException{
		if(this.srcTransIsolation != -1)
			conn.setTransactionIsolation(this.srcTransIsolation);
	}
	
	public void setTransactionIsolationLevel(int isolationLevel) throws SQLException {
		srcTransIsolation = conn.getTransactionIsolation();//保存原来的事务隔离级别,以便于后来恢复
		int defIsolationLevel = context.getContext().getConfig().getTransIsoLationLevel();
		if(isolationLevel != -1 && isolationLevel != defIsolationLevel)
			conn.setTransactionIsolation(isolationLevel);
		else if(defIsolationLevel !=-1)
			conn.setTransactionIsolation(defIsolationLevel);
	}
	
	public int getTransactionIsolationLevel()  throws SQLException{
		return conn.getTransactionIsolation();
	}
}
