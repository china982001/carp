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
package org.carp.jdbc;

import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
import org.carp.jdbc.provider.C3p0ConnectionProvider;
import org.carp.jdbc.provider.ConnectionProvider;
import org.carp.jdbc.provider.DBCPConnectionProvider;
import org.carp.jdbc.provider.DataSourceConnectionProvider;
import org.carp.jdbc.provider.JDBCConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库连接工厂类
 * 创建数据库连接管理对象。
 * @author zhou
 * @since 0.1
 */
public class ConnectionProviderFactory {
	private static final Logger logger = LoggerFactory.getLogger(ConnectionProviderFactory.class);
	public static ConnectionProvider getConnectionProvider(CarpSetting carp)throws CarpException{
		ConnectionProvider provider = null;
		if(carp.getUrl() != null && carp.getDataSource()!=null)
			throw new CarpException("carp.url and carp.datasource can exist only once, not at the same time");
		if(carp.getUrl()!=null){
			String pool = carp.getPool()==null?"":carp.getPool().toUpperCase();
			if("DBCP".equals(pool))
				provider = new DBCPConnectionProvider(carp);
			else if("C3P0".equals(pool))
				provider = new C3p0ConnectionProvider(carp);
			else
				provider = new JDBCConnectionProvider(carp);
		}else if(carp.getDataSource() != null){
			provider = new DataSourceConnectionProvider(carp);
		}else
			throw new CarpException("Carp configuration file error! No database connection information in the configuration.");
		logger.info("Use ConnectionProvider: {}",provider.getClass().getName());
		return provider;
	}
}
