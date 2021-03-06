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
package org.carp.assemble;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * Assign the table column value to the property value of the corresponding object 
 * 将数据表的字段值设置为对应的对象属性值
 * @author Zhou
 *
 */
public interface Assemble {	
	
	/**
	 * 设置实体的Field值
	 * @param rs 查询结果集
	 * @param entity  实体对象
	 * @param f  对象的属性域
	 * @param index 结果集字段索引，从当前记录行中，需要读取的字段索引
	 * @return  读取的字段值
	 * @throws Exception
	 */
	Object setFieldValue(ResultSet rs, Object entity,Field f,int index) throws Exception;
	/**
	 * 设置实体对象的field值 
	 * @param entity 实体对象
	 * @param f 实体属性对象
	 * @param value 被设置的field值
	 * @return field值
	 * @throws Exception
	 */
	Object setFieldValue(Object entity,Field f,Object value) throws Exception;
	
	/**
	 * set method value 
	 * @param rs ResultSet Object， getValue by index from rs
	 * @param entity  Assigned entity object
	 * @param m  Assigned entity method
	 * @param index column index of ResultSet
	 * @return
	 * @throws Exception
	 */
	Object setMethodValue(ResultSet rs, Object entity, Method m, int index)throws Exception;
}
