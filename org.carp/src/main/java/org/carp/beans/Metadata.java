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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.carp.util.EntityUtil;

/**
 * 
 * @author zhou
 * @version 0.1
 */
public abstract class Metadata {
	private Method getterMethod;
	private Method setterMethod;
	private Field field;
	public Metadata(Class<?> clazz, Field f){
		this.field = f;
		this.getterMethod = EntityUtil.getMethod(clazz, EntityUtil.getGetter(f.getName()), new Class<?>[]{});
		this.setterMethod = EntityUtil.getMethod(clazz, EntityUtil.getSetter(f.getName()), new Class<?>[]{f.getType()});
	}
	public Field getField() {
		return field;
	}
	public void setField(Field f) {
		this.field = f;
	}
	
	
	public Object getMethodValue(Object entity) throws Exception{
		if(this.getterMethod == null)
			return null;
		return this.getterMethod.invoke(entity, new Object[]{});
	}
	
	public void setMethodValue(Object entity, Object value)throws Exception{
		if(this.setterMethod != null)
			this.setterMethod.invoke(entity, new Object[]{value});
	}
}
