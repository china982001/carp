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
 * 映射注解（Mapping annotation），用于注释关联类的名称field
 * <li>foreignkey</li>
 * <li>mapCls</li>
 * @author zhou
 * @since 0.1
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Mapping{
	/**
	 * 本注解域的本类中的外键field名称，该外键与映射类的主键field相互对应
	 */
	String foreignkey();
	/**
	 * 本注解域所对应的映射类的映射field名称。
	 */
	String masterField();
	/**
	 * 本注解field所对应的映射类名称
	 * @return
	 */
	Class<?> mapCls();
	/**
	 * 在生成该pojo类的查询sql是，是否也把该Field添加到select列表中，关联查询。
	 * @return
	 */
	boolean relation() default false;
}
