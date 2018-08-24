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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.carp.CarpQuery;
import org.carp.DataSet;
import org.carp.engine.exec.BatchExecutor;
import org.carp.engine.exec.ClassQueryExecutor;
import org.carp.engine.exec.DatasetQueryExecutor;
import org.carp.engine.exec.Executor;
import org.carp.engine.exec.ProcedureExecutor;
import org.carp.engine.exec.QueryExecutor;
import org.carp.engine.exec.UpdateExecutor;
import org.carp.exception.CarpException;
import org.carp.parameter.OUTParameter;
import org.carp.parameter.Parameter;
import org.carp.script.SQL;

/**
 * 查询类，执行查询操作
 * @author zhou
 *
 */
public class CarpQueryImpl implements CarpQuery,Closeable{
	private CarpSessionImpl session;//连接session
	private String	          sql;		//需执行的sql语句
	private Executor		  executor = null; // sql语句执行器
	private PreparedStatement	statement;		//执行sql的java.sql.PreparedStatement的实例
	private int	              timeout = 0; //连接超时
	private String[]	      returnNames; //执行select语句，返回的select 字段列表
	private Class<?>[]	      returnTypes; //执行select语句，
	private Class<?>[]	      clazzes; //store procedure resultset to class array(maybe has more resultset) 
	private Parameter	      param	     = new Parameter();
	private OUTParameter  outParameter = new OUTParameter();
	private int	              fetchSize	 = 20;
	private int	              firstIndex	= -1;
	private int	              maxCount	 = -1;
	
	/**
	 * 
	 * @param session
	 * @param sql
	 * @param _dynamicSql
	 * @param classes
	 * @throws CarpException
	 */
	public CarpQueryImpl(CarpSessionImpl session, String sql, SQL _dynamicSql, Class<?>... classes) throws CarpException {
		try {
			this.session = session;
			this.sql = sql;
			this.clazzes = classes;
			if(_dynamicSql != null){
				this.sql = _dynamicSql.getSql();
				_dynamicSql.processParameters(this);
			}
		} catch (Exception ex) {
			throw new CarpException("数据库连接为空或者Sql语句错误，请检查！", ex);
		}
	}
	
	/**
	 * 执行查询
	 * @see CarpQuery
	 */
	public List<?> list() throws CarpException {
		List<Object> list = null;
		if(this.getClazzes() == null || this.getClazzes().length == 0)
			throw new CarpException("Pojo class could not be null，but it is null! ");
		try {
			ClassQueryExecutor executor = new ClassQueryExecutor(this);
			list = executor.list();
		} catch (Exception ex) {
			throw new CarpException("Query error,may sql or parameters incorrect.", ex);
		}finally{
			if(statement != null)try{statement.close();}catch(Exception e){}
		}
		return list;
	}

	public DataSet dataSet() throws CarpException {
		try {
			DatasetQueryExecutor executor = new DatasetQueryExecutor(this);
			return executor.dataSet();
		} catch (Exception ex) {
			throw new CarpException("Query failure. SQL statement or parameter may be incorrect.", ex);
		}finally{
			if(statement != null)try{statement.close();}catch(Exception e){}
		}
	}
	
	public ResultSet resultSet()throws CarpException{
		try {
			QueryExecutor executor = new QueryExecutor(this);
			ResultSet rs = executor.getResultSet();
			return rs;
		} catch (Exception ex) {
			throw new CarpException(ex);
		}
	}
	
	@Override
	public List<Object> executeProcudere() throws Exception {
		ProcedureExecutor pe = new ProcedureExecutor(this);
		pe.executeProcedure();
		return pe.getRSObjectList();
	}
	
	
	public CarpQuery addBatch() throws CarpException {
		try{
			if(this.executor == null)
				this.executor = new BatchExecutor(this);
			((BatchExecutor)this.executor).addBatch();
		}catch(Exception ex){
			throw new CarpException(ex); 
		}
		return this;
	}

	public void executeBatch()throws CarpException{
		try {
			this.statement.executeBatch();
		} catch (SQLException ex) {
			if(ex.getNextException()!=null)
				ex.getNextException().printStackTrace();
			throw new CarpException(ex); 
		}
	}
	
	public CarpQuery clearParameters(){
		this.param.clear();
		return this;
	}

	public int executeUpdate() throws CarpException {
		int code = -1;
		if(this.executor != null)// batch模式，无需执行executeUpdate方法.
			return 0;
		if(!this.session.isOpen())
			throw new CarpException("connection was closed,could not execute!");
		try{
			code = new UpdateExecutor(this).getRowCount();
		}catch(Exception ex){
			throw new CarpException("Query failure. SQL statement or parameter may be incorrect.",ex);
		}finally{
			if(statement != null)try{statement.close();}catch(Exception e){}
		}
		return code;
	}
	
	public void closeStatement(){
		this.outParameter.clear();
		this.outParameter = null;
		this.param.clear();
		this.param = null;
		this.session = null;
		this.sql = null;
		this.executor = null;
		try{if(statement != null && !statement.isClosed())statement.close();statement = null;}catch(Exception e){}
	}

	public String getQueryString() {
		return this.sql;
	}

	public String[] getReturnNames() {
		return this.returnNames;
	}

	public Class<?>[] getReturnTypes() {
		return this.returnTypes;
	}

	public CarpQuery setAsciiStream(int index, InputStream x) throws SQLException {
		param.setParameter(index, x, InputStream.class);
		return this;
	}
	
	public CarpQuery setAsciiStream(int index, Reader x) throws SQLException {
		param.setParameter(index, x, Reader.class);
		return this;
	}

	public CarpQuery setFirstIndex(int beginIndex) {
		this.firstIndex = beginIndex;
		return this;
	}

	public CarpQuery setBigDecimal(int index, BigDecimal x) throws SQLException {
		param.setParameter(index, x, BigDecimal.class);
		return this;
	}


	public CarpQuery setBinaryStream(int index, InputStream x) throws SQLException {
		param.setParameter(index, x, InputStream.class);
		return this;
	}

	public CarpQuery setBoolean(int index, boolean x) throws SQLException {
		param.setParameter(index, x, boolean.class);
		return this;
	}

	public CarpQuery setByte(int index, byte x) throws SQLException {
		param.setParameter(index, x, byte.class);
		return this;
	}

	public CarpQuery setBytes(int index, byte[] x) throws SQLException {
		param.setParameter(index, x, byte[].class);
		return this;
	}

	public CarpQuery setCharacterStream(int index, Reader reader) throws SQLException {
		param.setParameter(index, reader, Reader.class);
		return this;
	}

	public CarpQuery setDate(int index, java.util.Date value) throws SQLException {
		param.setParameter(index, value, java.util.Date.class);
		return this;
	}
	public CarpQuery setDate(int index, java.sql.Date value) throws SQLException {
		param.setParameter(index, value, java.sql.Date.class);
		return this;
	}

	public CarpQuery setDouble(int index, double x) throws SQLException {
		param.setParameter(index, x, double.class);
		return this;
	}

	public CarpQuery setMaxCount(int endIndex) {
		this.maxCount = endIndex;
		return this;
	}

	public CarpQuery setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
		return this;
	}

	public CarpQuery setFloat(int index, float x) throws SQLException {
		param.setParameter(index, x, float.class);
		return this;
	}

	public CarpQuery setInt(int index, int x) throws SQLException {
		param.setParameter(index, x, int.class);
		return this;
	}

	public CarpQuery setLong(int index, long x) throws SQLException {
		param.setParameter(index, x, long.class);
		return this;
	}

	public CarpQuery setNull(int index, int sqlType) throws SQLException {
		param.setParameter(index, sqlType, null);
		return this;
	}

	public CarpQuery setObject(int index, Object x) throws SQLException {
		param.setParameter(index, x, Object.class);
		return this;
	}

	public CarpQuery setRef(int index, Ref x) throws SQLException {
		param.setParameter(index, x, Ref.class);
		return this;
	}

	public CarpQuery setShort(int index, short x) throws SQLException {
		param.setParameter(index, x, short.class);
		return this;
	}

	public CarpQuery setString(int index, String x) throws SQLException {
		param.setParameter(index, x, String.class);
		return this;
	}

	public CarpQuery setTime(int index, java.util.Date value) throws SQLException {
		java.sql.Time ts = null;
		if (value != null)
			ts = new java.sql.Time(value.getTime());
		param.setParameter(index, ts, java.sql.Time.class);
		return this;
	}
	public CarpQuery setTime(int index, java.sql.Time value) throws SQLException {
		param.setParameter(index, value, java.sql.Time.class);
		return this;
	}

	public CarpQuery setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public CarpQuery setTimestamp(int index, java.util.Date value) throws SQLException {
		java.sql.Timestamp ts = null;
		if (value != null)
			ts = new java.sql.Timestamp(value.getTime());
		param.setParameter(index, ts, java.sql.Timestamp.class);
		return this;
	}

	public CarpQuery setURL(int index, URL x) throws SQLException {
		param.setParameter(index, x, URL.class);
		return this;
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

	public int getTimeout() {
    	return timeout;
    }

	public Parameter getParameters() {
    	return param;
    }

	public int getFetchSize() {
    	return fetchSize;
    }

	public int getFirstIndex() {
    	return firstIndex;
    }

	public int getMaxCount() {
    	return maxCount;
    }

	public void setStatement(PreparedStatement ps) {
    	this.statement = ps;
    }

	public CarpSessionImpl getSession() {
		return session;
	}

	public void setReturnNames(String[] returnNames) {
		this.returnNames = returnNames;
	}

	public void setReturnTypes(Class<?>[] returnTypes) {
		this.returnTypes = returnTypes;
	}

	@Override
	public CarpQuery registerOutParameter(int index, int sqlType) throws SQLException {
		param.setOutParameter(index, sqlType);
		return this;
	}
	
	public Class<?>[] getClazzes(){
		return this.clazzes;
	}
	@Override
	public OUTParameter getOutParameter(){
		return this.outParameter;
	}

	@Override
	public void close() throws IOException {
		this.closeStatement();
	}
}
