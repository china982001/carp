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


/**
 * Table's column metadata class, use to mark field's property.
 * @author zhou
 * @since 0.1
 */
public class ColumnsMetadata extends Metadata{
	
	/**
	 * column name
	 */
	private String colName;
	
	/**
	 * field name
	 */
	private String fieldName;
	
	/**
	 * The types of decoration field
	 */
	private Class<?> fieldType;
	
	/**
	 * Whether a field may be null
	 * null: true
	 * not null false
	 */
	private boolean isNull;
	
	/**
	 * note information
	 */
	private String remark;
	
	/**
	 * Length of the field values
	 */
	private int length;
	
	/**
	 * precision of the field values
	 */
	private int precision;
	
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
	public Class<?> getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
}
