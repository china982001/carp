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

import org.carp.impl.CarpQueryImpl;
import org.carp.parameter.Parameter;
import org.carp.parameter.Parameter.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		setSQLParameterValue();
		setLimitQueryParameters();
	}
	
	/**
	 * 设置Statement参数
	 * @throws Exception
	 */
	private void setSQLParameterValue() throws Exception{
		Parameter param = query.getParameters();
		ParametersProcessor psp = new ParametersProcessor(query.getStatement());
		int offset = 0;
		if(this.query.getFirstIndex() != -1)
			offset = this.query.getSession().getDialect().offset();
		for(int idx = 0, count = param.count(); idx < count; ++idx){
			Param p = param.getParamList().get(idx);
			psp.setStatementParameters(p.getValue(), p.getCls(), p.getIndex()+offset);
		}
	}
	
	/**
	 * 设置Statement查询参数，如果是带有分页的sql
	 * @throws Exception
	 */
	private void setLimitQueryParameters() throws Exception{
		if(query.getFetchSize() != 0){
			logger.debug("fetch size : "+query.getFetchSize());
			query.getStatement().setFetchSize(query.getFetchSize());
		}
		if(query.getTimeout()!=0){
			logger.debug("query timeout : "+query.getTimeout());
			query.getStatement().setQueryTimeout(query.getTimeout());
		}
		if(query.getFirstIndex()!=-1 && query.getMaxCount()!=-1 ){
			if(logger.isDebugEnabled()){
				logger.debug("first index : "+query.getFirstIndex());
				logger.debug("max rownum : "+query.getMaxCount());
			}
			this.query.getSession().getDialect().setQueryParameters(query);
		}
	}
}
