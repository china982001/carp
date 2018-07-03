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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.annotation.OneToOne;
import org.carp.exception.CarpException;

public class OTOProcessor  implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(OTOProcessor.class);
	public void parse(CarpBean bean, Class<?> cls) throws CarpException{
		Field[] fields = cls.getDeclaredFields();
		TableMetadata table = bean.getTableInfo();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof OneToOne){
					OneToOne oto = (OneToOne)anno;
					OTOMetadata om = new OTOMetadata(cls, field);
					om.setFieldName(field.getName());
					om.setField(field);
					om.setCascade(oto.cascade());
					om.setFieldType(field.getType());
					table.addOTOMetadata(om);
					if(logger.isDebugEnabled()){
						StringBuilder builder = new StringBuilder();
						builder.append("One To One ###  Field/Class : ");
						builder.append(field.getName());
						builder.append(" / ");
						builder.append(field.getType().getName());
						logger.debug(builder.toString());
					}
				}
			}
		}
	}
}
