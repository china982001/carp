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

import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
import org.carp.security.IPasswordDecryptor;
import org.carp.security.PasswordDecryptor;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * C3P0 Connection pool wraper class
 * 
 * @author zhou
 * @since 0.1
 */
public class C3p0ConnectionProvider extends AbstractConnectionProvider {

	public C3p0ConnectionProvider(CarpSetting carp) throws CarpException {
		super(carp);
	}

	/**
	 * create c3p0 datasource
	 * @throws CarpException
	 */
	protected void createDataSource() throws CarpException {
		ComboPooledDataSource bds = new ComboPooledDataSource();
		if(this.getConfig().getDriverClass() != null)
			try {
				bds.setDriverClass(this.getConfig().getDriverClass());
			} catch (Exception e) {
				throw new CarpException("Loaded DriverClass failed. Cause:"+e.getMessage(),e);
			}
		bds.setJdbcUrl(this.getConfig().getUrl());
		bds.setUser(this.getConfig().getUserName());
		String password = this.getConfig().getPassword();
		if(this.getConfig().isPwdEncode()){
			IPasswordDecryptor decryptor = this.getConfig().getDecryptor();
			if(this.getConfig().getDecryptor() == null)
				decryptor = new PasswordDecryptor();
			password = decryptor.decrypt(password);
		}
		bds.setPassword(password);
		//////////////////////////////////
		//   C3P0 ConnectionPool property setting,
		//////////////////////////////////
		if(!bds.isAutoCommitOnClose() && isNotNull("autoCommitOnClose"))
			bds.setAutoCommitOnClose(getBoolValue("autoCommitOnClose",false));
		if(bds.getAutomaticTestTable() == null && isNotNull("automaticTestTable"))
			bds.setAutomaticTestTable(getValue("automaticTestTable",null));
		if(!bds.isBreakAfterAcquireFailure() && isNotNull("breakAfterAcquireFailure"))
			bds.setBreakAfterAcquireFailure(getBoolValue("breakAfterAcquireFailure",false));
		if(bds.getCheckoutTimeout() == 0 && isNotNull("checkoutTimeout"))
			bds.setCheckoutTimeout(getIntValue("checkoutTimeout",0));
		if(bds.getIdleConnectionTestPeriod() == 0 && isNotNull("idleConnectionTestPeriod"))
			bds.setIdleConnectionTestPeriod(getIntValue("idleConnectionTestPeriod",0));
		if(bds.getInitialPoolSize()==3 && isNotNull("initialPoolSize"))
			bds.setInitialPoolSize(getIntValue("initialPoolSize",3));
		if(bds.getMaxConnectionAge()==0 && isNotNull("maxConnectionAge"))
			bds.setMaxConnectionAge(getIntValue("maxConnectionAge",0));
		if(bds.getMaxIdleTime() == 0 && isNotNull("maxIdleTime"))
			bds.setMaxIdleTime(getIntValue("maxIdleTime",0));
		if(bds.getMaxPoolSize() == 15 && isNotNull("maxPoolSize"))
			bds.setMaxPoolSize(getIntValue("maxPoolSize",15));
		if(bds.getMinPoolSize() == 3 && isNotNull("minPoolSize"))
			bds.setMinPoolSize(getIntValue("minPoolSize",3));
		if(bds.getNumHelperThreads() == 3 && isNotNull("numHelperThreads"))
			bds.setNumHelperThreads(getIntValue("numHelperThreads",3));
		if(!bds.isTestConnectionOnCheckin() && isNotNull("testConnectionOnCheckin"))	
			bds.setTestConnectionOnCheckin(getBoolValue("testConnectionOnCheckin",false));
		if(!bds.isTestConnectionOnCheckout() && isNotNull("testConnectionOnCheckout"))
			bds.setTestConnectionOnCheckout(getBoolValue("testConnectionOnCheckout",false));
		this.setDataSource(bds);
	}

	private boolean isNotNull(String key){
		return this.getConfig().getConnPoolProperty().containsKey(key);
	}
	private String getValue(String key,String defaultValue){
		return this.getConfig().getConnPoolProperty().getProperty(key, defaultValue);
	}
	
	private int getIntValue(String key,int defaultValue){
		String value = this.getConfig().getConnPoolProperty().getProperty(key);
		if(value == null)
			return defaultValue;
		return Integer.parseInt(value);
	}
	
	private boolean getBoolValue(String key,boolean defaultValue){
		String value = this.getConfig().getConnPoolProperty().getProperty(key);
		if(value == null)
			return defaultValue;
		return Boolean.getBoolean(value);
	}
}
