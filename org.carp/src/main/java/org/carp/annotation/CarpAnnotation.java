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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * One Annotation Class
 * Use to define
 * 用于定义注解类的属性值常量
 * @author zhou
 * @since 0.1
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface CarpAnnotation{
	/**
	 * 数据级联操作
	 * delete	删除时级联
	 * save		保存时级联
	 * update	更新时级联
	 * saveupdate	保存及更新时级联
	 * load		加载某个对象时级联
	 * all		做任何操作均进行级联
	 * none		做任何操作都不进行级联
	 */
	public enum Cascade{
		Delete, Save, Update,
		SaveUpdate, Load, All, None
	}
	
	/**
	 * 主键生成方式<br>
	 * assigned   由程序负责生成，默认<br>
	 * sequences  根据序列生成<br>
	 * auto       自动增长，需要数据库支持<br>
	 * custom     自定义，需要实现主键生成器接口的主键生成器类<br>
	 */
	public enum Generate{assigned, sequences, auto, custom}
	
	/**
	 * 数据级联操作时，子数据的加载容器类型
	 */
	public enum Container{Set, List, Vector }
}
