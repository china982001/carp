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
package org.carp.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carp.assemble.Assemble;
import org.carp.beans.CarpBean;
import org.carp.beans.PrimarysMetadata;
import org.carp.engine.event.DeleteEvent;
import org.carp.engine.event.Event;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;
import org.carp.type.TypeMapping;

/**
 * 事件工厂类
 * @author zhou
 * @since 0.1
 */
public class CarpEventFactory {
	
	/**
	 * 创建删除事件对象
	 * @param session
	 * @param cls
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Event	deleteEvent(CarpSessionImpl session,Class<?> cls,Object entity) throws Exception{
		Object obj = null;
		if(cls != null){
			CarpBean bean = BeansFactory.getBean(cls);
			try {
				obj = cls.newInstance();
			} catch (Exception e) {
				throw new CarpException("Could not instance Class: "+cls.getName());
			}
			if(entity instanceof HashMap){
				List<PrimarysMetadata> pms = bean.getPrimarys();
				for(PrimarysMetadata pm : pms){
					Map<String,Object> map = (Map<String,Object>)entity;
					Assemble assemble = TypeMapping.getAssembleByFieldType(pm.getFieldType());
					assemble.setFieldValue(obj, pm.getField(), map.get(pm.getColName()));
				}
			}else{
				PrimarysMetadata pm = bean.getPrimarys().get(0);
				Assemble assemble = TypeMapping.getAssembleByFieldType(pm.getFieldType());
				assemble.setFieldValue(obj, pm.getField(), entity);
			}
		}else
			obj = entity;
		Event event = new DeleteEvent(session,obj);
		return event;
	}
	
	
}
