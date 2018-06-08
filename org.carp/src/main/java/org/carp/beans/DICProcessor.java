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
import org.carp.annotation.CarpAnnotation;
import org.carp.annotation.Dic;
import org.carp.exception.CarpException;

public class DICProcessor  implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(DICProcessor.class);
	public void parse(CarpBean bean, Class<?> cls) throws CarpException{
		Field[] fields = cls.getDeclaredFields();
		TableMetadata table = bean.getTableInfo();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof Dic){
					Dic dic = (Dic)anno;
					DICMetadata dm = new DICMetadata();
					String containClass = field.getType().getName();
					if(containClass.toLowerCase().indexOf("list") > 0)
						dm.setContainer(CarpAnnotation.Container.List);
					else if(containClass.toLowerCase().indexOf("set") > 0)
						dm.setContainer(CarpAnnotation.Container.Set);
					else
						dm.setContainer(CarpAnnotation.Container.Vector);
					dm.setDicClass(dic.dicClass());
					dm.setFieldName(field.getName());
					dm.setSql(dic.sql());
					dm.setField(field);
					table.addDICMetadata(dm);
					if(logger.isDebugEnabled()){
						logger.debug("DIC ### field: "+field.getName()
								+", dicClass: "+dic.dicClass().getName()
								+", sql: "+dic.sql());
					}
				}
			}
		}
		
	}
}
