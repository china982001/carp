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
 * @category
 * table	数据库表<br>
 * schema   Schema名<br>
 * remark	表 - 类 的描述 
 * @author Z.Bin
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface Table {
	/**
	 * 数据表名
	 * @return
	 */
	String name();//表名
	/**
	 * 数据库的schema名称
	 * @return
	 */
	String schema() default "";//数据库Schema
	/**
	 * 数据库表的注释
	 * @return
	 */
	String remark() default "";//说明
}
