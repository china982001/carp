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
import org.carp.annotation.CarpAnnotation.Container;
import org.carp.annotation.Column;
import org.carp.annotation.OneToMany;
import org.carp.exception.CarpException;

public class OTMProcessor  implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(OTMProcessor.class);
	public void parse(CarpBean bean, Class<?> cls) throws CarpException{
		Field[] fields = cls.getDeclaredFields();
		TableMetadata table = bean.getTableInfo();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof OneToMany){
					OneToMany otm = (OneToMany)anno;
					OTMMetadata om = new OTMMetadata();
					om.setCascade(otm.cascade());
					om.setChildClass(otm.childClass());
					String containClass = field.getType().getName().toLowerCase();
					if(containClass.indexOf("list")>-1)
						om.setContain(Container.List);
					else if(containClass.indexOf("set")>-1)
						om.setContain(Container.Set);
					else
						om.setContain(Container.Vector);
					try {
						Column col = otm.childClass().getDeclaredField(otm.foreignName()).getAnnotation(Column.class);
						om.setFkey(col.name().toUpperCase());
					} catch (Exception e) {
						e.printStackTrace();
					} 
					om.setForeignName(otm.foreignName());
					om.setFieldName(field.getName());
					om.setField(field);
					
//					om.setFkey(otm.foreignKey().toUpperCase());
					om.setLazy(otm.lazy());
					om.setRemark(otm.remark());
					table.addOTMMetadata(om);
					if(logger.isDebugEnabled()){
						StringBuilder builder = new StringBuilder();
						builder.append("One To Many ###  One Field/Class: ");
						builder.append(field.getName());
						builder.append(" / "+cls.getName());
						builder.append(" ; Many Class: "+otm.childClass().getName());
						logger.debug(builder.toString());
					}
				}
			}
		}
	}

}
