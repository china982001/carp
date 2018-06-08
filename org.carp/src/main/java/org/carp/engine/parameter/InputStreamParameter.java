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

import java.io.InputStream;
import java.sql.PreparedStatement;

public class InputStreamParameter implements Parameter{

	public void setValue(PreparedStatement ps, Object value, int index) throws Exception {
		InputStream ips = (InputStream)value;
		if(ips == null){
			ps.setNull(index, java.sql.Types.LONGVARBINARY);
			return;
		}
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		byte[] b = new byte[4096];
		for(int len = -1; (len = ips.read(b, 0, 4096))!=-1; )
			baos.write(b, 0, len);
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(baos.toByteArray());
		ps.setBinaryStream(index, bais, baos.size());
	}

}
