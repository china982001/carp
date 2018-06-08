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

import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.exception.CarpException;
import org.carp.jdbc.JDBCContext;

/**
 * JTA事务接口实现类
 * @author zhou
 * @since 0.1
 */
public class JTATransaction implements Transaction {
	private static final Logger logger = LoggerFactory.getLogger(JTATransaction.class);
	private UserTransaction userTransaction;
	private Connection connection;
	private boolean begin = false;
	private boolean failedCommit = false;
	private boolean commmitted = false;
	private boolean newTransaction = false;
	private JDBCContext context;
	private int	srcTransIsolation = -1;
	
	/**
	 * 
	 * @param context
	 * @throws CarpException
	 */
	public JTATransaction(JDBCContext context) throws CarpException {
		InitialContext initCtx;
		try {
			initCtx = new InitialContext();
			userTransaction = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
			connection = context.getConnection();
			context.setConnection(connection);
		} catch (Exception e) {
			throw new CarpException("JtaTransaction initialization failed.  not found JTANaming : java:comp/UserTransaction");
		}
	    if (userTransaction == null) {
	    	throw new CarpException("JtaTransaction initialization failed.  UserTransaction was null.");
	    }
	    if (connection == null) {
	    	throw new CarpException("JtaTransaction initialization failed.  Connection was null.");
	    }
	    this.context = context;
	}

	public boolean isBegin(){
		return this.begin;
	}
	
	/**
	 * 提交事务
	 */
	public void commit() throws CarpException {
		if(begin)
			throw new CarpException("JTATransaction could not commit. Cause: transaction has already been committed.");
		if (connection != null) {
			if (commmitted) {
				throw new CarpException("JTATransaction could not commit. Cause: transaction has already been committed.");
			}
			try {
				if (newTransaction) 
					userTransaction.commit();
			} catch (Exception e) {
				failedCommit = true;
				throw new CarpException("JtaTransaction could not commit.  Cause: ", e);
			}
			commmitted = true;
		}
	}

	/**
	 * 提交事务
	 */
	public void rollback() throws CarpException {
		if (connection != null) {
			if (!commmitted) {
				try {
					if (userTransaction != null) {
						if (newTransaction) {
							userTransaction.rollback();
						} else {
							userTransaction.setRollbackOnly();
						}
					}
				} catch (Exception e) {
					throw new CarpException("JtaTransaction could not rollback.  Cause: ", e);
				}
			}
		}
	}

	/**
	 * 启动事务
	 */
	public void begin() throws  CarpException {
		if(begin)
			return;
		if(failedCommit)
			throw new CarpException("could not re-start transaction.  Cause: failed commit");
		try {
			newTransaction = userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION;
			if (newTransaction) {
				userTransaction.begin();
				if(logger.isDebugEnabled())
					logger.debug("JTATransaction starting...");
			}
		} catch (Exception e) {
			throw new CarpException("JTATransaction could not start transaction.  Cause: ", e);
		}
	}

	public void restoreTransactionIsolationLevel() throws SQLException {
		if(this.srcTransIsolation != -1)
			connection.setTransactionIsolation(this.srcTransIsolation);
	}
	
	public void setTransactionIsolationLevel(int isolationLevel) throws SQLException{
		srcTransIsolation = connection.getTransactionIsolation();//保存原来的事务隔离级别,以便于后来恢复
		int defIsolationLevel = context.getContext().getConfig().getTransIsoLationLevel();
		if(isolationLevel != -1 && isolationLevel != defIsolationLevel)
			connection.setTransactionIsolation(isolationLevel);
		else if(defIsolationLevel !=-1)
			connection.setTransactionIsolation(defIsolationLevel);
	}
	
	public int getTransactionIsolationLevel() throws SQLException{
		return connection.getTransactionIsolation();
	}
	

	@Override
	public void close() throws CarpException {
		throw new CarpException("JTA transaction does not support close.");		
	}
}
