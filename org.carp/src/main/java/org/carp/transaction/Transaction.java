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

import java.sql.SQLException;

import org.carp.exception.CarpException;

/**
 * JDBC、JTA事务接口
 * @author zhou
 * @since 0.1
 */
public interface Transaction {
	/**
	 * 开启事务
	 * @throws CarpException
	 */
	public void begin() throws CarpException;
	
	/**
	 * 事务是否开启
	 * @return
	 */
	public boolean isBegin();
	
	/**
	 * 提交事务
	 * @throws CarpException
	 */
	public void commit() throws CarpException;
	
	/**
	 * 回滚事务
	 * @throws CarpException
	 */
	public void rollback() throws  CarpException;
	
	/**
	 * close transaction , should rollback it and close if transaction isn't commited 
	 * @throws CarpException
	 */
	public void close()throws  CarpException;
	
	
	/**
	 * 重置事务隔离级别
	 */
	public void restoreTransactionIsolationLevel() throws SQLException;
	
	/**
	 * 设置事务隔离级别
	 * @param isolationLevel 事务级别
	 */
	public void setTransactionIsolationLevel(int isolationLevel) throws SQLException;
	
	/**
	 * 获取事务隔离级别
	 * @return 事务级别
	 */
	public int getTransactionIsolationLevel() throws SQLException;
}
