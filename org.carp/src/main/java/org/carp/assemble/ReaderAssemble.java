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

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

public class ReaderAssemble extends AbstractAssemble{
	
	public void setValue(ResultSet rs, List<Object> data, int index)
	throws Exception {
		Reader reader = getReader(rs.getCharacterStream(index));
		data.add(reader);
	}

	@Override
	public void setValue(ResultSet rs, List<Object> data, String colname)
			throws Exception {
		Reader reader = getReader(rs.getCharacterStream(colname));
		data.add(reader);
	}

	private Reader getReader(Reader reader)throws Exception{
		Reader stream = reader;
		if(reader != null){
			StringWriter writer = new StringWriter();
			char[] buf = new char[2048];
			for(int len = -1; (len = reader.read(buf, 0, 2048)) != -1;){
				writer.write(buf, 0, len);
			}
			stream = new StringReader(writer.toString());
			reader.close();
		}
		return stream;
	}
	public Object setFieldValue(ResultSet rs, Object entity, Field f, int index)
			throws Exception {
		Reader reader = getReader(rs.getCharacterStream(index));
		this.setFieldValue(entity, f, reader);
		return reader;
	}

	@Override
	public Object setFieldValue(ResultSet rs, Object entity, Field f,
			String colname) throws Exception {
		Reader reader = getReader(rs.getCharacterStream(colname));
		this.setFieldValue(entity, f, reader);
		return reader;
	}
}
