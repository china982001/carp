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
package org.carp.intercept;

import org.carp.impl.CarpSessionImpl;

/**
 * 默认拦截器实现类，没有添加任何功能
 * @author zhou
 * @since 0.1
 */
public class DefaultInterceptor implements Interceptor{
	
	public void onBeforeSave(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onAfterSave(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onBeforeLoad(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onAfterLoad(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onBeforeUpdate(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onAfterUpdate(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onBeforeDelete(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	public void onAfterDelete(Object entity,CarpSessionImpl session)throws Exception{
		
	}
	
}
