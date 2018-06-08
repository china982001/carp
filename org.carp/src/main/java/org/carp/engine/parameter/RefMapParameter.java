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
package org.carp.engine.parameter;

import java.sql.PreparedStatement;

public class RefMapParameter implements Parameter{

	public void setValue(PreparedStatement ps, Object value, int index) throws Exception {
		java.sql.Ref i = (java.sql.Ref)value;
		ps.setRef(index, i);
	}

}
