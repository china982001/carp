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
package org.carp.type;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.carp.assemble.ArrayAssemble;
import org.carp.assemble.Assemble;
import org.carp.assemble.BTByteAssemble;
import org.carp.assemble.BTDoubleAssemble;
import org.carp.assemble.BTFloatAssemble;
import org.carp.assemble.BTIntegerAssemble;
import org.carp.assemble.BTLongAssemble;
import org.carp.assemble.BTShortAssemble;
import org.carp.assemble.BigDecimalAssemble;
import org.carp.assemble.BlobAssemble;
import org.carp.assemble.BooleanAssemble;
import org.carp.assemble.ByteAssemble;
import org.carp.assemble.BytesAssemble;
import org.carp.assemble.ClobAssemble;
import org.carp.assemble.DoubleAssemble;
import org.carp.assemble.FloatAssemble;
import org.carp.assemble.IntegerAssemble;
import org.carp.assemble.LongAssemble;
import org.carp.assemble.ObjectAssemble;
import org.carp.assemble.RefAssemble;
import org.carp.assemble.SQLDateAssemble;
import org.carp.assemble.ShortAssemble;
import org.carp.assemble.StringAssemble;
import org.carp.assemble.StructAssemble;
import org.carp.assemble.TimeAssemble;
import org.carp.assemble.TimestampAssemble;
import org.carp.assemble.URLAssemble;
import org.carp.engine.parameter.ArrayMapParameter;
import org.carp.engine.parameter.ArrayParameter;
import org.carp.engine.parameter.BlobMapParameter;
import org.carp.engine.parameter.BlobParameter;
import org.carp.engine.parameter.BooleanMapParameter;
import org.carp.engine.parameter.BooleanParameter;
import org.carp.engine.parameter.ByteMapParameter;
import org.carp.engine.parameter.ByteParameter;
import org.carp.engine.parameter.BytesMapParameter;
import org.carp.engine.parameter.BytesParameter;
import org.carp.engine.parameter.ClobMapParameter;
import org.carp.engine.parameter.ClobParameter;
import org.carp.engine.parameter.DateMapParameter;
import org.carp.engine.parameter.DateParameter;
import org.carp.engine.parameter.DecimalMapParameter;
import org.carp.engine.parameter.DecimalParameter;
import org.carp.engine.parameter.DoubleMapParameter;
import org.carp.engine.parameter.DoubleParameter;
import org.carp.engine.parameter.FloatMapParameter;
import org.carp.engine.parameter.FloatParameter;
import org.carp.engine.parameter.InputStreamParameter;
import org.carp.engine.parameter.IntegerMapParameter;
import org.carp.engine.parameter.IntegerParameter;
import org.carp.engine.parameter.LongMapParameter;
import org.carp.engine.parameter.LongParameter;
import org.carp.engine.parameter.ObjectMapParameter;
import org.carp.engine.parameter.ObjectParameter;
import org.carp.engine.parameter.Parameter;
import org.carp.engine.parameter.ReaderParameter;
import org.carp.engine.parameter.RefMapParameter;
import org.carp.engine.parameter.RefParameter;
import org.carp.engine.parameter.ShortMapParameter;
import org.carp.engine.parameter.ShortParameter;
import org.carp.engine.parameter.SqlDateParameter;
import org.carp.engine.parameter.StringMapParameter;
import org.carp.engine.parameter.StringParameter;
import org.carp.engine.parameter.StructParameter;
import org.carp.engine.parameter.TimeMapParameter;
import org.carp.engine.parameter.TimeParameter;
import org.carp.engine.parameter.TimestampMapParameter;
import org.carp.engine.parameter.TimestampParameter;

public class TypeMapping {
	private final static Map<Class<?>,Assemble> fieldMap = new HashMap<Class<?>,Assemble>(30); 
	private final static Map<Class<?>,Parameter> javaTypeMap = new HashMap<Class<?>,Parameter>(30); 
	private final static Map<Integer,Parameter> sqlTypeMap = new HashMap<Integer,Parameter>(30);
	private final static Map<Integer,Class<?>> sqlJavaMap = new HashMap<Integer,Class<?>>();
	private final static Map<Integer,Assemble> sqlToAsMap = new HashMap<Integer,Assemble>();
	
	
	static{ 
		//field映射
		fieldMap.put(int.class, new BTIntegerAssemble());
		fieldMap.put(Integer.class,new IntegerAssemble());
		fieldMap.put(byte.class,new BTByteAssemble());
		fieldMap.put(Byte.class, new ByteAssemble());
		fieldMap.put(byte[].class, new BytesAssemble());
		fieldMap.put(short.class, new BTShortAssemble());
		fieldMap.put(Short.class, new ShortAssemble());
		fieldMap.put(long.class, new BTLongAssemble());
		fieldMap.put(Long.class, new LongAssemble());
		fieldMap.put(Blob.class, new BlobAssemble());
		fieldMap.put(Clob.class, new ClobAssemble());
		fieldMap.put(String.class, new StringAssemble());
		fieldMap.put(java.sql.Date.class, new SQLDateAssemble());
		fieldMap.put(java.util.Date.class, new TimestampAssemble());
		fieldMap.put(Time.class, new TimeAssemble());
		fieldMap.put(Timestamp.class, new TimestampAssemble());
		fieldMap.put(float.class, new BTFloatAssemble());
		fieldMap.put(Float.class, new FloatAssemble());
		fieldMap.put(Double.class, new DoubleAssemble());
		fieldMap.put(double.class, new BTDoubleAssemble());
		fieldMap.put(BigDecimal.class, new BigDecimalAssemble());
		fieldMap.put(boolean.class, new BooleanAssemble());
		fieldMap.put(Boolean.class, new BooleanAssemble());
		fieldMap.put(Object.class, new ObjectAssemble());
		fieldMap.put(Ref.class, new RefAssemble());
		fieldMap.put(Array.class, new ArrayAssemble());
		fieldMap.put(java.sql.Struct.class, new StructAssemble());
		fieldMap.put(java.net.URL.class, new URLAssemble());
		
		//java class to PreparedStatement Parameter mapping
		javaTypeMap.put(int.class, new IntegerParameter());
		javaTypeMap.put(Integer.class, new IntegerParameter());
		javaTypeMap.put(byte.class, new ByteParameter());
		javaTypeMap.put(Byte.class, new ByteParameter());
		javaTypeMap.put(byte[].class, new BytesParameter());
		javaTypeMap.put(short.class, new ShortParameter());
		javaTypeMap.put(Short.class, new ShortParameter());
		javaTypeMap.put(long.class, new LongParameter());
		javaTypeMap.put(Long.class, new LongParameter());
		javaTypeMap.put(float.class, new FloatParameter());
		javaTypeMap.put(Float.class, new FloatParameter());
		javaTypeMap.put(Double.class, new DoubleParameter());
		javaTypeMap.put(double.class, new DoubleParameter());
		javaTypeMap.put(BigDecimal.class, new DecimalParameter());
		javaTypeMap.put(boolean.class, new BooleanParameter());
		javaTypeMap.put(Boolean.class, new BooleanParameter());
		javaTypeMap.put(Blob.class, new BlobParameter());
		javaTypeMap.put(Clob.class, new ClobParameter());
		javaTypeMap.put(String.class, new StringParameter());
		javaTypeMap.put(Date.class, new DateParameter());
		javaTypeMap.put(java.sql.Date.class, new SqlDateParameter());
		javaTypeMap.put(Time.class, new TimeParameter());
		javaTypeMap.put(Timestamp.class, new TimestampParameter());
		javaTypeMap.put(Object.class, new ObjectParameter());
		javaTypeMap.put(InputStream.class, new InputStreamParameter());
		javaTypeMap.put(FileInputStream.class, new InputStreamParameter());
		javaTypeMap.put(Reader.class, new ReaderParameter());
		javaTypeMap.put(FileReader.class, new ReaderParameter());
		javaTypeMap.put(Ref.class, new RefParameter());
		javaTypeMap.put(Array.class, new ArrayParameter());
		javaTypeMap.put(java.sql.Struct.class, new StructParameter());
		
		//sql types to PreparedStatement Parameter mapping,For the map collection to insert data using
		sqlTypeMap.put(java.sql.Types.ARRAY, new ArrayMapParameter());
		sqlTypeMap.put(java.sql.Types.BIGINT, new LongMapParameter());
		sqlTypeMap.put(java.sql.Types.BINARY, new BytesMapParameter());
		sqlTypeMap.put(java.sql.Types.BLOB, new BlobMapParameter());
		sqlTypeMap.put(java.sql.Types.BOOLEAN, new BooleanMapParameter());
		sqlTypeMap.put(java.sql.Types.BIT, new BooleanMapParameter());
		sqlTypeMap.put(java.sql.Types.CHAR, new StringMapParameter());
		sqlTypeMap.put(java.sql.Types.CLOB, new ClobMapParameter());
		sqlTypeMap.put(java.sql.Types.DATE, new DateMapParameter());
		sqlTypeMap.put(java.sql.Types.DECIMAL, new DecimalMapParameter());
		sqlTypeMap.put(java.sql.Types.DOUBLE, new DoubleMapParameter());
		sqlTypeMap.put(java.sql.Types.FLOAT, new FloatMapParameter());
		sqlTypeMap.put(java.sql.Types.INTEGER, new IntegerMapParameter());
		sqlTypeMap.put(java.sql.Types.JAVA_OBJECT, new ObjectMapParameter());
		sqlTypeMap.put(java.sql.Types.LONGVARBINARY, new BytesMapParameter());
		sqlTypeMap.put(java.sql.Types.LONGVARCHAR, new StringMapParameter());
		sqlTypeMap.put(java.sql.Types.NUMERIC, new DecimalMapParameter());
		sqlTypeMap.put(java.sql.Types.REAL, new FloatMapParameter());
		sqlTypeMap.put(java.sql.Types.REF, new RefMapParameter());
		sqlTypeMap.put(java.sql.Types.SMALLINT, new ShortMapParameter());
		sqlTypeMap.put(java.sql.Types.TIME, new TimeMapParameter());
		sqlTypeMap.put(java.sql.Types.TIMESTAMP, new TimestampMapParameter());
		sqlTypeMap.put(java.sql.Types.TINYINT, new ByteMapParameter());
		sqlTypeMap.put(java.sql.Types.VARBINARY, new BytesMapParameter());
		sqlTypeMap.put(java.sql.Types.VARCHAR, new StringMapParameter());
		
		//sql types to java class mapping
		sqlJavaMap.put(Types.BIT, Boolean.class);
		sqlJavaMap.put(Types.BIGINT, Long.class);
		sqlJavaMap.put(Types.BINARY, InputStream.class);
		sqlJavaMap.put(Types.BLOB, InputStream.class);
		sqlJavaMap.put(Types.BOOLEAN, Boolean.class);
		sqlJavaMap.put(Types.CHAR, String.class);
		sqlJavaMap.put(Types.CLOB, Reader.class);
		sqlJavaMap.put(Types.DATE, Date.class);
		sqlJavaMap.put(Types.DECIMAL, BigDecimal.class);
		sqlJavaMap.put(Types.DOUBLE, Double.class);
		sqlJavaMap.put(Types.FLOAT, Float.class);
		sqlJavaMap.put(Types.INTEGER, Integer.class);
		sqlJavaMap.put(Types.JAVA_OBJECT, Object.class);
		sqlJavaMap.put(Types.LONGVARBINARY, InputStream.class);
		sqlJavaMap.put(Types.LONGVARCHAR, InputStream.class);
		sqlJavaMap.put(Types.NUMERIC, BigDecimal.class);
		sqlJavaMap.put(Types.OTHER, Object.class);
		sqlJavaMap.put(Types.REAL, Double.class);
		sqlJavaMap.put(Types.REF, Ref.class);
		sqlJavaMap.put(Types.SMALLINT, Short.class);
		sqlJavaMap.put(Types.STRUCT, Enum.class);
		sqlJavaMap.put(Types.TIME, Time.class);
		sqlJavaMap.put(Types.TIMESTAMP, Timestamp.class);
		sqlJavaMap.put(Types.TINYINT, Byte.class);
		sqlJavaMap.put(Types.VARBINARY, InputStream.class);
		sqlJavaMap.put(Types.VARCHAR, String.class);
		
		//sql types to Assemble mapping
		sqlToAsMap.put(Types.BIT, fieldMap.get(Boolean.class));
		sqlToAsMap.put(Types.BIGINT, fieldMap.get(Long.class));
		sqlToAsMap.put(Types.BINARY, fieldMap.get(byte[].class));
		sqlToAsMap.put(Types.BLOB, fieldMap.get(byte[].class));
		sqlToAsMap.put(Types.BOOLEAN, fieldMap.get(Boolean.class));
		sqlToAsMap.put(Types.CHAR, fieldMap.get(String.class));
		sqlToAsMap.put(Types.CLOB, fieldMap.get(String.class));
		sqlToAsMap.put(Types.DATE, fieldMap.get(Date.class));
		sqlToAsMap.put(Types.DECIMAL, fieldMap.get(BigDecimal.class));
		sqlToAsMap.put(Types.DOUBLE, fieldMap.get(Double.class));
		sqlToAsMap.put(Types.FLOAT, fieldMap.get(Double.class));
		sqlToAsMap.put(Types.INTEGER, fieldMap.get(Integer.class));
		sqlToAsMap.put(Types.JAVA_OBJECT, fieldMap.get(Object.class));
		sqlToAsMap.put(Types.LONGVARBINARY, fieldMap.get(byte[].class));
		sqlToAsMap.put(Types.LONGVARCHAR, fieldMap.get(String.class));
		sqlToAsMap.put(Types.NUMERIC, fieldMap.get(BigDecimal.class));
		sqlToAsMap.put(Types.OTHER, fieldMap.get(Object.class));
		sqlToAsMap.put(Types.REAL, fieldMap.get(Float.class));
		sqlToAsMap.put(Types.REF, fieldMap.get(Ref.class));
		sqlToAsMap.put(Types.SMALLINT, fieldMap.get(Short.class));
		sqlToAsMap.put(Types.STRUCT, fieldMap.get(Object.class));
		sqlToAsMap.put(Types.TIME, fieldMap.get(Time.class));
		sqlToAsMap.put(Types.TIMESTAMP, fieldMap.get(Timestamp.class));
		sqlToAsMap.put(Types.TINYINT, fieldMap.get(Byte.class));
		sqlToAsMap.put(Types.VARBINARY, fieldMap.get(byte[].class));
		sqlToAsMap.put(Types.VARCHAR, fieldMap.get(String.class));
	}
	
	public static Assemble getAssembleByFieldType(Class<?> cls){
		return fieldMap.get(cls);
	}
	
	public static Parameter getParameterByFieldType(Class<?> cls){
		return javaTypeMap.get(cls);
	}
	
	public static Parameter getParameterBySqlType(Integer sqlType){
		return sqlTypeMap.get(sqlType);
	}

	public static Class<?> getJavaTypeBySqlType(int sqlType){
		return sqlJavaMap.get(sqlType);
	}
	
	public static Assemble getAssembleBySqlType(int sqlType){
		return sqlToAsMap.get(sqlType);
	}
}
