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
package org.carp.id;

import java.io.Serializable;
import java.sql.ResultSet;

import org.carp.assemble.Assemble;
import org.carp.beans.PrimarysMetadata;
import org.carp.impl.CarpSessionImpl;
import org.carp.type.TypeMapping;

/**
 * 主键自动生成类
 * @author zhou
 * @since 0.1
 */
public class AutoGenerator  implements Generator{

	/**
	 * @see
	 */
	public Serializable generate(CarpSessionImpl session,Object entity,PrimarysMetadata pm)throws Exception {
		Object key = null;
		try(ResultSet rs = session.getStatement().getGeneratedKeys();){
			while(rs.next()){
				Assemble assemble = TypeMapping.getAssembleByFieldType(pm.getFieldType());// (Assemble)TypeMapping.getAssembleClass(pm.getFieldType()).newInstance();
				key = assemble.setFieldValue(rs, entity, pm.getField(), 1);
			}
		}
		return (java.io.Serializable)key;
	}

}
