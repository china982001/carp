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

import org.carp.DataSet;
import org.carp.impl.CarpQueryImpl;
import org.carp.impl.DataSetImpl;

/**
 * 执行查询类，没有pojo实体，以DataSet对象装载查询出的记录.
 * @author Administrator
 *
 */
public class DatasetQueryExecutor extends QueryExecutor{
	/**
	 * 构造函数
	 * @param query
	 * @throws Exception
	 */
	public DatasetQueryExecutor(CarpQueryImpl query)throws Exception{
		super(query);
	}
	
	/**
	 * 根据查询sql，返回DataSet对象装载的记录.
	 * @return
	 * @throws Exception
	 */
	public DataSet dataSet() throws Exception{
		DataSet dataSet = new DataSetImpl(this.getQuery(),this.getMetadata(),this.getResultSet());
		return dataSet;
	}
}
