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

import java.util.Enumeration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
import org.carp.security.IPasswordDecryptor;
import org.carp.security.PasswordDecryptor;

/**
 * dbcp connection pool warper class
 * use dbcp to provider datasource
 * @author zhou
 * @since 0.1
 */
public class DBCPConnectionProvider extends AbstractConnectionProvider {
	private CarpSetting carp;
	public DBCPConnectionProvider(CarpSetting carp) throws CarpException {
		this.carp = carp;
		build();
		this.databaseProducename();
		this.dialect();
	}

	/**
	 * create dbcp datasource by PoolingDataSource
	 * @throws CarpException
	 */
//	private void build() throws CarpException {
//		BasicDataSource bds;
//		try {
//			carp.getConnPoolProperty().put("user", carp.getUserName());
//			carp.getConnPoolProperty().put("url", carp.getUrl());
//			carp.getConnPoolProperty().put("driverClassName", carp.getDriverClass());
//			String password = carp.getPassword();
//			if(carp.isPwdEncode()){
//				IPasswordDecryptor decryptor = carp.getDecryptor();
//				if(carp.getDecryptor() == null)
//					decryptor = new PasswordDecryptor();
//				password = decryptor.decrypt(password);
//			}
//			carp.getConnPoolProperty().put("password", password);
//			if (carp.getCatalog() != null && !carp.getCatalog().equals(""))
//				carp.getConnPoolProperty().put("defaultCatalog", carp.getCatalog());
//			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(carp.getUrl(),carp.getConnPoolProperty());
//			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
//			ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
//			poolableConnectionFactory.setPool(connectionPool);
//			PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
//			
//			this.setDataSource(dataSource);
//		} catch (Exception e) {
//			throw new CarpException("Create dbcp BasicDataSource failed. Cause:"+e.getMessage(),e);
//		}
//	}
	
	/**
	 * create dbcp datasource by BasicDataSource
	 * @throws CarpException
	 */
	private void build() throws CarpException {
		BasicDataSource bds;
		try {
			carp.getConnPoolProperty().put("username", carp.getUserName());
			carp.getConnPoolProperty().put("url", carp.getUrl());
			if(carp.getDriverClass() != null)
				carp.getConnPoolProperty().put("driverClassName", carp.getDriverClass());
			String password = carp.getPassword();
			if(carp.isPwdEncode()){
				IPasswordDecryptor decryptor = carp.getDecryptor();
				if(carp.getDecryptor() == null)
					decryptor = new PasswordDecryptor();
				password = decryptor.decrypt(password);
			}
			carp.getConnPoolProperty().put("password", password);
			if (carp.getCatalog() != null && !carp.getCatalog().equals(""))
				carp.getConnPoolProperty().put("defaultCatalog", carp.getCatalog());
			bds = BasicDataSourceFactory.createDataSource(carp.getConnPoolProperty());
			Enumeration<Object> enums = carp.getExtProperty().keys();
			while (enums.hasMoreElements()) {
				String key = (String) enums.nextElement();
				bds.addConnectionProperty(key, carp.getExtProperty().getProperty(key));
			}
			this.setDataSource(bds);
		} catch (Exception e) {
			throw new CarpException("Create dbcp BasicDataSource failed. Cause:"+e.getMessage(),e);
		}
	}

	public CarpSetting getConfig() {
		return this.carp;
	}
}
