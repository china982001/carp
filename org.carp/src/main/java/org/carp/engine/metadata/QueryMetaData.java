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
package org.carp.engine.metadata;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.carp.type.TypeMapping;
import org.carp.util.EntityUtil;

/**
 * 元数据类
 * 或者执行查询时，解析ResultSet元数据，解析对应的cls的元数据，构建相关的关系
 * @author zhou
 * @since 0.1
 */
public class QueryMetaData extends MetaData{
	private List<String> nameList = new ArrayList<String>();
	private List<Field> fieldList = new ArrayList<Field>();
	private Map<String,Field> fields = new HashMap<String,Field>();
	
	public QueryMetaData(Class<?> cls, ResultSet rs)throws Exception{
		super(rs);
		processMetadata(cls);
	}
	
	/**
	 * 处理cls的元数据，将之与col信息进行关联
	 * @param cls
	 * @throws Exception
	 */
	private void processMetadata(Class<?> cls)throws Exception{
		Set<String> cols = this.getColumnsMap().keySet();
		logger.debug("Parsing field information for the class: [{}]",cls.getName());
		for(String col : cols){
			if("CARP_ROW_NUM".equals(col))
				continue;
			String name = EntityUtil.getFieldName(col);//this.getFieldName(col.toLowerCase());
			ColumnInfo info = this.getColumnsMap().get(col);
			info.setFieldname(name);
			nameList.add(name);
			Field f = cls.getDeclaredField(name);
			info.setField(f);
			fieldList.add(f);
			fields.put(col, f);
			
			info.setJavaType(f.getType());
			info.setAssemble(TypeMapping.getAssembleByFieldType(f.getType()));
			if(logger.isDebugEnabled())
				logger.debug("ColumnName: {} , FieldName: {} , FieldType: {}",col,name,f.getType().getName());
		}
	}
	
}
