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

import org.carp.exception.CarpException;

public class BeansProcess {
	public static CarpBean parser(Class<?> cls) throws CarpException{
		if(cls == null)
			throw new CarpException("The parameters cls is null. cls : "+cls);
		CarpBean bean = new CarpBean(new TableMetadata());
		new TableProcessor().parse(bean, cls);
		if(bean.getTable()==null || bean.getTable().equals(""))
			throw new CarpException("Class: "+cls.getName()+" Unconfigured mapping relationship.");
		new PrimarysProcessor().parse(bean, cls);
		new ColumnsProcessor().parse(bean, cls);
		new DICProcessor().parse(bean, cls);
		new MTOProcessor().parse(bean, cls);
		new OTMProcessor().parse(bean, cls);
		new OTOProcessor().parse(bean, cls);
		new MappingProcessor().parse(bean, cls);
		return bean;
	}
}
