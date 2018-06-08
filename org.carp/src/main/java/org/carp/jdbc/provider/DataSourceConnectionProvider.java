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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
/**
 * DataSource形式的Connenction提供类
 * @author zhou
 */
public class DataSourceConnectionProvider extends AbstractConnectionProvider {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceConnectionProvider.class);
	private CarpSetting carp;

	/**
	 * 
	 * @param carp
	 * @throws CarpException
	 */
	public DataSourceConnectionProvider(CarpSetting carp) throws CarpException{
		this.carp = carp;
		loadDataSourceFromAppServer();
		databaseProducename();
		dialect();
		if(logger.isDebugEnabled())
			logger.debug("database dialect : "+this.getCarpSqlClass());
	}
	public CarpSetting getConfig() {
		return this.carp;
	}
	
	/**
	 * 从服务器数据源配置中加载数据源
	 * @throws CarpException
	 */
	private void loadDataSourceFromAppServer()throws CarpException{
		try{
			javax.naming.InitialContext context = new javax.naming.InitialContext();
			this.setDataSource((javax.sql.DataSource)context.lookup(carp.getDataSource()));
		}catch(Exception ex){
			throw new CarpException(ex);
		}
	}
}
