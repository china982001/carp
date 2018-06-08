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
package org.carp.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.annotation.Mapping;
import org.carp.exception.CarpException;

/**
 * Mapping注解解析处理类
 * 解析Mapping注解
 * @author zhou
 * @since 0.1
 */
public class MappingProcessor  implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(MappingProcessor.class);
	
	/**
	 * 解析注解元数据
	 */
	public void parse(CarpBean bean, Class<?> cls) throws CarpException{
		Field[] fields = cls.getDeclaredFields();
		TableMetadata table = bean.getTableInfo();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof Mapping){
					Mapping mp = (Mapping)anno;
					MappingMetadata mm = new MappingMetadata();
					mm.setField(field);
					mm.setFieldName(field.getName());
					mm.setFieldType(field.getType());
					mm.setFkName(mp.foreignkey());
					List<ColumnsMetadata> cols = bean.getColumns();
					for(ColumnsMetadata col: cols)
						if(mp.foreignkey().equals(col.getFieldName())){
							mm.setFkField(col.getField());
							mm.setFkFieldType(col.getFieldType());
							mm.setFkColumn(col.getColName());break;
						}
					if(mm.getFkField() == null)
						throw new CarpException(cls+"，foreignkey："+mp.foreignkey()+" 不存在！");
//					try{
//						Column col = mp.mapCls().getDeclaredField(mp.masterField()).getAnnotation(Column.class);
//						mm.setMapColumn(col.name().toUpperCase());
//					}catch(Exception e){
//						throw new CarpException("",e);
//					}
//					mm.setFieldName(mp.masterField())
					CarpBean b = BeansProcess.parser(mp.mapCls());
					List<ColumnsMetadata> cms = b.getColumns();
					for(ColumnsMetadata col: cms){
						if(mp.masterField().equals(col.getFieldName())){
							mm.setMapColumn(col.getColName());break;
						}
					}
					mm.setMasterField(mp.masterField());
					mm.setRelation(mp.relation());
					mm.setMasterAlias(parserField(field));
					if(mm.getMapColumn() == null)
						throw new CarpException(mp.mapCls()+"，"+mm.getFieldName()+" 不存在！");
					mm.setPkColumm(b.getPrimarys().get(0).getColName());
					mm.setMapTable(b.getTable());
					mm.setMapSchema(b.getSchema());
					if(mm.isRelation()) //allow mapping
						table.addMappingMetadata(mm);
					if(logger.isDebugEnabled()){
						logger.debug("Mapping ### field: "+field.getName()
								+", MappingClass: "+mp.mapCls().getName()
								+", Mapping Column: "+mm.getMapColumn());
					}
				}
			}
		}
	}
	private String parserField(Field f){
		String name = f.getName();
		StringBuilder buf = new StringBuilder("");
		for(int i=0; i<name.length(); ++i){
			String s = name.substring(i,i+1);
			if(s.getBytes()[0]>=65 && s.getBytes()[0]<=90){
				buf.append("_").append(s.toLowerCase());
			}else
				buf.append(s);
		}
		return buf.toString()+"_";
	}
}
