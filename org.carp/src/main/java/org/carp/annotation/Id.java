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

import org.carp.annotation.CarpAnnotation.Generate;
import org.carp.id.Generator;

/**
 * 主键字段注解类
 * <br/>
 * <b>用于对数据表主键字段对应的pojo类的属性进行注解，标识该属性为主键属性</b>
 * <table>
 * 	<tr>
 * 		<td>name</td>
 * 		<td>数据库column名称</td>
 * 	</tr>
 * 	<tr>
 * 		<td>build</td>
 * 		<td>主键生成方式，可选设置 Enum Build(assigned,sequences,auto,custom),默认为 Build.assigned</td>
 * 	</tr>
 * 	<tr>
 * 		<td>builder</td>
 * 		<td>主键生成器类，默认接口，如果需要实现自定义的主键生成方式，需要实现该接口</td>
 * 	</tr>
 * 	<tr>
 * 		<td>sequence</td>
 * 		<td>序列名，当主键生成方式build为sequences时，序列名发生作用，需要设置sequence的值</td>
 * 	</tr>
 * 	<tr>
 * 		<td>remark</td>
 * 		<td>Field - Column 描述信息</td>
 * 	</tr>
 * </table>
 * @author zhou
 * @since 0.1
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Id {
	/**
	 * 字段名称，所在表的主键列
	 * @return
	 */
	String name(); //对应数据库字段名
	/**
	 * 该主键列的列值的生成方式
	 * @return
	 */
	Generate build() default Generate.assigned;//主键生成器生成方式
	/**
	 * 主键值的生成类，仅仅在build为Generate.sequences的时候起作用
	 * @return
	 */
	Class<? extends Generator> builder() default Generator.class;//主键生成器类
	/**
	 * 序列名称
	 * 当主键生成方式build为sequences时，序列名称发生作用，需要设置sequence的值
	 * @return
	 */
	String sequence() default "";//当主键生成方式build为sequences时，序列名称发生作用，需要设置sequence的值。
	/**
	 * 备注信息
	 * @return
	 */
	String remark() default ""; //字段说明
	
}
