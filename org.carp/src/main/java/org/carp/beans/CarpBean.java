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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * pojo的Bean信息管理类
 * @author zhou
 * @since 0.1
 */
public class CarpBean{
	private TableMetadata table;
	
	public CarpBean(TableMetadata tableInfo){
		this.table = tableInfo;
	} 
	public String getTable(){
		return this.table.getTable();
	}
	
	public Class<?> getTableClass(){
		return this.table.getCls();
	}
	
	public String getTableRemark(){
		return this.table.getRemark();
	}
	
	public String getSchema(){
		return this.table.getSchema();
	}
	
	public TableMetadata getTableInfo() {
		return this.table;
	}
	
	public List<PrimarysMetadata> getPrimarys(){
		return this.table.getPrimaryList();
	}
	
	public List<ColumnsMetadata> getColumns() {
		return this.table.getColumnList();
	}
	public List<DICMetadata> getDics() {
		return this.table.getDicList();
	}
	public List<OTMMetadata> getOtms() {
		return this.table.getOtmList();
	}
	public List<MTOMetadata> getMtos() {
		return this.table.getMtoList();
	}
	public List<OTOMetadata> getOtos() {
		return this.table.getOtoList();
	}
	public List<MappingMetadata> getMaps() {
		return this.table.getMapList();
	}
	
	/**
	 * 取得实体的主键值
	 * @param entity
	 * @return
	 */
	public java.io.Serializable getPrimaryValue(Object entity){
		Field f = this.table.getPrimaryList().get(0).getField();
		try {
			boolean b = f.isAccessible();
			f.setAccessible(true);
			Serializable id = (Serializable) f.get(entity);
			f.setAccessible(b);
			return id;
		} catch (Exception e) {}
		return null;
	}
	
	/**
	 * 根据实体类的field名称，取得对应的主键值
	 * @param entity 实体对象
	 * @param name 实体类的field名称，主键field
	 * @return
	 */
	public java.io.Serializable getPrimaryValue(Object entity,String name){
		List<PrimarysMetadata> pms = this.table.getPrimaryList();
		for(PrimarysMetadata pm: pms){
			if(pm.getFieldName().equals(name)){
				Field f = this.table.getPrimaryList().get(0).getField();
				try {
					boolean b = f.isAccessible();
					f.setAccessible(true);
					Serializable id = (Serializable) f.get(entity);
					f.setAccessible(b);
					return id;
				} catch (Exception e) {}
			}
		}
		return null;
	}
}
