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
package org.carp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 实体对象工具类 本类提供了对实体的操作方法
 * 
 * @author zhou
 * @since 0.1
 */
public class EntityUtil {
 
	/**
	 * 将数据表的列名称转换成类的field名称
	 * 
	 * @param colname
	 *            列名
	 * @return field名
	 */
	public static String getFieldName(String colname) {
		String[] cols = colname.toLowerCase().split("_");
		String field = "";
		for (String s : cols) {
			if (s.trim().equals(""))
				continue;
			if (field.equals(""))
				field = s;
			else {
				if (s.length() == 1)
					field += s.toUpperCase();
				else
					field += s.substring(0, 1).toUpperCase() + s.substring(1);
			}
		}
		return field;
	}

	/**
	 * Gets the value of the specified field for the given object
	 * 
	 * @param obj
	 * @param f
	 * @return field value
	 */
	public static Object getValue(Object obj, Field f) {
		Object o = null;
		try {
			boolean b = f.isAccessible();
			f.setAccessible(true);
			o = f.get(obj);
			f.setAccessible(b);
		} catch (Exception e) {
		}
		return o;
	}

	public static Field getField(Class<?> cls, String name) {
		Field f = null;
		try {
			f = cls.getDeclaredField(name);
		} catch (Exception ex) {
		}
		return f;
	}

	public static void setValue(Object obj, Object value, String name, Class<?> parameterType) {
		try {
			Method m = getMethod(obj.getClass(), getSetter(name), new Class<?>[] { parameterType });
			if (value == null)
				m.invoke(obj, new Object[] { value });
			else if (parameterType.equals(int.class))
				m.invoke(obj, new Object[] { new Integer(value + "").intValue() });
			else if (parameterType.equals(Integer.class))
				m.invoke(obj, new Object[] { new Integer(value + "") });
			else if (parameterType.equals(long.class))
				m.invoke(obj, new Object[] { new Long(value + "").longValue() });
			else if (parameterType.equals(Long.class))
				m.invoke(obj, new Object[] { new Long(value + "") });
			else if (parameterType.equals(short.class))
				m.invoke(obj, new Object[] { new Short(value + "").shortValue() });
			else if (parameterType.equals(Short.class))
				m.invoke(obj, new Object[] { new Short(value + "") });
			else if (parameterType.equals(byte.class))
				m.invoke(obj, new Object[] { new Byte(value + "").byteValue() });
			else if (parameterType.equals(Byte.class))
				m.invoke(obj, new Object[] { new Byte(value + "") });
			else if (parameterType.equals(String.class))
				m.invoke(obj, new Object[] { value + "" });
			else if (parameterType.equals(float.class))
				m.invoke(obj, new Object[] { new Float(value + "").floatValue() });
			else if (parameterType.equals(Float.class))
				m.invoke(obj, new Object[] { new Float(value + "") });
			else if (parameterType.equals(Double.class))
				m.invoke(obj, new Object[] { new Double(value + "") });
			else if (parameterType.equals(double.class))
				m.invoke(obj, new Object[] { new Double(value + "").doubleValue() });
			else
				m.invoke(obj, new Object[] { value });
		} catch (Exception e) {
		}
	}

	public static Method getMethod(Class<?> cls, String name, Class<?>[] parameterTypes) {
		try {
			return cls.getMethod(name, parameterTypes);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getSetter(String name) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return "set" + name;
	}
}
