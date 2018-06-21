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
import org.carp.beans.CarpBean;
import org.carp.beans.OTMMetadata;
import org.carp.beans.OTOMetadata;
import org.carp.exception.CarpException;
import org.carp.factory.BeansFactory;
import org.carp.impl.CarpSessionImpl;

/**
 * 级联删除操作类
 * @author zhou
 * @since 0.1
 */
public class DeleteCascade implements Cascade{
	private CarpSessionImpl _session;
	private CarpBean _bean;
//	private Object _data;
	private Object _key;
	public DeleteCascade(CarpSessionImpl session, CarpBean bean, Object data, Object key){
		this._session = session;
		this._bean = bean;
//		this._data = data;
		this._key = key;
	}
	
	/**
	 * 字典级联操作
	 * @throws CarpException
	 */
	public Cascade cascadeDICOperator() throws CarpException{
		return this;
	}
	
	/**
	 * 一对多级联操作
	 * @throws CarpException
	 * @throws SQLException
	 */
	public Cascade cascadeOTMOperator() throws CarpException, SQLException{
		List<OTMMetadata> otms = _bean.getOtms();
		if(otms != null){
			for(OTMMetadata otm : otms){
				if(isCascadeDelete(otm.getCascade())){
					CarpBean childBean = BeansFactory.getBean(otm.getChildClass());
					String csql = "select * from "+childBean.getTable()+" where "+otm.getFkey()+" = ?";
					CarpQuery query = _session.createQuery(csql,otm.getChildClass());
					if(_bean.getPrimarys().get(0).getFieldType().equals(String.class)){
						query.setString(1, _key.toString());
					}else{
						query.setLong(1, new Long(_key.toString()));
					}
					List<?> childs = query.list();
					if(childs !=null && !childs.isEmpty())
						for(Object obj : childs){
							_session.delete(obj);
						}
				}
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
		if(otos != null && !otos.isEmpty())
			for(OTOMetadata oto : otos){
				if(isCascadeDelete(oto.getCascade())){
					_session.delete(oto.getFieldType(), (java.io.Serializable)_key);
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
	
	private boolean isCascadeDelete(org.carp.annotation.CarpAnnotation.Cascade cascade){
		return cascade == org.carp.annotation.CarpAnnotation.Cascade.All
			|| cascade == org.carp.annotation.CarpAnnotation.Cascade.Delete;
	}
}
