package org.carp.engine.helper;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;
import org.carp.sql.AbstractSql;
import org.carp.sql.CarpSql;

public class QuerySqlHelper extends SqlHelper {
	private final static Logger logger = LoggerFactory.getLogger(QuerySqlHelper.class);
	private CarpQueryImpl _query;
	public QuerySqlHelper(CarpQueryImpl query){
		super(query.getSession().getJdbcContext().getConfig());
		_query = query;
	}
	@Override
	public String buildSql() throws CarpException {
		CarpSql carpSql =  AbstractSql.getCarpSql(_query.getSession().getJdbcContext().getConfig(), _query.getCls());
		if(logger.isDebugEnabled()){
			logger.debug("Query Class："+_query.getCls());
		}
		if(_query.getFirstIndex()!=-1 && _query.getMaxCount()!=-1 ){
			if(_query.getSql() == null)
				_query.setSql(carpSql.getPageSql());
			else
				_query.setSql(carpSql.getPageSql(_query.getSql()));
		}else{
			if(_query.getSql() == null)
				_query.setSql(carpSql.getQuerySql());
		}
		if(logger.isDebugEnabled()){
			logger.debug("Query SQL: "+_query.getSql());
		}
		return _query.getSql();
	}

	@Override
	public String getSql() {
		return _query.getSql();
	}

}
