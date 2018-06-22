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

/**
 * Assign the table column value to the property value of the corresponding object
 * 
 * @author zhou
 * @since 0.1
 */
public abstract class AbstractAssemble implements Assemble{

	@Override
	public Object setFieldValue(Object entity, Field f, Object value)throws Exception {
		boolean isAccess = f.isAccessible();
		f.setAccessible(true);
		f.set(entity, value);
		f.setAccessible(isAccess);
		return value;
	}
}
