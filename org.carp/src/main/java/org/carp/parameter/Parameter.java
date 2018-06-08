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
package org.carp.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询参数集合类
 * 用于暂存需要用于查询条件的sql参数，待执行SQL语句前，将其注入到PreparedStatement对象中
 * @author zhou
 *
 */
public final class Parameter {
	private List<Param> params = new ArrayList<Param>(5);
	
	/**
	 * 设置查询参数
	 * @param key  参数索引值，从1开始， 1,2,3,4，。。。。
	 * @param value 参数值
	 * @param cls  参数值类型
	 */
	public void setParameter(Integer index,Object value,Class<?> cls){
		Param p = new Param(index,value,cls);
		params.add(p);
	}
	
	public List<Param> getParamList(){
		return params;
	}
	
	public void clear(){
		params.clear();
	}
	public int count(){
		return params.size();//valueMap.size();
	}
	
	public class Param{
		Integer index;
		Object value;
		Class<?> cls;
		public Param(Integer idx,Object value, Class<?> cls){
			this.index = idx;
			this.value = value;
			this.cls = cls;
		}
		public Integer getIndex() {
			return index;
		}
		public Object getValue() {
			return value;
		}
		public Class<?> getCls() {
			return cls;
		}
	}
}
