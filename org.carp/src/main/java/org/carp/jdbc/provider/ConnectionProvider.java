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

import javax.sql.DataSource;

import org.carp.cfg.CarpSetting;

/**
 * 数据库连接提供接口类
 * @author zhou
 * @since 0.1
 */
public interface ConnectionProvider {
	/**
	 * Get java.sql.DataSource 取得数据源
	 * @return
	 */
	DataSource getDataSource();
	/**
	 * 取得Carp基本配置
	 * @return
	 */
	CarpSetting getConfig();

	/**
	 * Retrieve database dialect class
	 * @return
	 */
	Class<?> getCarpSqlClass();
}
