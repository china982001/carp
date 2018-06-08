package org.carp.engine.helper;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;
import org.carp.sql.AbstractSql;
import org.carp.sql.CarpSql;

public class SessionSqlHelper extends SqlHelper {
	private final static Logger logger = LoggerFactory.getLogger(SessionSqlHelper.class);
	private CarpSessionImpl _session;
	private String sqlType;
	private Class<?> cls;
	private String sql;
	
	public SessionSqlHelper(CarpSessionImpl session,String sqlType, Class<?> cls){
		super(session.getJdbcContext().getConfig());
		this._session = session;
		this.sqlType = sqlType;
		this.cls = cls;
	}
	
	@Override
	public String buildSql() throws CarpException {
		CarpSql carpSql =  AbstractSql.getCarpSql(_session.getJdbcContext().getConfig(),cls);
		if(sqlType.equals("insert")){
			sql = carpSql.getInsertSql();
		}else if(sqlType.equals("update")){
			sql = carpSql.getUpdateSql();
		}else if(sqlType.equals("delete")){
			sql = carpSql.getDeleteSql();
		}else{ // sqlType == find
			sql = carpSql.getLoadSql();
		}
		if(logger.isDebugEnabled()){
			logger.debug("Query SQL: "+sql);
		}
		return sql;
	}

	@Override
	public String getSql() {
		return sql;
	}

}
