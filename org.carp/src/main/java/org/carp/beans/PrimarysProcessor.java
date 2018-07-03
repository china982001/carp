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
import org.carp.annotation.Id;
import org.carp.annotation.CarpAnnotation.Generate;
import org.carp.exception.CarpException;
import org.carp.id.Generator;

public class PrimarysProcessor  implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(PrimarysProcessor.class);
	public void parse(CarpBean bean, Class<?> cls) throws CarpException {
		TableMetadata table = bean.getTableInfo();
		Field[] fields = cls.getDeclaredFields();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof Id){
					Id pka = (Id)anno;
					PrimarysMetadata pkm = new PrimarysMetadata(cls, field);
					pkm.setColName(pka.name().toUpperCase());
					pkm.setFieldName(field.getName());
					pkm.setField(field);
					pkm.setFieldType(field.getType());
					pkm.setBuilder(pka.builder());
					pkm.setBuild(pka.build());
					pkm.setRemark(pka.remark());
					pkm.setSequence(pka.sequence());
					table.addPrimarysMetadata(pkm);
					if(logger.isDebugEnabled())
						logger.debug("Table Primarys, column:"+pkm.getColName()+
								", field:"+pkm.getFieldName()+
								", Generator:"+pka.build());
					if(pka.build() == Generate.sequences){
						if(pka.sequence().trim().equals(""))
							throw new CarpException("主键以sequence方式生成，但是没有设置 Sequence，请配置！");
					}else if(pka.build() == Generate.custom){
						if(pka.builder().getName().equals(Generator.class.getName()))
							throw new CarpException("主键以 custom方式生成，需要主键生成器实现类！但是没有配置，请配置！");
						else{
							try {
								
								pka.builder().newInstance();
							} catch (Exception e) {
								throw new CarpException("主键以 custom方式生成，需要主键生成器实现类！但是该生成器实现类不能实例化，请重新配置！");
							}
						}
					}
				}
			}
		}
	}
}
