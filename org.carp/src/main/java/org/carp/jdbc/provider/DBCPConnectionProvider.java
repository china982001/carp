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
	public DBCPConnectionProvider(CarpSetting carp) throws CarpException {
		super(carp);
	}

	/**
	 * create dbcp datasource by PoolingDataSource
	 * @throws CarpException
	 */
//	protected void createDataSource() throws CarpException {
//		BasicDataSource bds;
//		try {
//			this.getConfig().getConnPoolProperty().put("user", this.getConfig().getUserName());
//			this.getConfig().getConnPoolProperty().put("url", this.getConfig().getUrl());
//			this.getConfig().getConnPoolProperty().put("driverClassName", this.getConfig().getDriverClass());
//			String password = this.getConfig().getPassword();
//			if(this.getConfig().isPwdEncode()){
//				IPasswordDecryptor decryptor = this.getConfig().getDecryptor();
//				if(this.getConfig().getDecryptor() == null)
//					decryptor = new PasswordDecryptor();
//				password = decryptor.decrypt(password);
//			}
//			this.getConfig().getConnPoolProperty().put("password", password);
//			if (this.getConfig().getCatalog() != null && !this.getConfig().getCatalog().equals(""))
//				this.getConfig().getConnPoolProperty().put("defaultCatalog", this.getConfig().getCatalog());
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
	protected void createDataSource() throws CarpException {
		BasicDataSource bds;
		try {
			this.getConfig().getConnPoolProperty().put("username", this.getConfig().getUserName());
			this.getConfig().getConnPoolProperty().put("url", this.getConfig().getUrl());
			if(this.getConfig().getDriverClass() != null)
				this.getConfig().getConnPoolProperty().put("driverClassName", this.getConfig().getDriverClass());
			String password = this.getConfig().getPassword();
			if(this.getConfig().isPwdEncode()){
				IPasswordDecryptor decryptor = this.getConfig().getDecryptor();
				if(this.getConfig().getDecryptor() == null)
					decryptor = new PasswordDecryptor();
				password = decryptor.decrypt(password);
			}
			this.getConfig().getConnPoolProperty().put("password", password);
			if (this.getConfig().getCatalog() != null && !this.getConfig().getCatalog().equals(""))
				this.getConfig().getConnPoolProperty().put("defaultCatalog", this.getConfig().getCatalog());
			bds = BasicDataSourceFactory.createDataSource(this.getConfig().getConnPoolProperty());
			Enumeration<Object> enums = this.getConfig().getExtProperty().keys();
			while (enums.hasMoreElements()) {
				String key = (String) enums.nextElement();
				bds.addConnectionProperty(key, this.getConfig().getExtProperty().getProperty(key));
			}
			this.setDataSource(bds);
		} catch (Exception e) {
			throw new CarpException("Create dbcp BasicDataSource failed. Cause:"+e.getMessage(),e);
		}
	}
}
