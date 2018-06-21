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
package org.carp.engine.exec;

import java.sql.SQLException;

import org.carp.engine.helper.QuerySqlHelper;
import org.carp.engine.helper.SqlHelper;
import org.carp.engine.statement.CarpStatement;
import org.carp.impl.CarpQueryImpl;

/**
 * Batch mode actuator，used to perform batch methods.
 * @author zhou
 * @since 0.2
 */
public class BatchExecutor extends Executor{
	public BatchExecutor(CarpQueryImpl query) throws Exception{
		super(query);
	}

	@Override
	protected void executeStatement() throws Exception {
	}

	@Override
	protected void process() throws Exception {
		SqlHelper helper = new QuerySqlHelper(this.getQuery());
		helper.buildSql();
		helper.showSql();
		new CarpStatement(this.getQuery()).createQueryStatement(); //创建Statement对象
	}

	/**
	 * Adds a set of parameters to this PreparedStatement object's batch of commands.
	 * @throws SQLException 
	 */
	public void addBatch() throws Exception {
		//设置Statement参数
		this.getSQLParameter().processSQLParameters();
		this.getQuery().getStatement().addBatch();
		this.getQuery().clearParameters();
	}

	@Override
	protected void parserMetaData() throws Exception {
		
	}
}
