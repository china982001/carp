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
package org.carp.engine.cascade;

import java.sql.SQLException;
import java.util.List;

import org.carp.CarpQuery;
import org.carp.annotation.CarpAnnotation;
import org.carp.beans.CarpBean;
import org.carp.beans.DICMetadata;
import org.carp.beans.OTMMetadata;
import org.carp.beans.OTOMetadata;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;

/**
 * 查找单个对象的级联操作类
 * @author zhou
 * @since 0.2
 */
public class FindCascade implements Cascade{
	private CarpSessionImpl _session;
	private CarpBean _bean;
	private Object _data;
	private Object _key;
	public FindCascade(CarpSessionImpl session, CarpBean bean, Object data, Object key){
		this._session = session;
		this._bean = bean;
		this._data = data;
		this._key = key;
	}
	
	/**
	 * 字典级联操作
	 * @throws CarpException
	 */
	public Cascade cascadeDICOperator() throws CarpException{
		List<DICMetadata> dics = _bean.getDics();
		for(DICMetadata dic : dics){
			dic.setValue(_data, _session.createQuery(dic.getSql(),dic.getDicClass()).list());
		}
		return this;
	}
	
	/**
	 * 一对多级联操作
	 * @throws CarpException
	 * @throws SQLException
	 */
	public Cascade cascadeOTMOperator() throws Exception{
		List<OTMMetadata> otms = _bean.getOtms();
		for(OTMMetadata otm : otms){
			if(otm.getCascade()== CarpAnnotation.Cascade.All || otm.getCascade()== CarpAnnotation.Cascade.Load){
				String sql = _session.getJdbcContext().getContext().getDialect().getQuerySql(otm.getChildClass());
				if(sql.toLowerCase().indexOf(" where ")>0)
					sql += " and "+_bean.getTable()+"_."+otm.getFkey()+" = ?";
				else
					sql += " where "+otm.getFkey()+" = ?";
				CarpQuery query = _session.createQuery(sql,otm.getChildClass());
				if(_bean.getPrimarys().get(0).getFieldType().equals(String.class))
					query.setString(1, _key.toString());
				else
					query.setLong(1, new Long(_key.toString()));
				otm.setValue(_data, query.list());
			}
		}
		return this;
	}
	
	/**
	 * 一对一级联操作
	 * @throws CarpException
	 */
	public Cascade cascadeOTOOperator() throws CarpException{
		List<OTOMetadata> otos = _bean.getOtos();
		for(OTOMetadata oto : otos){
			if(oto.getCascade()== CarpAnnotation.Cascade.All|| oto.getCascade()== CarpAnnotation.Cascade.Load){
				oto.setValue(_data, _session.get(oto.getFieldType(), _key.toString()));
			}
		}
		return this;
	}
	
	/**
	 * 多对一级联操作
	 */
	public Cascade cascadeMTOOperator(){
		return this;
	}
}
