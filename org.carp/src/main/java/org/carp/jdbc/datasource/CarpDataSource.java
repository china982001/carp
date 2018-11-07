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
package org.carp.jdbc.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.carp.cfg.CarpSetting;
import org.carp.exception.CarpException;
import org.carp.security.IPasswordDecryptor;
import org.carp.security.PasswordDecryptor;

/**
 * A simple datasource implements class
 * @author zhou
 * @since 0.1
 */
public class CarpDataSource implements DataSource{
	private CarpSetting _carp;
	private Properties p = new Properties();
	
	public CarpDataSource(CarpSetting carp) throws CarpException{
		this._carp = carp;
		try {
			if(carp.getDriverClass() != null && !carp.getDriverClass().equals(""))
				Class.forName(carp.getDriverClass());
		}catch (Exception ex) {
			throw new CarpException("Can't load driver class. please check classpath. Cause:"+ex,ex);
		}
		this.p.putAll(carp.getExtProperty()); 
		this.p.setProperty("user", carp.getUserName());
		String password = carp.getPassword();
		if(carp.isPwdEncode()){
			IPasswordDecryptor decryptor = carp.getDecryptor();
			if(carp.getDecryptor() == null)
				decryptor = new PasswordDecryptor();
			password = decryptor.decrypt(password);
		}
		if(password != null)
			this.p.setProperty("password",password);
	}

	public Connection getConnection() throws SQLException {
		return configure(DriverManager.getConnection(_carp.getUrl(), p));
	}

	public Connection getConnection(String username, String password)throws SQLException {
		return configure(DriverManager.getConnection(_carp.getUrl(), username,password));
	}

	/**
	 * set connection autocommit and transIsoLationLevel
	 * @param conn 
	 * @return
	 * @throws SQLException
	 */
	private Connection configure(Connection conn) throws SQLException {
		if (_carp.isAutoCommit() != null && _carp.isAutoCommit() != conn.getAutoCommit()) {
			conn.setAutoCommit(_carp.isAutoCommit());
		}
		if (_carp.getTransIsoLationLevel() != null) {
			conn.setTransactionIsolation(_carp.getTransIsoLationLevel());
		}
		return conn;
	}
	
	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		DriverManager.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		DriverManager.setLoginTimeout(seconds);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException(getClass().getName() + " is not a wrapper.");
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
}
