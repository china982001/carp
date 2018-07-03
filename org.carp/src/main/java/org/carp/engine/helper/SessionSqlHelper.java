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
package org.carp.engine.helper;

import org.carp.engine.event.Event;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;
import org.carp.sql.CarpSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sql statement generation helper
 * @author zhou
 * @version 0.3
 */
public class SessionSqlHelper extends SqlHelper {
	private final static Logger logger = LoggerFactory.getLogger(SessionSqlHelper.class);
	private CarpSessionImpl _session;
	private int sqlType;
	private Class<?> cls;
	private String sql;
	
	public SessionSqlHelper(CarpSessionImpl session,int eventType, Class<?> cls){
		super(session.getJdbcContext().getConfig());
		this._session = session;
		this.sqlType = eventType;
		this.cls = cls;
	}
	
	/**
	 * create/get sql from CarpSql
	 */
	@Override
	public String buildSql() throws CarpException {
		CarpSql carpSql =  this._session.getDialect();
		switch(this.sqlType){
			case Event.INSERT_EVENT_TYPE :sql = carpSql.getInsertSql(cls);break;
			case Event.UPDATE_EVENT_TYPE :sql = carpSql.getUpdateSql(cls);break;
			case Event.DELETE_EVENT_TYPE :sql = carpSql.getDeleteSql(cls);break;
			default:sql = carpSql.getLoadSql(cls);
		}
		logger.debug("Query SQL: {}",sql);
		return sql;
	}

	/**
	 * retrieve sql
	 */
	@Override
	public String getSql() {
		return sql;
	}

}
