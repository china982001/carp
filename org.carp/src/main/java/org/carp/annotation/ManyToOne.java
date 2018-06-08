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
 * 用于注解多对一的关联映射，注解为pojo类型field
 * @author zhou
 * @since 0.1
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface ManyToOne {
	/**
	 * 外键字段的column名称,即一方在多方的字段名称，暂未使用
	 * @return
	 */
	String column() default "";//字段名称
	/**
	 * 外键字段的field名称，即一方在多方的field名称
	 * @return
	 */
	String foreignField();
	/**
	 * 注解说明
	 * @
	 */
	String remark()default "";//字段说明
	/**
	 * 是否延迟加载数据,暂未使用
	 */
	boolean	lazy() default false;
}
