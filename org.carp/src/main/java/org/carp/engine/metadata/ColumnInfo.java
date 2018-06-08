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


/**
 * 
 * @author zhou
 * @version 0.1
 */
public final class ColumnInfo {
	private String _colname;
	private String _fieldname;
	private int sqlType;
	private Class<?> javaType;
	private Assemble  _assemble;
	private Field _field;
	private int idx;
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
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("column index: [").append(this.idx).append("] ");
		builder.append("column name: [").append(this._colname).append("] ");
		builder.append("field name: [").append(this._fieldname).append("] ");
		builder.append("sql type: [").append(this.sqlType).append("] ");
		builder.append("java type: [").append(this.javaType).append("] ");
		builder.append("Assemble: [").append(this._assemble).append("] ");
		return builder.toString();
	}
}
