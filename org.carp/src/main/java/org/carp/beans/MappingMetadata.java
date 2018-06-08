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



public class MappingMetadata extends Metadata{
	/**
	 * 注解域的field名称
	 */
	private String fieldName;
	/**
	 * 注解域的field类型
	 */
	private Class<?> fieldType;
	/**
	 * 注解域所对应的映射类的schema
	 */
	private String mapSchema;
	/**
	 * 注解域所对应的映射类的Table名称
	 */
	private String mapTable;
	/**
	 * 注解域所对应的映射类mapClass的映射字段名称
	 */
	private String mapColumn;
	/**
	 * 注解域所对应的映射类mapClass的映射field名称
	 */
	private String masterField;
	/**
	 * 注解域所对应的映射类mapClass的映射字段的别名，当注解field的名称与映射mapClass的field名称不一致时使用
	 */
	private String masterAlias;
	/**
	 * 本注解域的类的外键field名称，该外键与映射类的主键field相互对应
	 */
	private String fkName;
	/**
	 * 本注解域的类的外键field类型，该外键与映射类的主键field相互对应
	 */
	private Class<?> fkFieldType;
	/**
	 * 本注解域的类的外键field，该外键与映射类的主键field相互对应
	 */
	private Field fkField;
	/**
	 * 本注解域的类的外键field的映射column名称，该外键与映射类的主键field的映射primary key column相互对应
	 */
	private String fkColumn;
	/**
	 * 本注解域的类对应的映射类的主键field的映射primary key column名称
	 */
	private String pkColumm;
	/**
	 * 是否把本注解域的类对应的映射类的的field的映射column名称添加到select列表中，关联查询
	 */
	private boolean isRelation;
	
	public boolean isRelation() {
		return isRelation;
	}

	public void setRelation(boolean isRelation) {
		this.isRelation = isRelation;
	}

	public String getFkColumn() {
		return fkColumn;
	}

	public void setFkColumn(String fkColumn) {
		this.fkColumn = fkColumn;
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

	public String getMapTable() {
		return mapTable;
	}

	public void setMapTable(String mapTable) {
		this.mapTable = mapTable;
	}

	public String getMapColumn() {
		return mapColumn;
	}

	public void setMapColumn(String mapColumn) {
		this.mapColumn = mapColumn;
	}

	public String getFkName() {
		return fkName;
	}

	public void setFkName(String fkName) {
		this.fkName = fkName;
	}

	public Class<?> getFkFieldType() {
		return fkFieldType;
	}

	public void setFkFieldType(Class<?> fkFieldType) {
		this.fkFieldType = fkFieldType;
	}

	public Field getFkField() {
		return fkField;
	}

	public void setFkField(Field fkField) {
		this.fkField = fkField;
	}

	public String getMapSchema() {
		return mapSchema;
	}

	public void setMapSchema(String mapSchema) {
		this.mapSchema = mapSchema;
	}

	public String getPkColumm() {
		return pkColumm;
	}

	public void setPkColumm(String pkColumm) {
		this.pkColumm = pkColumm;
	}

	public String getMasterField() {
		return masterField;
	}

	public void setMasterField(String masterField) {
		this.masterField = masterField;
	}

	public String getMasterAlias() {
		return masterAlias;
	}

	public void setMasterAlias(String masterAlias) {
		this.masterAlias = masterAlias;
	}
}
