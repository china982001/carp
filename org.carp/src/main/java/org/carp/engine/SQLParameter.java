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
package org.carp.engine;

import java.util.Iterator;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.impl.CarpQueryImpl;
import org.carp.parameter.Parameter;
import org.carp.parameter.Parameter.Param;
import org.carp.sql.AbstractSql;
import org.carp.sql.CarpSql;

/**
 * 设置sql语句的参数
 * @author zhou
 * @since 0.1
 */
public class SQLParameter {
	private final static Logger logger = LoggerFactory.getLogger(SQLParameter.class);
	private CarpQueryImpl query;
	
	public SQLParameter(CarpQueryImpl query){
		this.query = query;
	}

	/**
	 * 设置Statement参数
	 * @throws Exception
	 */
	public void processSQLParameters() throws Exception{
		setParameterValue();
		setQueryParameters();
	}
	
	/**
	 * 设置Statement参数
	 * @throws Exception
	 */
	private void setParameterValue() throws Exception{
		Parameter param = query.getParameters();
		ParametersProcessor psp = new ParametersProcessor(query.getStatement());
		int pos = 0;
		if(this.query.getFirstIndex() != -1){
			pos = this.query.getCarpSql().position();
		}
		for(int idx = 0, count = param.count(); idx < count; ++idx){
			Param p = param.getParamList().get(idx);
			psp.setStatementParameters(p.getValue(), p.getCls(), p.getIndex()+pos);
		}
//		for(Iterator<Integer> it = param.getKeys(); it.hasNext();){
//			Integer key = it.next();
//			Class<?> cls = param.getType(key);
//			psp.setStatementParameters(param.getValue(key), cls, key+pos);
//		}
	}
	
	/**
	 * 设置Statement查询参数，如果是带有分页的sql
	 * @throws Exception
	 */
	private void setQueryParameters() throws Exception{
		if(query.getFetchSize() != 0){
			if(logger.isDebugEnabled())
				logger.debug("fetch size : "+query.getFetchSize());
			query.getStatement().setFetchSize(query.getFetchSize());
		}
		if(query.getTimeout()!=0){
			if(logger.isDebugEnabled())
				logger.debug("query timeout : "+query.getTimeout());
			query.getStatement().setQueryTimeout(query.getTimeout());
		}
		if(query.getFirstIndex()!=-1 && query.getMaxCount()!=-1 ){
			if(logger.isDebugEnabled()){
				logger.debug("first index : "+query.getFirstIndex());
				logger.debug("max rownum : "+query.getMaxCount());
			}
			CarpSql carpSql = AbstractSql.getCarpSql(query.getSession().getJdbcContext().getConfig(), query.getCls());//query.getSession().getJdbcContext().getContext().getCarpSql(query.getCls());
			carpSql.setQueryParameters(query);
//			carpSql.setQueryParameters(query.getPreparedStatement(), query.getFirstIndex(), query.getMaxCount(), query.getParameters().count());
		}
	}
}
