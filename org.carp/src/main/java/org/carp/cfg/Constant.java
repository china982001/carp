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
package org.carp.cfg;

/**
 * Carp常量定义类
 * @author zhou
 * @since 0.1
 */
public final class Constant {
	/**
	 * Carp Configuration property defined
	 */
	public static final String USER_NAME = "carp.user";  
	public static final String USER_PASSWORD = "carp.password";
	public static final String PWD_ENCODE = "carp.password.encode";
	public static final String DRIVER_CLASS = "carp.driver";
	public static final String CONNECTIION_URL = "carp.url";
	public static final String DATASOURCE = "carp.datasource";
	public static final String AUTO_COMMIT = "connection.autocommit";
	public static final String DIALECT = "carp.dialect";
	public static final String CONNECT_POOL = "carp.pool";
	public static final String CARP_TRANSACTION = "carp.transaction";
	public static final String CARP_TRANS_ISOLATIONLEVEL = "carp.transaction.isolationlevel";
	public static final String SHOW_SQL = "show.sql";
	public static final String CARP_CACHE = "carp.cache";
	public static final String CARP_JDBC_BATCH_SIZE = "carp.jdbc.batch_size";
	public static final String CARP_JDBC_FETCH_SIZE = "carp.jdbc.fetch_size";
	public static final String ENABLED_SCROLL_RESULTSET = "enabled.scroll_resultset";
	public static final String CARP_CATALOG = "carp.catalog";
	public static final String CARP_SCHEMA = "carp.schema";
	public static final String QUERY_TIMEOUT = "query.timeout";

	/**
	 * transaction type defined
	 */
	public static final String TRANSACTION_JDBC = "JDBC";
	public static final String TRANSACTION_JTA = "JTA";
}
