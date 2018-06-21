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
package org.carp.jdbc.provider;

import java.sql.Connection;

import javax.sql.DataSource;

import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
import org.carp.sql.CarpSql;
import org.carp.sql.DB2CarpSql;
import org.carp.sql.DefaultSql;
import org.carp.sql.DerbyCarpSql;
import org.carp.sql.H2CarpSql;
import org.carp.sql.HSQLCarpSql;
import org.carp.sql.MySqlCarpSql;
import org.carp.sql.OracleCarpSql;
import org.carp.sql.PostgreSqlCarpSql;
import org.carp.sql.SqlServer2005CarpSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConnectionProvider接口实现类
 * @author Administrator
 * @see ConnectionProvider
 * @version 0.2
 */
public abstract class AbstractConnectionProvider implements ConnectionProvider {
	private static final Logger logger = LoggerFactory.getLogger(AbstractConnectionProvider.class);
	private CarpSetting config;
	private String databaseName ="";
	private int databaseVersion;
	private DataSource dataSource;
	private CarpSql dialect = null;
	
	public AbstractConnectionProvider(CarpSetting config)throws CarpException{
		this.config = config;
		createDataSource();
		initDatabaseInfo();
		initDatabaseDialect();
	}
	
	/**
	 * Initialize a data source to get a database connection
	 */
	protected abstract void createDataSource()throws CarpException;
	/**
	 * Gets the database metadata for automatically judging database dialect objects
	 * 如：ORACLE,DB2，等
	 * @throws CarpException
	 */
	protected void initDatabaseInfo() throws CarpException{
		try(Connection conn = this.getDataSource().getConnection()){
			this.databaseName = conn.getMetaData().getDatabaseProductName().toUpperCase();
			this.databaseVersion = conn.getMetaData().getDatabaseMajorVersion();
			if(logger.isDebugEnabled()){
				logger.debug("database : "+databaseName+" , MajorVersion : "+this.databaseVersion);
			}
		}catch(Exception ex){
			throw new CarpException(ex);
		}
	}
	
	/**
	 * Initialize the database dialect object or, if it does not exist, use the default dialect
	 */
	protected void initDatabaseDialect()throws CarpException{
		Class<? extends CarpSql> carpSqlClass = null;
		if(this.getConfig().getDatabaseDialect()!=null)
			carpSqlClass = this.getConfig().getDatabaseDialect();
		else{
			if(this.databaseName.indexOf("DB2") != -1){//db2
				carpSqlClass = DB2CarpSql.class;
			}else if(this.databaseName.indexOf("ORACLE") != -1){//oracle
				carpSqlClass = OracleCarpSql.class;
			}else if(this.databaseName.indexOf("MYSQL") != -1){//mysql
				carpSqlClass = MySqlCarpSql.class;
			}else if(this.databaseName.indexOf("HSQL") != -1){//hsqldb
				carpSqlClass = HSQLCarpSql.class;
			}else if(this.databaseName.indexOf("POSTGRE") != -1){//PostgreSql
				carpSqlClass = PostgreSqlCarpSql.class;
			}else if(this.databaseName.indexOf("DERBY") != -1){//PostgreSql
				carpSqlClass = DerbyCarpSql.class;
			}else if(this.databaseName.indexOf("H2") != -1){//PostgreSql
				carpSqlClass = H2CarpSql.class;
			}else if(this.databaseName.indexOf("SQL SERVER") != -1){
				carpSqlClass = SqlServer2005CarpSql.class;
				// Unsupported Sql Server 2000 and below
			}else{//默认sql产生类
				carpSqlClass = DefaultSql.class;
			}
		}
		this.getConfig().setDatabaseDialect(carpSqlClass);
		logger.debug("database dialect : "+this.dialectClass().getName());
	}

	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}

	protected void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public CarpSetting getConfig(){
		return this.config;
	}
	/**
	 * review Database dialect object
	 */
	public CarpSql getDialect(){
		if(dialect == null)
			try {
				dialect = this.dialectClass().newInstance();
			} catch (Exception e) {	}
		return this.dialect;
	}
	
	/**
	 * review CarpSql implements class
	 */
	@Override
	public Class<? extends CarpSql> dialectClass() {
		return this.config.getDatabaseDialect();
	}
}
