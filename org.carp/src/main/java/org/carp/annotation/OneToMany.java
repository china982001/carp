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

import org.carp.annotation.CarpAnnotation.Cascade;

/**
 * 一对多关系的注解，用于注解类型为List的field域<br>
 * <li>foreignKey</li> 外键字段
 * <li>foreignName</li> 外键域名
 * <li>childClass</li> 子类名
 * <li>cascade</li> 级联操作
 * <li>lazy</li> 是否延迟加载，暂未支持
 * <li>remark</li> 备注
 * @author zhou
 * @category annotation
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface OneToMany {
	/**
	 * 外键字段名称
	 */
	String foreignKey() default "";//字段名称
	
	/**
	 * 外键字段对应的Field名称
	 */
	String foreignName();//字段名称
	/**
	 * 对应的子类
	 */
	Class<?> childClass();//对应的子类
	/**
	 * 级联操作，默认无级联操作。
	 */
	Cascade cascade() default Cascade.None;
	/**
	 * 延迟加载数据
	 * @return
	 */
	boolean	lazy() default false;
	/**
	 * 字段说明
	 * @return
	 */
	String remark() default "";
	
}
