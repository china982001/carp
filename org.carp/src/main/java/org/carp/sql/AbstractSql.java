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
package org.carp.sql;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.carp.annotation.CarpAnnotation.Generate;
import org.carp.beans.CarpBean;
import org.carp.beans.ColumnsMetadata;
import org.carp.beans.MappingMetadata;
import org.carp.beans.PrimarysMetadata;
import org.carp.exception.CarpException;
import org.carp.factory.BeansFactory;

public abstract class AbstractSql implements CarpSql{
	protected final static Map<String,String> insertMap = new ConcurrentHashMap<String, String>();
	protected final static Map<String,String> updateMap = new ConcurrentHashMap<String, String>();
	protected final static Map<String,String> deleteMap = new ConcurrentHashMap<String, String>();
	protected final static Map<String,String> loadMap = new ConcurrentHashMap<String, String>();
	protected final static Map<String,String> selectMap = new ConcurrentHashMap<String, String>();
	protected final static Map<String,String> selectPageMap = new ConcurrentHashMap<String, String>();
	
	private String  schema;
	
	public void setSchema(String schema){
		this.schema = schema;
	}
	
	/**
	 * 默认支持分页功能，凡是不支持或部分支持分页功能的数据库，需要在各自的CarpSql接口的实现类中重载该方法，返回相应的值
	 */
	public PageSupport pageMode() {
		return PageSupport.COMPLETE;
	}
	
	/**
	 * 返回查询分页sql
	 */
	public String getPageSql(Class<?> clazz) throws CarpException {
		CarpBean bean = BeansFactory.getBean(clazz);
		String queryPageSql = selectPageMap.get(bean.getTable());
		if(queryPageSql == null){
			queryPageSql = this.getPageSql(this.getQuerySql(clazz));
			selectPageMap.put(bean.getTable(), queryPageSql);
		}
		return queryPageSql; 
	}
	
	/**
	 * 根据主键查询记录的sql语句
	 * @throws CarpException 
	 */
	public String getLoadSql(Class<?> clazz) throws CarpException{
		CarpBean bean = BeansFactory.getBean(clazz);
		String loadSql = loadMap.get(bean.getTable());
		if(loadSql == null){
			List<PrimarysMetadata> pks = bean.getPrimarys();
			if(pks.size() == 0)
				throw new CarpException("Primary key not exist, can't find by key.");
			StringBuilder sql = new StringBuilder("SELECT * FROM  ");
			this.appendSchema(bean,sql);
			sql.append(bean.getTable()).append(" WHERE ");
			for(int i = 0, count = pks.size(); i < count; ++i){
				if(i!=0)
					sql.append(" AND ");
				sql.append(pks.get(i).getColName()).append("=?");
			}
			loadSql = sql.toString();
			loadMap.put(bean.getTable(), loadSql);
		}
		return loadSql;
	}
	
	/**
	 * 取得执行分页查询时，select查询的分页sql语句
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public abstract String getPageSql(String sql) throws CarpException;
	
	/**
	 * @throws CarpException 
	 * 
	 */
	public String getQuerySql(Class<?> clazz) throws CarpException{
		CarpBean bean = BeansFactory.getBean(clazz);
		if(bean == null)
			throw new CarpException("Class:"+clazz+" has not a assiosite mapping Table!");
		String querySql = selectMap.get(bean.getTable());
		if(querySql == null){
			StringBuilder sql = new StringBuilder("SELECT ");
			for(PrimarysMetadata pk : bean.getPrimarys())
				sql.append("T0_.").append(pk.getColName()).append(",");
			this.appendColumens(bean,sql, "T0_");
			for(int i=0,count =bean.getMaps().size(); i<count; ++i){
				MappingMetadata mapMeta = bean.getMaps().get(i);
				sql.append(",").append("T").append(i+1).append("_.");
				sql.append(mapMeta.getMapColumn()).append(" ").append(mapMeta.getMasterAlias());
			}
			sql.append(" FROM ");
			this.appendSchema(bean,sql);
			sql.append(bean.getTable()).append(" T0_ ");
			for(int i=0,count =bean.getMaps().size(); i<count; ++i){
				MappingMetadata mapMeta = bean.getMaps().get(i);
				sql.append(", ");
				sql.append((mapMeta.getMapSchema() != null) ? mapMeta.getMapSchema() +"." : "");
				sql.append(mapMeta.getMapTable()).append(" T").append(i+1).append("_ ");
			}
			if(!bean.getMaps().isEmpty()){
				sql.append(" WHERE ");
				for(int i=0,count = bean.getMaps().size(); i<count; ++i){
					MappingMetadata mm = bean.getMaps().get(i);
					if(i!=0)
						sql.append(" AND ");
					sql.append("T0_.").append(mm.getFkColumn()).append(" = ");
					sql.append("T").append(i+1).append("_.").append(mm.getPkColumm());
				}
			}
			querySql = sql.toString();
			selectMap.put(bean.getTable(), querySql);
		}
		return querySql;
	}
	
	/**
	 * 取得执行insert操作时的insert sql语句
	 * @param table
	 * @return
	 * @throws CarpException 
	 */
	public String getInsertSql(Class<?> clazz) throws CarpException{
		CarpBean bean = BeansFactory.getBean(clazz);
		if(bean == null)
			throw new CarpException("Class:"+clazz+" has not a assiosite mapping Table!");
		String insertSql = insertMap.get(bean.getTable());
		if(insertSql == null){
			List<ColumnsMetadata> cms = bean.getColumns();
			List<PrimarysMetadata> pks = bean.getPrimarys();
			StringBuilder sql = new StringBuilder("INSERT INTO ");
			this.appendSchema(bean,sql);
			sql.append(bean.getTable()).append("(");
			this.appendColumens(bean,sql, null);
			for(PrimarysMetadata pk:pks){
				if(pk.getBuild() != Generate.auto){
					sql.append(",");
					sql.append(pk.getColName());
				}
			}
			sql.append(") VALUES(");
			for(int i=0,count = cms.size(); i<count; ++i){
				if(i!=0)
					sql.append(",");
				sql.append("?");
			}
			
			for(int i=0, count = pks.size(); i < count; ++i){
				PrimarysMetadata pk = pks.get(i);
				if(pk.getBuild() != Generate.auto){
					sql.append(",");
					sql.append("?");
				}
			}
			sql.append(")");
			insertSql = sql.toString();
			insertMap.put(bean.getTable(), insertSql);
		}
		return insertSql;
	}
	
	/**
	 * 取得执行update时候update sql语句
	 * @param cls
	 * @param table
	 * @return
	 * @throws CarpException 
	 */
	public String getUpdateSql(Class<?> clazz) throws CarpException{
		CarpBean bean = BeansFactory.getBean(clazz);
		if(bean == null)
			throw new CarpException("Class:"+clazz+" has not a assiosite mapping Table!");
		String updateSql = updateMap.get(bean.getTable());
		if(updateSql == null){
			List<ColumnsMetadata> cms = bean.getColumns();
			List<PrimarysMetadata> pks = bean.getPrimarys();
			StringBuilder sql = new StringBuilder("UPDATE ");
			this.appendSchema(bean,sql);
			sql.append(bean.getTable()).append(" SET ");
			for(int i = 0, count = cms.size(); i < count; ++i){
				if(i!=0)
					sql.append(",");
				sql.append(cms.get(i).getColName());
				sql.append("=?");
			}
			sql.append(" WHERE ");
			for(int i = 0, count = pks.size(); i < count; ++i){
				if(i!=0)
					sql.append(" AND ");
				sql.append(pks.get(i).getColName());
				sql.append("=?");
			}
			updateSql = sql.toString();
			updateMap.put(bean.getTable(), updateSql);
		}
		return updateSql;
	}
	
	/**
	 * 取得执行update的delete语句,根据主键删除的delete语句
	 * @throws CarpException 
	 */
	public String getDeleteSql(Class<?> clazz) throws CarpException{
		CarpBean bean = BeansFactory.getBean(clazz);
		if(bean == null)
			throw new CarpException("Class:"+clazz+" has not a assiosite mapping Table!");
		String deleteSql = deleteMap.get(bean.getTable());
		if(deleteSql == null){
			List<PrimarysMetadata> pks = bean.getPrimarys();
			StringBuilder sql = new StringBuilder("DELETE FROM ");
			this.appendSchema(bean,sql);
			sql.append(bean.getTable()).append(" WHERE ");
			for(int i = 0, count = pks.size(); i < count; ++i){
				if(i!=0)
					sql.append(" AND ");
				sql.append(pks.get(i).getColName());
				sql.append(" = ?");
			}
			deleteSql =sql.toString();
			deleteMap.put(bean.getTable(), deleteSql);
		}
		return deleteSql;
	}
	
	/**
	 * generate columns list.
	 * @param builder
	 * @param tableAlias
	 */
	protected void appendColumens(CarpBean bean, StringBuilder builder, String tableAlias){
		for(ColumnsMetadata cm : bean.getColumns()){
			if(tableAlias != null)
				builder.append(tableAlias).append(".");
			builder.append(cm.getColName()).append(",");
		}
		builder.deleteCharAt(builder.length()-1);
	}
	
	/**
	 * 附加schema名字到sql语句中
	 * @param sql
	 * @return
	 * @throws CarpException 
	 */
	protected void appendSchema(CarpBean bean,StringBuilder sql) throws CarpException{
		if(bean.getSchema() != null)
			sql.append(bean.getSchema()).append(".");
		else if(this.schema != null)
			sql.append(this.schema).append(".");
	}
}
