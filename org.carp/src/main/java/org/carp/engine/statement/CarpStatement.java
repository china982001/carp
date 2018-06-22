package org.carp.engine.statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;
import org.carp.impl.CarpSessionImpl;

/**
 * PreparedStatement对象构建
 * @author zhou
 *
 */
public class CarpStatement {
	private CarpSessionImpl session;
	private CarpQueryImpl query; 
	public CarpStatement(CarpSessionImpl _session){
		this.session = _session;
	}
	public CarpStatement(CarpQueryImpl _query){
		this.query = _query;
		this.session = this.query.getSession();
	}
	
	
	
	/**
	 * 创建查询PreparedStatement对象，根据配置，自适应是否创建滚动结果集.
	 * @return
	 * @throws SQLException
	 * @throws CarpException
	 */
	public PreparedStatement createQueryStatement() throws SQLException, CarpException{
		PreparedStatement ps = null;
		if(query.getStatement() == null){
			if(this.query.getSession().getJdbcContext().getConfig().isEnableScrollableResultSet())
				ps = query.getSession().getConnection().prepareStatement(query.getSql(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			else
				ps = query.getSession().getConnection().prepareStatement(query.getSql());
			query.setStatement(ps);
		}else
			ps = query.getStatement();
		return ps;
	}
	
	/**
	 * 创建查询CallableStatement对象， 根据配置，自适应是否创建滚动结果集.
	 * @return
	 * @throws Exception
	 */
	public PreparedStatement createProcedureStatement() throws Exception{
		if(query.getStatement() == null){
			if(this.query.getSession().getJdbcContext().getConfig().isEnableScrollableResultSet())
				query.setStatement(query.getSession().getConnection().prepareCall(query.getSql(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY));
			else
				query.setStatement(query.getSession().getConnection().prepareCall(query.getSql()));
		}
		return query.getStatement();
	}
	
	/**
	 * 创建PreparedStatement对象
	 * @param sql
	 * @return 
	 * @throws SQLException
	 * @throws CarpException
	 */
	public PreparedStatement createSessionStatement(String sql) throws SQLException, CarpException{
		if(!sql.equals(this.session.getSql())){
			if(this.session.getStatement() != null)
				this.session.getStatement().close();
			this.session.setStatement(this.session.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS));
			this.session.setSql(sql);
		}
		return this.session.getStatement();
	}
}
