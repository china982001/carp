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

public class MTOMetadata extends Metadata {
	public MTOMetadata(Class<?> clazz, Field f) {
		super(clazz, f);
	}
	private String colName;
	private String fieldName;
	private Class<?> fieldClass;
	private String parentField;
	private Class<?> masteClass;
	private boolean  lazy;
	
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Class<?> getMasteClass() {
		return masteClass;
	}
	public void setMasteClass(Class<?> masteClass) {
		this.masteClass = masteClass;
	}
	public boolean getLazy() {
		return lazy;
	}
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	public Class<?> getFieldClass() {
		return fieldClass;
	}
	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}
	public String getParentField() {
		return parentField;
	}
	public void setParentField(String parentField) {
		this.parentField = parentField;
	}
}
