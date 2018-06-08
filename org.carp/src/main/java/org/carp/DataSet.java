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
package org.carp;

import java.util.List;
/**
 * 返回查询到的结果集数据。
 * 
 * @author zhou
 * @version 0.1
 */
public interface DataSet {
	/**
	 * 是否有下一条记录
	 * @return
	 */
	public boolean next();
	/**
	 * 根据字段名称，当前光标指向的行的对应的字段值。
	 * @param name 字段名称
	 * @return
	 */
	public Object getData(String name);
	/**
	 * 根据索引位置，当前光标指向的行的对应的字段值。
	 * @param name 字段名称
	 * @return
	 */
	public Object getData(int index);
	/**
	 * 返回查询到的数据集合
	 * @return
	 */
	public List<List<Object>> getData();
	/**
	 * 返回查询到的数据的title的名称列表。
	 * @return
	 */
	public List<String> getTitle();
	/**
	 * 从返回的结果集中，根据索引号，返回该索引指向的记录。
	 * @param index
	 * @return
	 */
	public List<Object> getRowData(int index) ;
	/**
	 * 返回查询到的结果集列的类型集合，按照seclect列表的顺序排列
	 * @return
	 */
	public List<Class<?>> getColumnType();
	/**
	 * 返回的记录条数
	 * @return
	 */
	public int count();
}
