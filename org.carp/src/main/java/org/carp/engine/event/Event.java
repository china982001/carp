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

import java.sql.Blob;
import java.sql.Clob;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.beans.CarpBean;
import org.carp.beans.ColumnsMetadata;
import org.carp.beans.PrimarysMetadata;
import org.carp.engine.ParametersProcessor;
import org.carp.engine.helper.SessionSqlHelper;
import org.carp.engine.helper.SqlHelper;
import org.carp.engine.statement.CarpStatement;
import org.carp.exception.CarpException;
import org.carp.factory.BeansFactory;
import org.carp.impl.CarpSessionImpl;
import org.carp.sql.OracleCarpSql;

/**
 * 数据操作基本事件类
 * @author zhou
 * @since 0.2
 */
public abstract class Event{
	private static final Logger logger = LoggerFactory.getLogger(Event.class);
	private CarpSessionImpl session;
	private Object entity; //实体对象
	private String sql; //sql语句
	private int index = 0;  //sql语句的参数索引
	private CarpBean bean;  //实体类对应的Bean对象
	private String optType;
	
	public Event(CarpSessionImpl session,Object entity,String _optType) throws CarpException{
		this.session = session;
		this.entity = entity;
		bean = BeansFactory.getBean(this.entity.getClass());
		this.optType = _optType;
	}
	
	public Event(CarpSessionImpl session,Class<?> cls,Object key,String _optType) throws CarpException{
		this.session = session;
		bean = BeansFactory.getBean(cls);
		this.optType = _optType;
	}
	
	/**
	 * 取得实体对象的主键值
	 * @return
	 */
	public java.io.Serializable getPrimaryValue(){
		return null;
	}
	/**
	 * 构建Insert/update/delete/find sql语句
	 * @throws CarpException
	 */
	protected void buildSql() throws Exception{
		Class<?> cls = this.entity != null ? this.entity.getClass():bean.getTableClass();
		SqlHelper helper = new SessionSqlHelper(this.session,optType,cls);
		sql = helper.buildSql();
		helper.showSql();
	}
	
	/**
	 * 执行Update操作
	 * @throws Exception
	 */
	private void executeStatement()throws Exception{
		this.session.getPs().executeUpdate();
	}
	
	/**
	 * 对象的事件处理方法
	 * @throws Exception
	 */
	public void execute()throws Exception{
		validate();
		cascadeBeforeOperator(); //statement操作前的级联处理
		buildSql(); //生成sql语句
		executeBefore(); 
		//buildStatement(); //创建statement对象，
		new CarpStatement(this.getSession()).createSessionStatement(sql);
		processStatmentParameters(new ParametersProcessor(this.session.getPs())); //处理statement参数
		executeStatement(); //执行statement
		executeAfter();
		cascadeAfterOperator(); //statement操作后的级联处理
	}
	
	/**
	 * 校验参数的有效性
	 * @param psProcess
	 * @throws Exception
	 */
	protected abstract boolean validate() throws Exception;
	
	/**
	 * 设置Statement需要的参数
	 * @param psProcess
	 * @throws Exception
	 */
	protected abstract void processStatmentParameters(ParametersProcessor psProcess) throws Exception;
	
	/**
	 * statement操作前的级联处理
	 * @throws Exception
	 */
	protected abstract void cascadeBeforeOperator() throws Exception;
	
	/**
	 * statement操作后的级联处理
	 * @throws Exception
	 */
	protected abstract void cascadeAfterOperator() throws Exception;
	/**
	 * 执行前的处理
	 * @throws Exception
	 */
	protected abstract void executeBefore() throws Exception;
	/**
	 * 执行后的处理
	 * @throws Exception
	 */
	protected abstract void executeAfter() throws Exception;

	/**
	 * 处理实体的column的值
	 * @param psProcess
	 * @throws Exception
	 */
	protected void processFieldValues(ParametersProcessor psProcess) throws Exception{
		List<ColumnsMetadata> columns = bean.getColumns();
		for(int i = 0, count = columns.size(); i < count; ++i){
			ColumnsMetadata column = columns.get(i);
			Class<?> ft = column.getFieldType();
			Object value = column.getValue(entity);
			int _index = this.getNextIndex();
			if(logger.isDebugEnabled()){
				logger.debug("参数索引："+ _index +" , ColumnName:"+column.getColName()+" , FieldName:"+column.getFieldName()+",FieldType:"+column.getFieldType().getName()+",FieldValue: "+value);
				if(value != null)
					logger.debug("ValueType: "+value.getClass().getName());
			}
			psProcess.setStatementParameters(value, ft, _index);
		}
	}
	
	/**
	 * 处理实体的id的值
	 * @param psProcess
	 * @throws Exception
	 */
	protected void processPrimaryValues(ParametersProcessor psProcess) throws Exception{
		List<PrimarysMetadata> pms = bean.getPrimarys();
		for(int i = 0, count = pms.size(); i < count; ++i){
			PrimarysMetadata pk = pms.get(i);
			Class<?> ft = pk.getFieldType();
			Object value = pk.getValue(entity);
			int _index = this.getNextIndex();
			if(logger.isDebugEnabled()){
				logger.debug("参数索引："+_index+" , 主键列名:"+pk.getColName()+" , FieldName:"+pk.getFieldName()+" , FieldType:"+pk.getFieldType().getName()+" ,FieldValue: "+value);
				if(value != null)
					logger.debug(value.getClass().getName());
			}
			psProcess.setStatementParameters(value, ft, _index);
		}
	}
	
	protected CarpSessionImpl getSession() {
		return session;
	}

	public Object getEntity() {
		return entity;
	}

	protected void setEntity(Object entity) {
		this.entity = entity;
	}
	
	protected String getSql() {
		return sql;
	}

	protected void setSql(String sql) {
		this.sql = sql;
	}

	protected CarpBean getBean() {
		return bean;
	}
	protected int getNextIndex(){
		return ++index;
	}
}
