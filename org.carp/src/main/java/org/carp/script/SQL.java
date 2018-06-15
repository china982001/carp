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
package org.carp.script;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Ref;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carp.CarpQuery;
import org.carp.script.node.BaseNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parse wrapper class for dynamic sql statements 
 * for generating dynamic sql and injection of parameter values
 * @author zhou
 * @version 0.3
 */
public class SQL {
	private static final Logger logger = LoggerFactory.getLogger(SQL.class);
	private SQLNode _sqlNode;
	private Map<String,Object> params;
	private StringBuilder buffer = new StringBuilder();
	private List<Object> values = new ArrayList<Object>();
	
	/**
	 * Constructor, creates the SQL object
	 * @param sqlNode.  the sql name defined in the configuration
	 * @param param.  The set of parameters used in dynamic sql
	 */
	public SQL(SQLNode sqlNode,Map<String,Object> _param){
		this._sqlNode = sqlNode;
		this.params = _param!=null?_param:new HashMap<String,Object>(1);
	}
	
	/**
	 * Calculate the conditions in the statement, generate the sql statement.
	 * @throws Exception
	 */
	public void parser() throws Exception{
		List<BaseNode> nodes = _sqlNode.getNodes();
		logger.debug("Sql Parameters : "+ this.params);
		for(BaseNode node : nodes){
			if(node.verifyCondition(params))
				buffer.append(" ").append(node.parser(params, values));
		}
	}
	
	/**
	 * When sql queries are executed, query parameters are injected
	 * @param query.  query session 
	 * @throws Exception
	 */
	public void processParameters(CarpQuery query) throws Exception{
		for(int i = 0; i < this.values.size(); ++i){
			SQLParam p = (SQLParam)this.values.get(i);
			if(p.value instanceof Integer){
				query.setInt(i+1, (Integer)p.getValue());
			}else if(p.value instanceof String){
				query.setString(i+1,p.getValue()+"");
			}else if(p.value instanceof Long){
				query.setLong(i+1, (Long)p.value);
			}else if(p.value instanceof Double){
				query.setDouble(i+1, (Double)p.value);
			}else if(p.value instanceof Float){
				query.setFloat(i+1, (Float)p.value);
			}else if(p.value instanceof byte[]){
				query.setBytes(i+1, (byte[])p.value);
			}else if(p.value instanceof Boolean){
				query.setBoolean(i+1, (Boolean)p.value);
			}else if(p.value instanceof BigDecimal){
				query.setBigDecimal(i+1, (BigDecimal)p.value);
			}else if(p.value instanceof Ref){
				query.setRef(i+1, (Ref)p.value);
			}else if(p.value instanceof InputStream){
				query.setBinaryStream(i+1, (InputStream)p.value);
			}else if(p.value instanceof Reader){
				query.setCharacterStream(i+1, (Reader)p.value);
			}else if(p.value instanceof Date){
				query.setDate(i+1, (Date)p.value);
			}else if(p.value instanceof java.sql.Date){
				query.setDate(i+1, (java.sql.Date)p.value);
			}else if(p.value instanceof Time){
				query.setTime(i+1, (Time)p.value);
			}else if(p.value instanceof URL){
				query.setURL(i+1, (URL)p.value);
			}else{
				query.setObject(i+1, p.value);
			}
		}
	}
	
	/**
	 * Collection of parameter values used by this SQL object
	 * @return
	 */
	public List<Object> getParamValues(){
		return this.values;
	}
	
	/**
	 * Executed sql statements
	 * @return
	 */
	public String getSql(){
		return this.buffer.toString();
	}
}
