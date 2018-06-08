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
import java.util.Map;

import org.carp.annotation.PojoParser;
import org.carp.beans.BeansProcess;
import org.carp.beans.CarpBean;
import org.carp.exception.CarpException;
/**
 * JavaBean工厂类
 * 创建pojo类对应的Bean管理对象。
 * @author zhou
 * @since 0.1
 */
public class BeansFactory {
	private static Map<Class<?>,CarpBean> clsMap = new HashMap<Class<?>,CarpBean>();
	
	/**
	 * 获取Bean对象
	 * @param cls pojo class
	 * @return CarpBean对象
	 * @throws CarpException
	 */
	public static CarpBean getBean(Class<?> cls) throws CarpException{
		if(cls == null)
			throw new CarpException("Parameter: cls is null");
		CarpBean bean = clsMap.get(cls);
		if(bean==null){
			if(!isParserBean(cls)) ////true 解析   false 不解析
				return null;
			bean = BeansProcess.parser(cls);
			if(bean.getTable().equals("")){
				throw new CarpException("Class:"+cls+" has not a assiosite mapping Table!");
			}
			clsMap.put(cls, bean);
		}
		return bean;
	}
	
	/**
	 * 是否解析javabean
	 * @param cls
	 * @return true 解析   false 不解析
	 */
	private static boolean isParserBean(Class<?> cls){
		PojoParser parser = cls.getAnnotation(PojoParser.class);
		if(parser == null) return true; //解析
		return parser.parser(); //true 解析   false 不解析
	}
}
