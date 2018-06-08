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
package org.carp.intercept;

import org.carp.impl.CarpSessionImpl;

/**
 * 拦截器接口类
 *
 * @author zhou
 * @since 0.1
 */
public interface Interceptor {
	
	/**
	 * 保存前事件
	 * @param entity 待序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onBeforeSave(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 保存后事件
	 * @param entity 序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onAfterSave(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 装载前事件
	 * @param entity 待序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onBeforeLoad(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 装载后事件
	 * @param entity 序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onAfterLoad(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 更新前事件
	 * @param entity 待序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onBeforeUpdate(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 更新后事件
	 * @param entity 序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onAfterUpdate(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 删除前事件
	 * @param entity 待序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onBeforeDelete(Object entity,CarpSessionImpl session)throws Exception;
	
	/**
	 * 删除后事件
	 * @param entity 序列化对象
	 * @param session 数据库连接对象
	 * @throws Exception
	 */
	public void onAfterDelete(Object entity,CarpSessionImpl session)throws Exception;
}
