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

import org.carp.annotation.CarpAnnotation.Generate;
import org.carp.id.Generator;

public class PrimarysMetadata extends Metadata {
	public PrimarysMetadata(Class<?> clazz, Field f) {
		super(clazz, f);
	}
	private Generate build;
	private Class<? extends Generator> builder;
	private String colName;
	private Class<?> fieldType;
	private String fieldName;
	private String sequence;
	private String remark;
	
	public Generate getBuild() {
		return build;
	}
	public void setBuild(Generate build) {
		this.build = build;
	}
	public Class<? extends Generator> getBuilder() {
		return builder;
	}
	public void setBuilder(Class<? extends Generator> builder) {
		this.builder = builder;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public Class<?> getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
