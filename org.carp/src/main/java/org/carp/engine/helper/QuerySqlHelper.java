package org.carp.engine.helper;

import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;
import org.carp.sql.CarpSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CarpQuery's object sql helper class
 * @author zhoubin
 */
public class QuerySqlHelper extends SqlHelper {
	private final static Logger logger = LoggerFactory.getLogger(QuerySqlHelper.class);
	private CarpQueryImpl _query;
	public QuerySqlHelper(CarpQueryImpl query){
		super(query.getSession().getJdbcContext().getConfig());
		_query = query;
	}
	
	/**
	 * create sql
	 */
	@Override
	public String buildSql() throws CarpException {
		CarpSql carpSql =  this._query.getSession().getDialect();
		logger.debug("Query Class：" + (_query.getClazzes().length==0?"":_query.getClazzes()[0].getName()));
		if(_query.getFirstIndex()!=-1 && _query.getMaxCount()!=-1 ){
			if(_query.getSql() == null)
				_query.setSql(carpSql.getPageSql(_query.getClazzes()[0]));
			else
				_query.setSql(carpSql.getPageSql(_query.getSql()));
		}else{
			if(_query.getSql() == null)
				_query.setSql(carpSql.getQuerySql(_query.getClazzes()[0]));
		}
		logger.debug("Query SQL: "+_query.getSql());
		return _query.getSql();
	}

	/**
	 * get sql
	 */
	@Override
	public String getSql() {
		return _query.getSql();
	}

}
