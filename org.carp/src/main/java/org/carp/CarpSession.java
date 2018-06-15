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
package org.carp;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;

import org.carp.exception.CarpException;
import org.carp.script.SQL;
import org.carp.transaction.Transaction;
/**
 * 数据库连接会话类
 * @author zhou
 * @version 0.1
 */
public interface CarpSession extends AutoCloseable{
	/**
	 * 持久化对象到数据库
	 * @param obj 需要持久化的对象
	 * @return 持久化对象的主键值
	 * @throws CarpException
	 */
	Serializable save(Object obj)throws CarpException;
	/**
	 * 持久化对象到数据库
	 * @param objs 需要持久化的对象集合
	 * @return 持久化对象的主键集合
	 * @throws CarpException
	 */
	Serializable[] save(Object[] objs)throws CarpException;
	/**
	 * 持久化数据到数据库
	 * @param table 需要insert数据的数据表
	 * @param map 列与列值的Map对象（列值对）,列为table的列名，如果不存在，则抛出异常
	 * @throws CarpException
	 */
	void save(String table,Map<String,Object> map)throws CarpException;
	/**
	 * 删除对象
	 * @param obj 对象
	 * @throws CarpException
	 */
	int delete(Object obj)throws CarpException ;
	/**
	 * 删除对象
	 * @param cls 数据表对应的pojo类
	 * @param id 主键值
	 * @throws CarpException
	 */
	int delete(Class<?> cls,java.io.Serializable id)throws CarpException;
	/**
	 * 删除对象
	 * @param cls 数据表对于的pojo类
	 * @param ids 联合主键值
	 * @throws CarpException
	 */
	int delete(Class<?> cls,Map<String,Object> ids)throws CarpException;
	/**
	 * 更新一个对象
	 * @param obj 对象
	 * @throws CarpException
	 */
	void update(Object obj)throws CarpException;
	/**
	 * 根据标识查找一个对象
	 * @param cls 对象类
	 * @param id 主键值
	 * @return cls对象
	 * @throws CarpException
	 */
	Object get(Class<?> cls,java.io.Serializable id)throws CarpException;
	
	/**
	 * 根据key查找一个cls类型的对象。
	 * @param cls 对象类
	 * @param key 主键值，map
	 * @return cls对象
	 * @throws CarpException
	 */
	Object get(Class<?> cls, Map<String, Object> key)throws CarpException;
	/**
	 * 创建数据集查询对象，
	 * @param sql 查询sql
	 * @return 数据集查询对象
	 * @throws CarpException
	 */
	CarpQuery creatDataSetQuery(String sql)throws CarpException;
	/**
	 * 创建数据集查询对象，
	 * @param sql 查询sql
	 * @return 数据集查询对象
	 * @throws CarpException
	 */
	CarpQuery creatDataSetQuery(SQL sql)throws CarpException;
	/**
	 * 创建可执行查询对象
	 * @param sql 可执行sql(delete，update，insert)
	 * @return 可执行查询对象
	 * @throws CarpException
	 */
	CarpQuery creatUpdateQuery(String sql)throws CarpException;
	/**
	 * 创建可执行查询对象
	 * @param sql 可执行sql(delete，update，insert)
	 * @return 可执行查询对象
	 * @throws CarpException
	 */
	CarpQuery creatUpdateQuery(SQL sql)throws CarpException;
	/**
	 * 创建查询对象
	 * @param cls 查询类
	 * @return 查询对象
	 * @throws CarpException
	 */
	CarpQuery creatQuery(Class<?> cls)throws CarpException;
	/**
	 * 创建查询对象
	 * @param cls 查询类
	 * @param sql 查询sql
	 * @return 查询对象
	 * @throws CarpException
	 */
	CarpQuery creatQuery(Class<?> cls,String sql)throws CarpException;
	//void flush() throws CarpException;
	/**
	 * 创建查询对象
	 * @param cls 查询类
	 * @param sql 查询sql
	 * @return 查询对象
	 * @throws CarpException
	 */
	CarpQuery creatQuery(Class<?> cls,SQL sql)throws CarpException;
	
	/**
	 * Create stored procedure query objects to execute stored procedures.
	 * If a stored procedure has a return result set and the returned result set can be converted to an object collection, 
	 * you need to pass in the class of object to support multiple result sets, Each result set corresponds to a class. 
	 * Make sure that the field name in the class matches the field name in the result set. 
	 * Carp will automatically encapsulate each result set into an object collection.
	 * 
	 * @param sql  stored procedure
	 * @param classes  Object's class
	 * @return CarpQuery object
	 * @throws CarpException
	 */
	CarpQuery createProcedureQuery(String sql,Class<?>... classes)throws CarpException;
	
	/**
	 * 启动事务
	 */
	Transaction beginTransaction() throws CarpException;
	/**
	 * 判断数据库连接是否打开
	 * @return
	 */
	boolean isOpen();
	/**
	 * 关闭会话
	 * @throws CarpException
	 */
	void close()throws CarpException;
	/**
	 * 从会话中取得数据库连接
	 * @return
	 * @throws CarpException
	 */
	Connection getConnection() throws CarpException;
}
