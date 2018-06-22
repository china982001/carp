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
package org.carp.assemble;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;


public class DoubleAssemble extends AbstractAssemble{
	@Override	
	public Object setFieldValue(ResultSet rs, Object entity, Field f, int index)
			throws Exception {
		Object value = rs.getObject(index);
		Double b = null;
		if(value != null)
			b = new Double(value.toString());
		this.setFieldValue(entity, f, b);
		return b;
	}

	@Override
	public Object setFieldValue(Object entity, Field f, Object value)throws Exception {
		Double v = null;
		if(value != null)
			v = Double.valueOf(value+"");
		return super.setFieldValue(entity, f, v);
	}

	@Override
	public Object setMethodValue(ResultSet rs, Object entity, Method m, int index) throws Exception {
		Object value = rs.getDouble(index);
		m.invoke(entity, value);
		return value;
	}
}
