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

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author zhou
 * @since 0.1
 */
public class TableMetadata {
	private Class<?> cls;
	private String table;
	private String schema;
	private String remark;
	private List<PrimarysMetadata> primaryList = new ArrayList<PrimarysMetadata>(1);
	private List<ColumnsMetadata> columnList = new ArrayList<ColumnsMetadata>(16);
	private List<DICMetadata> dicList = new ArrayList<DICMetadata>(1);
	private List<MTOMetadata> mtoList = new ArrayList<MTOMetadata>(2);
	private List<OTMMetadata> otmList = new ArrayList<OTMMetadata>(2);
	private List<OTOMetadata> otoList = new ArrayList<OTOMetadata>(2);
	private List<MappingMetadata> mapList = new ArrayList<MappingMetadata>(2);
	
	public Class<?> getCls() {
		return cls;
	}
	public void setCls(Class<?> cls) {
		this.cls = cls;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<PrimarysMetadata> getPrimaryList() {
		return primaryList;
	}
	
	public void addPrimarysMetadata(PrimarysMetadata primary){
		this.primaryList.add(primary);
	}
	
	public List<ColumnsMetadata> getColumnList() {
		return columnList;
	}

	public void addColumnsMetadata(ColumnsMetadata column){
		this.columnList.add(column);
	}
	
	public List<DICMetadata> getDicList() {
		return dicList;
	}
	public void addDICMetadata(DICMetadata dic){
		this.dicList.add(dic);
	}
	
	public List<MTOMetadata> getMtoList() {
		return mtoList;
	}
	public void addMTOMetadata(MTOMetadata mto){
		this.mtoList.add(mto);
	}
	
	public List<OTMMetadata> getOtmList() {
		return otmList;
	}
	public void addOTMMetadata(OTMMetadata otm){
		this.otmList.add(otm);
	}
	
	public List<OTOMetadata> getOtoList() {
		return otoList;
	}
	public void addOTOMetadata(OTOMetadata oto){
		this.otoList.add(oto);
	}
	public List<MappingMetadata> getMapList() {
		return mapList;
	}
	public void addMappingMetadata(MappingMetadata mm){
		this.mapList.add(mm);
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public void setPrimaryList(List<PrimarysMetadata> primaryList) {
		this.primaryList = primaryList;
	}
	public void setColumnList(List<ColumnsMetadata> columnList) {
		this.columnList = columnList;
	}
	public void setDicList(List<DICMetadata> dicList) {
		this.dicList = dicList;
	}
	public void setMtoList(List<MTOMetadata> mtoList) {
		this.mtoList = mtoList;
	}
	public void setOtmList(List<OTMMetadata> otmList) {
		this.otmList = otmList;
	}
	public void setOtoList(List<OTOMetadata> otoList) {
		this.otoList = otoList;
	}
	public void setMapList(List<MappingMetadata> mapList) {
		this.mapList = mapList;
	}
}
