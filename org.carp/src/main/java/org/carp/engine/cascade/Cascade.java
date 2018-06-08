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

import org.carp.exception.CarpException;
/**
 * 对象的级联操作
 * @author zhou
 * @since 0.2
 */
public interface Cascade {
	/**
	 * 字典级联操作
	 * @throws CarpException
	 */
	public Cascade cascadeDICOperator() throws Exception;
	
	/**
	 * 一对多级联操作
	 * @throws CarpException
	 * @throws SQLException
	 */
	public Cascade cascadeOTMOperator() throws Exception;
	
	/**
	 * 一对一级联操作
	 * @throws CarpException
	 */
	public Cascade cascadeOTOOperator() throws Exception;
	
	/**
	 * 多对一级联操作
	 */
	public Cascade cascadeMTOOperator()throws Exception;
}
