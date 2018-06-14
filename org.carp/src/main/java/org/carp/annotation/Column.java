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
package org.carp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * field annotation
 * Class domain annotation properties for annotation fields.
 * @author zhou
 * @since 0.1
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Column{
	/**
	 * column'name,used to corresponding data table field name
	 */
	String name();
	
	/** 
	 * The value of the field is allowed to null, the default value is Nullable.Yes
	 */
	boolean Null() default true;
	/** 
	 * 字段说明,对应的数据表字段的注释 
	 */
	String remark()default "";//字段说明
	/** 
	 * 数据长度，如果是浮点型，那么该值为整数部分的长度 
	 */
	int length() default 0;//长度
	/** 
	 * 精度。仅对浮点数有效，为小数位的长度 
	 * */
	int precision() default 0;//精度
	
	/**
	 * The sqltype type of this field, which tells carp a precise carp type.
	 * whose value definition refers to java.sql.Types
	 * @return default 0
	 */
	int jdbcType() default 0; 
}
