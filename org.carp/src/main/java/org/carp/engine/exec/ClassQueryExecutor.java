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

import java.util.List;

import org.carp.engine.metadata.QueryMetaData;
import org.carp.engine.result.RSProcessor;
import org.carp.impl.CarpQueryImpl;

/**
 * 查询执行类
 * @author zhou
 *
 */
public class ClassQueryExecutor extends QueryExecutor{
	private RSProcessor rsp;
	public ClassQueryExecutor(CarpQueryImpl query)throws Exception{
		super(query);
	}
	
	/**
	 * 取得对象集合
	 * @return
	 * @throws Exception
	 */
	public List<Object> list()throws Exception{
		List<Object> list = null;
		try{
			rsp = new RSProcessor(this.getQuery(),this.getQuery().getClazzes()[0],this.getMetadata(),this.getResultSet());
			list = rsp.list();
		}finally{
			this.getResultSet().close();
			this.getMetadata().getColumnsMap().clear();
			this.setMetadata(null);
		}
		return list;
	}

	@Override
	protected void parserMetaData() throws Exception {
		this.setMetadata(new QueryMetaData(this.getQuery().getClazzes()[0],this.getResultSet()));
	}
}
