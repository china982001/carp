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
package org.carp.sql;

import java.sql.SQLException;

import org.carp.beans.CarpBean;
import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;

public interface CarpSql {
	/**
	 * 取Schema
	 * @return
	 */
	String getSchema();
	/**
	 * 设置Schema
	 * @param schema
	 */
	void setSchema(String schema);
	/**
	 * 根据Class取得对应的Class Info
	 * @return
	 */
	CarpBean getBeanInfo();
		
	/**
	 * 设置pojo Class
	 * @param cls
	 */
	void setClass(Class<?> cls)throws CarpException;
	
	/**
	 * 根据序列名称，取得序列生成的sql
	 * @param seq
	 * @return
	 */
	String getSequenceSql(String seq) throws CarpException;
	
	/**
	 * 取得执行分页查询时，select查询的分页sql语句 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	String getPageSql(String sql) throws CarpException;
	
	/**
	 * 取得执行分页查询时，select查询的分页sql语句 
	 * @return
	 * @throws Exception
	 */
	String getPageSql() throws CarpException;
	
	/**
	 * 确定绑定查询分页参数位置信息
	 * @throws Exception
	 */
	int position();
	
	/**
	 * COMPLETE  完全支持 ,返回满足要求的记录
	 * PARTIAL  部分支持，返回前N条记录，需要使用滚动结果集，过滤掉前N-m条，仅仅使用后面的m条记录
	 * NONE  不支持，返回全部记录，使用滚动结果集，定位到n-m条记录后，再仅仅确定m条记录即可。
	 */
	enum PageSupport{COMPLETE,PARTIAL,NONE};
	
	/**
	 * 分页模式
	 * 用于确定数据库是否支持分页，如果不支持，则利用jdbc的滚动结果集，查询所需的记录<br/>
	 * 0:支持分页,返回满足要求的记录
	 * 1:部分支持分页，返回前N条记录，需要使用滚动结果集，过滤掉前N-m条，仅仅使用后面的m条记录
	 * 2:不支持分页，返回全部记录，使用滚动结果集，定位到n-m条记录后，再仅仅确定m条记录即可。
	 * @return
	 */
	PageSupport pageMode();

	/**
	 * 是否支持滚动结果集
	 * @return true/false 支持/不支持
	 */
	boolean enableScrollableResultSet();
	/**
	 * 根据Class，取得class对应的数据表的查询sql（不带有分页功能）
	 * @return
	 * @throws CarpException 
	 */
	String getQuerySql() throws CarpException;
	
	/**
	 * 根据主键，取得单个对象的sql
	 * @return
	 */
	String getLoadSql()throws CarpException;
	
	/**
	 * 取得执行insert操作时的insert sql语句
	 * @return
	 * @throws CarpException 
	 */
	public String getInsertSql() throws CarpException;
	
	/**
	 * 取得执行update时候update sql语句
	 * @return
	 * @throws CarpException 
	 */
	public String getUpdateSql() throws CarpException;
	
	/**
	 * 取得执行delete操作时的delete sql语句
	 * @return
	 * @throws CarpException 
	 */
	public String getDeleteSql() throws CarpException;
	
	/**
	 * 设置查询参数
	 * 因为不同的数据库，分页方式不同，有的分页索引在前，有的在后面，所以需要知道该sql的参数个数（除分页索引外）。
	 * 从query对象中获取下列参数：
	 * java.sql.PreparedStatement 
	 * firstIndex, 起始索引
	 * maxIndex, 最大记录数
	 * paramsCount 查询参数个数
	 * @param query
	 */
	public void setQueryParameters(CarpQueryImpl query)throws SQLException;
}
