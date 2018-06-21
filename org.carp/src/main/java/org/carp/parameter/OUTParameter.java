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
package org.carp.parameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The out type parameter return value definition class of stored procedure
 * @author zhou
 * @version 0.3
 */
public final class OUTParameter {
	private final Map<Integer,Object> params = new HashMap<>();
	
	/**
	 * set parameter value
	 * @param index  out parameter index
	 * @param value
	 */
	public void setOutParameterVlaue(int index, Object value){
		params.put(index, value);
	}
	
	public String getString(int index){
		Object v = params.get(index);
		if(v != null)
			return v+"";
		return null;
	}
	
	public long getLong(int index){
		Object v = params.get(index);
		return Long.parseLong(v+"");
	}
	
	public int getInt(int index){
		Object v = params.get(index);
		return Integer.parseInt(v+"");
	}
	
	public short getShort(int index){
		Object v = params.get(index);
		return Short.parseShort(v+"");
	}
	public byte getByte(int index){
		Object v = params.get(index);
		return Byte.parseByte(v+"");
	}
	public Boolean getBoolean(int index){
		Object v = params.get(index);
		return Boolean.parseBoolean(v+"");
	}
	public byte[] getByteArray(int index){
		Object v = params.get(index);
		return (byte[])v;
	}
	
	public Blob getBlob(int index){
		Object v = params.get(index);
		return (Blob)v;
	}
	public Clob getClob(int index){
		Object v = params.get(index);
		return (Clob)v;
	}

	public BigDecimal getBigDecimal(int index){
		Object v = params.get(index);
		return new BigDecimal(v+"");
	}
	public BigInteger getBigInteger(int index){
		Object v = params.get(index);
		return new BigInteger(v+"");
	}
	
	public Date getDate(int index){
		Object v = params.get(index);
		long time = ((java.sql.Date)v).getTime();
		return new Date(time);
	}
	
	public Timestamp getTimestamp(int index){
		Object v = params.get(index);
		return (Timestamp)v;
	}
	
	/**
	 * Parameter’s count
	 * @return
	 */
	public int count(){
		return this.params.size();
	}
	
	public void clear(){
		this.params.clear();
	}
}
