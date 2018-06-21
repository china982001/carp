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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import org.carp.CarpQuery;
import org.carp.CarpSession;
import org.carp.engine.event.Event;
import org.carp.engine.event.FindEvent;
import org.carp.engine.event.MapEvent;
import org.carp.engine.event.SaveEvent;
import org.carp.engine.event.UpdateEvent;
import org.carp.exception.CarpException;
import org.carp.factory.CarpEventFactory;
import org.carp.intercept.Interceptor;
import org.carp.jdbc.JDBCContext;
import org.carp.jdbc.provider.ConnectionProvider;
import org.carp.script.SQL;
import org.carp.sql.CarpSql;
import org.carp.transaction.Transaction;

/**
 * 数据库连接会话类
 * @author Administrator
 *
 */
public class CarpSessionImpl implements CarpSession{
	private JDBCContext jdbcContext;
	private PreparedStatement statement = null;
	private String sql;
	private Transaction tx ;
	private Interceptor interceptor;
	
	/**
	 * 
	 * @param provider
	 * @throws CarpException
	 */
	public CarpSessionImpl(ConnectionProvider provider) throws CarpException{
		this(provider,null);
	}
	
	/**
	 * 
	 * @param provider
	 * @param interceptor
	 * @throws CarpException
	 */
	public CarpSessionImpl(ConnectionProvider provider,Interceptor interceptor) throws CarpException{
		this.jdbcContext = new JDBCContext(provider);
		this.interceptor = interceptor;
	}
	
	/**
	 * 获取连接
	 */
	public Connection getConnection() throws CarpException{
		try {
			return this.jdbcContext.getConnection();
		} catch (Exception e) {
			throw new CarpException("不能取得数据库连接!",e);
		}
	}

	/**
	 * 根据一个对象删除一条记录
	 * @param obj
	 * @return
	 * @throws CarpException
	 */
	public int delete(Object obj) throws CarpException {
		return this.conDelete(null, obj);
	}
	
	/**
	 * @param cls
	 * @param id
	 * @return
	 * @throws CarpException
	 */
	public int delete(Class<?> cls, Serializable id) throws CarpException{
		Object obj = id;
		return this.conDelete(cls, obj);
	}
	
	/**
	 * 
	 * @param cls
	 * @param key
	 * @return
	 * @throws CarpException
	 */
	public int delete(Class<?> cls, Map<String, Object> key) throws CarpException{
		Object obj = key;
		return this.conDelete(cls, obj);
	}
	
	private int conDelete(Class<?> cls, Object obj) throws CarpException{
		if(!isOpen())
			throw new CarpException("Connection could not used！could not execute delete");
		try{
			CarpEventFactory.deleteEvent(this, cls, obj).execute();
//			Event event = CarpEventFactory.createDeleteEvent(this,cls, obj);
//			event.execute();
		}catch(Exception ex){
			throw new CarpException("delete failed."+obj,ex);
		}
		return 0;
	}

	public Object get(Class<?> cls, Serializable id)throws CarpException {
		Object key = id;
		return this.conGet(cls, key);
	}
	
	public Object get(Class<?> cls, Map<String, Object> key)throws CarpException {
		Object values = key;
		return this.conGet(cls, values);
	}
	
	private Object conGet(Class<?> cls, Object key)throws CarpException {
		try{
			Event event = new FindEvent(this,cls,key);
			event.execute();
			return event.getEntity();//new CarpLoadProcessor(this,cls,key).get();
		}catch(Exception ex){
			throw new CarpException(ex);
		}
	}

	public Transaction beginTransaction() throws CarpException {
		try {
			tx = this.jdbcContext.getTransaction();
			tx.begin();
			return tx;
		} catch (Exception e) {
			throw new CarpException(e);
		}
	}

	public boolean isOpen() {
		return !this.jdbcContext.isClose();
	}
	
	public Serializable save(Object obj)throws CarpException {
		if(!isOpen())
			throw new CarpException("connection was closed,could not execute save!");
		try{ 
			Event event = new SaveEvent(this,obj);
			event.execute();
			return event.getPrimaryValue();
		}catch(Exception ex){
			throw new CarpException("The serialization of data to the database failed. Cause:"+ex,ex);
		}
	}
	
	public Serializable[] save(Object[] objs)throws CarpException {
		if(!isOpen())
			throw new CarpException("connection was closed,could not execute save!");
		try{
			Serializable[] ids = new Serializable[objs.length];
			for(int i=0,count=objs.length; i<=count; ++i){
				Event event = new SaveEvent(this,objs[i]);
				event.execute();
				ids[i] = event.getPrimaryValue();
			}
			return ids;
		}catch(Exception ex){
			throw new CarpException("The serialization of data to the database failed. Cause:"+ex,ex);
		}
	}
	
	
	
	public void save(String table, Map<String,Object> map)throws CarpException {
		if(!isOpen())
			throw new CarpException("connection was closed,could not execute save!");
		try{
			new MapEvent(this,table,map).execute();
//			return null;
		}catch(Exception ex){
			throw new CarpException("The serialization of data to the database failed. Cause:"+ex,ex);
		}
	}

	public void update(Object obj) throws CarpException {
		if(!isOpen())
			throw new CarpException("Connection could not used！could not execute udate");
		try{
			new UpdateEvent(this,obj).execute();
		}catch(Exception ex){
			throw new CarpException("update failed."+obj,ex);
		}
	}
	
	public void close() throws CarpException {
		try{
			if(this.jdbcContext.isClose())
				throw new CarpException("Session closed!");
			rollbackTransaction();
			if(statement!=null)
				statement.close();
			statement = null;
			this.jdbcContext.close();
			this.jdbcContext = null;
		}catch(Exception ex){
			throw new CarpException("close sesssion failed！",ex);
		}
	}
	
	private void rollbackTransaction() throws CarpException{
		if(tx != null && tx.isBegin()){
			tx.rollback();
		}
	}

	@Override
	public CarpQuery createQuery(Class<?> clazz) throws CarpException {
		return this.conCreateQuery(null,null,clazz);
	}

	@Override
	public CarpQuery createQuery(String sql, Class<?>... clzes) throws CarpException {
		return this.conCreateQuery(sql,null,clzes);
	}

	@Override
	public CarpQuery createQuery(SQL sql, Class<?>... clzes) throws CarpException {
		return this.conCreateQuery(null,sql,clzes);
	}
	
	private CarpQueryImpl conCreateQuery(String sql, SQL _dynamicSql, Class<?>... clazzes)throws CarpException{
		if(!isOpen())
			throw new CarpException("Unable to open database connection, cannot create CarpQuery object");
		return new CarpQueryImpl(this,sql,_dynamicSql,clazzes);
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public PreparedStatement getStatement() {
		return statement;
	}

	public void setStatement(PreparedStatement ps) {
		this.statement = ps;
	}



	public JDBCContext getJdbcContext() {
		return jdbcContext;
	}



	public Interceptor getInterceptor() {
		return interceptor;
	}

	public CarpSql getDialect(){
		return this.jdbcContext.getContext().getDialect();
	}
	public Class<? extends CarpSql> dialectClass(){
		return this.jdbcContext.getContext().dialectClass();
	}

}
