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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.carp.engine.SQLParameter;
import org.carp.engine.helper.QuerySqlHelper;
import org.carp.engine.helper.SqlHelper;
import org.carp.engine.metadata.ColumnInfo;
import org.carp.engine.metadata.MetaData;
import org.carp.engine.statement.CarpStatement;
import org.carp.impl.CarpQueryImpl;
/**
 * 执行器基本抽象类
 * @author zhou
 * @since 0.2
 */
public abstract class Executor {
	private CarpQueryImpl _query = null;
	private MetaData metadata; //结果集元数据
	public Executor(CarpQueryImpl query) throws Exception{
		this._query = query;
		process();
	}
	
	protected CarpQueryImpl getQuery(){
		return this._query;
	}
	
	/**
	 * 处理过程
	 * @throws Exception
	 */
	protected void process() throws Exception{
		SqlHelper helper = new QuerySqlHelper(this._query);
		helper.buildSql();
		helper.showSql();
//		new Sql(this._query).showSql();//构建Sql语句
		new CarpStatement(this._query).createQueryStatement(); //创建Statement对象
		//设置Statement参数
		new SQLParameter(this._query).processSQLParameters();
		executeStatement();//执行Statement
		parserMetaData();//metadata
		setReturnNamesAndTypes();
	}
	
	
	/**
	 * 执行Statement，子类需要实现该方法
	 * @throws Exception
	 */
	abstract protected void executeStatement()throws Exception;
	
	abstract protected void parserMetaData()throws Exception;

	public MetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(MetaData metadata) {
		this.metadata = metadata;
	}
	protected void setReturnNamesAndTypes(){
		if(this.metadata == null)
			return;
		_query.setReturnNames(this.getMetadata().getColumnsMap().keySet().toArray(new String[0]));
		Collection<ColumnInfo> colls = this.getMetadata().getColumnsMap().values();
		List<Class<?>> clz = new ArrayList<Class<?>>();
		for(ColumnInfo col : colls){
			clz.add(col.getJavaType());
		}
		_query.setReturnTypes(clz.toArray(new Class<?>[0]));
	}
}
