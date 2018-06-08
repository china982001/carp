package org.carp.engine.helper;

import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;

/**
 * SQL语句生成助手类
 * @author zhou
 * @since 0.3
 */
public abstract class SqlHelper {
	private CarpSetting conf;
	
	public SqlHelper(CarpSetting setting){
		this.conf = setting;
	}
	
	public abstract String buildSql()throws CarpException;
	
	public abstract String getSql();
	
	public void showSql(){
		if(conf.isShowSql())
			System.out.println("Carp SQL : "+getSql());
	}
}
