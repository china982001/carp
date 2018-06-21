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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.annotation.Column;
import org.carp.exception.CarpException;

/**
 * 
 * @author zhou
 * @version 0.1
 */
public class ColumnsProcessor  implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(ColumnsProcessor.class);
	public void parse(CarpBean bean, Class<?> cls)  throws CarpException{
		TableMetadata table = bean.getTableInfo();
		Field[] fields = cls.getDeclaredFields();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof Column){
					ColumnsMetadata cm = new ColumnsMetadata();
					table.addColumnsMetadata(cm);
					Column ca = (Column)anno;
					cm.setColName(ca.name().toUpperCase());
					cm.setFieldName(field.getName());
					cm.setFieldType(field.getType());
					cm.setLength(ca.length());
					cm.setNull(ca.Null());
					cm.setPrecision(ca.precision());
					cm.setRemark(ca.remark());
					cm.setField(field);
					logger.debug("Table Column:[column:{},file:{},length:{}]",cm.getColName(),cm.getFieldName(),cm.getLength());
				}
			}
		}
	}
}
