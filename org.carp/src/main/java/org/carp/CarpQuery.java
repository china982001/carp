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

import java.io.Closeable;
import java.math.BigDecimal;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.carp.exception.CarpException;
import org.carp.parameter.OUTParameter;

/**
 * Carp query definition interface for defining SQL creation, parameter injection, execution, result set or out parameter acquisition.
 * 查询接口定义类
 * @author zhou
 * @version 0.1
 */
public interface CarpQuery extends Closeable{
	/**
	 * When SQL's select statement is executed, the result set record is converted to a collection of object objects,
	 * and the object collection is returned.
	 * 
	 * @return Object Collection
	 * @throws CarpException
	 */
	List<?> list()throws CarpException ;
	
	/**
	 * When SQL's select statement is executed, the result set record is converted to a DataSet.
	 * @return DataSet
	 * @throws CarpException
	 */
	DataSet dataSet() throws CarpException;
	/**
	 * Review SQL statements to be executed.
	 * @return
	 */
	String getQueryString();
	/**
	 * 查询sql的select列表字段的类型数组
	 * @return
	 */
	Class<?>[] getReturnTypes();
	
	/**
	 * 查询sql的select列表字段的名称数组
	 * @return
	 */
	String[] getReturnNames();
	/**
	 * 直接返回查询sql语句的结果集，返回的结果集数据取决于数据库是否支持分页功能，如果不支持则返回全部数据
	 * @return
	 * @throws CarpException
	 */
	ResultSet resultSet()throws CarpException;
	/**
	 * 设置查询sql的起始索引
	 * @param first
	 * @return
	 */
	CarpQuery setFirstIndex(int first);
	/**
	 * 设置查询的结果集最多可以返回多少条记录。
	 * @param last
	 * @return
	 */
	CarpQuery setMaxCount(int last);
	/**
	 * 设置查询超时
	 * @param timeout
	 * @return
	 */
	CarpQuery setTimeout(int timeout);
	/**
	 * 设置Statement对象的fetch大小
	 * @param fetchSize
	 * @return
	 */
	CarpQuery setFetchSize(int fetchSize);
	/**
	 * 执行sql(update,delete,insert)语句
	 * @return
	 * @throws CarpException
	 */
    int executeUpdate() throws CarpException;
    /**
     * 以批处理方式执行Sql(update,delete,insert)语句
     * @throws CarpException
     */
    void executeBatch()throws CarpException;
    /**
     * 把sql(update,delete,insert)语句，添加到批处理中
     * @return
     * @throws CarpException
     */
    CarpQuery addBatch() throws CarpException;
    /**
     * 清理session中的查询参数
     * @return
     * @throws SQLException
     */
    CarpQuery clearParameters() throws SQLException;

    /**
     * close used PreparedStatement
     */
    void closeStatement();

    /**
     * 
     * @param index
     * @param sqlType
     * @return
     * @throws SQLException
     */
    CarpQuery setNull(int index, int sqlType) throws SQLException;
    /**
     * 
     * @param index
     * @param x
     * @return
     * @throws SQLException
     */
    CarpQuery setBoolean(int index, boolean x) throws SQLException;
    /**
     * 
     * @param index
     * @param x
     * @return
     * @throws SQLException
     */
    CarpQuery setByte(int index, byte x) throws SQLException;
    /**
     * 
     * @param index
     * @param x
     * @return
     * @throws SQLException
     */
    CarpQuery setShort(int index, short x) throws SQLException;
    /**
     * 
     * @param index
     * @param x
     * @return
     * @throws SQLException
     */
    CarpQuery setInt(int index, int x) throws SQLException;
    /**
     * 
     * @param index
     * @param x
     * @return
     * @throws SQLException
     */
    CarpQuery setLong(int index, long x) throws SQLException;
    /**
     * 
     * @param index
     * @param x
     * @return
     * @throws SQLException
     */
    CarpQuery setFloat(int index, float x) throws SQLException;
    CarpQuery setDouble(int index, double x) throws SQLException;
    CarpQuery setBigDecimal(int index, BigDecimal x) throws SQLException;
    CarpQuery setString(int index, String x) throws SQLException;
    CarpQuery setBytes(int index, byte x[]) throws SQLException;
    CarpQuery setDate(int index, Date value) throws SQLException;
    CarpQuery setDate(int index, java.sql.Date value) throws SQLException;
    CarpQuery setTime(int index, Date value)  throws SQLException;
    CarpQuery setTime(int index, java.sql.Time value)  throws SQLException;
    CarpQuery setTimestamp(int index, Date value)  throws SQLException;
    CarpQuery setAsciiStream(int index, java.io.InputStream x)  throws SQLException;
    CarpQuery setAsciiStream(int index, java.io.Reader reader)  throws SQLException;
    CarpQuery setBinaryStream(int index, java.io.InputStream x) throws SQLException;
    CarpQuery setObject(int index, Object x) throws SQLException;
    CarpQuery setCharacterStream(int index, java.io.Reader reader) throws SQLException;
    CarpQuery setRef (int index, Ref x) throws SQLException;
    CarpQuery setURL(int index, java.net.URL x) throws SQLException;
    
    ////////////////      Procedure Call Method Defined           ///////////
    /**
     * Register parameters of type out to get the returned parameter values
     * @param index 
     * @param sqlType 
     * @return
     * @throws SQLException
     */
    CarpQuery registerOutParameter(int index, int sqlType) throws SQLException;
    
    /**
     * After the stored procedure is executed, 
     * the set of out type parameter values is obtained from the parameter index
     * @return
     * @throws Exception
     */
    OUTParameter getOutParameter() throws Exception;
    
    /**
     * Execute a stored procedure that returns the result set collection of stored procedures (there may be no or more than 1 result set)
     * @return
     * @throws Exception
     */
    List<Object> executeProcudere() throws Exception;
}
