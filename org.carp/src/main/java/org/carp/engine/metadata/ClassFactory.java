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
package org.carp.engine.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.carp.exception.CarpException;
/**
 * Query Class Parser Factory
 * Parse and cache the fields and methods of class
 * @author zhoubin
 *
 */
public class ClassFactory {
	private static Map<String,ClassMetadata> clzMap = new HashMap<>();
	
	/**
	 * Get the metadata information of the class, such as field method
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static ClassMetadata getClassMetadata(Class<?> clazz)throws Exception{
		if(clazz == null)
			throw new CarpException("Parameter clazz is null. clazz:"+clazz);
		ClassMetadata meta = clzMap.get(clazz.getName());
		if(meta == null){
			meta = new ClassMetadata(clazz);
			parserField(meta,clazz.getDeclaredFields());
			parserMethod(meta, clazz.getDeclaredMethods());
			clzMap.put(clazz.getName(), meta);
		}
		return meta;
	}
	
	/**
	 * Parse the fields of class
	 * @param meta
	 * @param fields
	 * @throws Exception
	 */
	private static void parserField(ClassMetadata meta,Field[] fields)throws Exception{
		for(Field f : fields){
			if(!Modifier.isStatic(f.getModifiers())){
				meta.putField(f.getName(), f);
			}
		}
	}
	/**
	 * Parse the methods of class
	 * @param meta
	 * @param methods
	 * @throws Exception
	 */
	private static void parserMethod(ClassMetadata meta,Method[] methods)throws Exception{
		for(Method m : methods){
			if(!Modifier.isStatic(m.getModifiers())){
				meta.putMethod(m.getName(), m);
			}
		}
	}
	 
}
