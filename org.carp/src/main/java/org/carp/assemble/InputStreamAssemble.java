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
package org.carp.assemble;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

public class InputStreamAssemble extends AbstractAssemble {

	public void setValue(ResultSet rs, List<Object> data, int index)
			throws Exception {
		InputStream value = getValue(rs.getBinaryStream(index));
		data.add(value);
	}

	@Override
	public void setValue(ResultSet rs, List<Object> data, String colname)
			throws Exception {
		InputStream value = getValue(rs.getBinaryStream(colname));
		data.add(value);
	}
	
	private InputStream getValue(InputStream ins)throws Exception{
		InputStream stream = ins;
		if (ins != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[2048];
			for (int len = -1; (len = ins.read(b, 0, 2048)) != -1;)
				baos.write(b, 0, len);
			stream = new java.io.ByteArrayInputStream(baos.toByteArray());
			ins.close();
		}
		return stream;
	}

	public Object setFieldValue(ResultSet rs, Object entity, Field f, int index)
			throws Exception {
		InputStream value = getValue(rs.getBinaryStream(index));
		this.setFieldValue(entity, f, value);
		return value;
	}

	@Override
	public Object setFieldValue(ResultSet rs, Object entity, Field f,
			String colname) throws Exception {
		InputStream value = getValue(rs.getBinaryStream(colname));
		this.setFieldValue(entity, f, value);
		return value;
	}

}
