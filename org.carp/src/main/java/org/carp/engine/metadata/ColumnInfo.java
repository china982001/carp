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
package org.carp.engine.metadata;

import java.lang.reflect.Field;

import org.carp.assemble.Assemble;
import org.carp.engine.metadata.ClassMetadata.MethodInfo;


/**
 * Metadata information definition class
 * @author zhou
 * @version 0.1
 */
public final class ColumnInfo {
	private String _colname;   // column name
	private String _fieldname; //Column corresponding to the field name
	private int sqlType;       //column type
	private Class<?> javaType; //Java type corresponding to column type
	private Assemble  _assemble; //Get data values from rs and assign this value to the corresponding field
	private Field _field;      //filename's Field Object
	private MethodInfo _method;    //setter method
	private int idx;		   //Index value of the columninfo object in the collection, from 0. 0,1,2,3,......
	public String getColname() {
		return _colname;
	}
	public void setColname(String _colname) {
		this._colname = _colname;
	}
	public String getFieldname() {
		return _fieldname;
	}
	public void setFieldname(String _fieldname) {
		this._fieldname = _fieldname;
	}
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public Class<?> getJavaType() {
		return javaType;
	}
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
	public Assemble getAssemble() {
		return _assemble;
	}
	public void setAssemble(Assemble _assemble) {
		this._assemble = _assemble;
	}
	public Field getField() {
		return _field;
	}
	public void setField(Field _field) {
		this._field = _field;
	}
	public MethodInfo getMethod() {
		return _method;
	}
	public void setMethod(MethodInfo _method) {
		this._method = _method;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("ColumnInfo:[");
		builder.append("ColumnIndex: \"").append(this.idx).append("\"; ");
		builder.append("ColumnName: \"").append(this._colname).append("\"; ");
		builder.append("FieldName: \"").append(this._fieldname).append("\"; ");
		builder.append("SqlType: \"").append(this.sqlType).append("\"; ");
		builder.append("JavaType: \"").append(this.javaType).append("\"; ");
		builder.append("Assemble: \"").append(this._assemble.getClass().getName()).append("\"]");
		return builder.toString();
	}
}
