package org.carp.engine.helper;

import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;

/**
 * Helper classes that generate SQL statements.<br/>
 * SQL语句生成助手类
 * @author zhou
 * @since 0.3
 */
public abstract class SqlHelper {
	private CarpSetting conf;
	
	public SqlHelper(CarpSetting setting){
		this.conf = setting;
	}
	/**
	 * Build SQL statements
	 * @return
	 * @throws CarpException
	 */
	public abstract String buildSql()throws CarpException;
	
	/**
	 * Get the generated sql statement
	 * @return
	 */
	public abstract String getSql();
	
	/**
	 * Whether the sql statement is displayed in the console
	 */
	public void showSql(){
		if(conf.isShowSql())
			System.out.println("Carp SQL : "+getSql());
	}
}
