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

import org.carp.impl.CarpQueryImpl;

/**
 * 更新执行器类
 * @author zhou
 * @since 0.2
 */
public class UpdateExecutor extends Executor{
	private int rowCount;
	public UpdateExecutor(CarpQueryImpl query) throws Exception{
		super(query);
	}

	@Override
	protected void executeStatement() throws Exception {
		rowCount = this.getQuery().getStatement().executeUpdate();
	}

	/**
	 * 返回受影响的行数
	 * @return
	 */
	public int getRowCount() {
		return rowCount;
	}

	@Override
	protected void parserMetaData() throws Exception {
	}
}
