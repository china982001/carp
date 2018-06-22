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

public class BTFloatAssemble extends AbstractAssemble{
	@Override
	public Object setFieldValue(ResultSet rs, Object entity, Field f, int index)
			throws Exception {
		Object value = rs.getObject(index);
		Float b = null;
		if(value != null)
			b = new Float(value.toString());
		this.setFieldValue(entity, f, b);
		return b;
	}

	@Override
	public Object setFieldValue(Object entity, Field f, Object value)throws Exception {
		boolean isAccess = f.isAccessible();
		f.setAccessible(true);
		if(value != null)
			f.set(entity, Float.valueOf(value+""));
		f.setAccessible(isAccess);
		return value;
	}

	@Override
	public Object setMethodValue(ResultSet rs, Object entity, Method m, int index) throws Exception {
		Object value = rs.getFloat(index);
		m.invoke(entity, value);
		return value;
	}
}
