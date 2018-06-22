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

import org.carp.beans.CarpBean;
import org.carp.beans.OTMMetadata;
import org.carp.beans.OTOMetadata;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;
import org.carp.util.EntityUtil;

/**
 * 级联更新操作类
 * @author zhou
 * @since 0.2
 */
public class UpdateCascade implements Cascade{
	private CarpSessionImpl _session;
	private CarpBean _bean;
	private Object _data;
	private Object _key; 
	public UpdateCascade(CarpSessionImpl session, CarpBean bean, Object entity, Object key){
		this._session = session;
		this._bean = bean;
		this._data = entity;
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
	public Cascade cascadeOTMOperator() throws Exception{
		List<OTMMetadata> otms = _bean.getOtms();
		for(OTMMetadata otm : otms){
			if(this.isCascadeUpdate(otm.getCascade())){
				java.util.Collection<?> collection = (java.util.Collection<?>)otm.getValue(_data);
				if(collection !=null && !collection.isEmpty()){
					for(Object childObject : collection){
						EntityUtil.setFieldValue(childObject,EntityUtil.getField(childObject.getClass(), otm.getForeignName()),_key);
//						Class<?> cls = EntityUtil.getField(childObject.getClass(), otm.getForeignName()).getType();
//						EntityUtil.setValue(childObject, _key, otm.getForeignName(), cls);
						_session.update(childObject);
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
		for(OTOMetadata oto : otos){
			if(this.isCascadeUpdate(oto.getCascade())){
				Object obj = oto.getValue(_data);
				if(obj != null)
					_session.update(obj);
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
	private boolean isCascadeUpdate(org.carp.annotation.CarpAnnotation.Cascade _cas){
		return _cas == org.carp.annotation.CarpAnnotation.Cascade.All
			|| _cas == org.carp.annotation.CarpAnnotation.Cascade.Update
			|| _cas == org.carp.annotation.CarpAnnotation.Cascade.SaveUpdate;
	}
}
