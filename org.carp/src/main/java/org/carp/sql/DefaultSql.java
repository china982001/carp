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
package org.carp.sql;

import java.sql.SQLException;

import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;

public class DefaultSql extends AbstractSql{

	public String getSequenceSql(String seq) throws CarpException {
		throw new CarpException("不支持序列的数据库类型！");
	}

	public int offset() {
		return 0;
	}

	@Override
	public String getPageSql(String sql) {
		return sql;
	}

	/**
	 * 重载方法，数据库不支持分页功能
	 */
	public PageSupport pageMode(){
		return PageSupport.NONE;
	}

	public void setQueryParameters(CarpQueryImpl query) throws SQLException {
		// TODO Auto-generated method stub
	}
}
