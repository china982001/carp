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
package org.carp.script;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SQLFactory {
	private static Map<String,SQLNode> sqlMap = new ConcurrentHashMap<String, SQLNode>();

	public static SQL getSQL(String sqlname) throws Exception{
		return getSQL(sqlname,null);
	}
	public static SQL getSQL(String sqlname,Map<String,Object> param) throws Exception{
		SQLNode sqlNode = sqlMap.get(sqlname).onClone();
		SQL sql = new SQL(sqlNode,param);
		sql.parser();
		return sql;
	}
	
	protected static void putSQLNode(String name, SQLNode sqlnode){
		sqlMap.put(name, sqlnode);
	}
	

}
