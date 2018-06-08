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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.carp.annotation.Table;

public class TableProcessor implements AnnotationProcessor{
	private static final Logger logger = LoggerFactory.getLogger(TableProcessor.class);
	public void parse(CarpBean bean, Class<?> cls) {
		Annotation[] annos = cls.getAnnotations();
		if(annos == null || annos.length == 0)
			return;
		TableMetadata table = bean.getTableInfo();
		table.setCls(cls);
		for(Annotation anno:annos){
			if(anno instanceof Table){
				Table ta = (Table)anno;
				table.setTable(ta.name().trim().toUpperCase());
				table.setRemark(ta.remark());
				table.setSchema(ta.schema().trim().equals("")?null:ta.schema().trim());
				if(logger.isDebugEnabled())
					logger.debug("Database Table,  table : "+ta.name()+"; schema : "+ta.schema());
			}
		}
	}
}
