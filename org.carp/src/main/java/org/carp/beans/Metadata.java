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

public abstract class Metadata {
	private Field field;
	
	public Field getField() {
		return field;
	}
	public void setField(Field f) {
		this.field = f;
	}
	
	public Object getValue(Object obj){
		try {
			Field f = getField();
			boolean b = f.isAccessible();
			f.setAccessible(true);
			Object value = f.get(obj);
			f.setAccessible(b);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setValue(Object obj,Object value){
		try {
			Field f = getField();
			boolean b = f.isAccessible();
			f.setAccessible(true);
			f.set(obj, value);
			f.setAccessible(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
