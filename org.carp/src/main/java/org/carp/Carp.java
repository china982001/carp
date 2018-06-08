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
package org.carp;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;

import org.carp.type.CarpBlobImpl;
import org.carp.type.CarpClobImpl;

/**
 * 实用类，进行数据之间的格式转换等功能。
 * 
 * @author zhou
 * @version 0.1
 */
public class Carp {
	public final static int ENTITY_POJO = 0;
	public final static int ENTITY_MAP = 1;
  
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static Blob createBlob(String s) {
		try {
			return new CarpBlobImpl(s.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Blob createBlob(byte[] b) {
		try {
			return new CarpBlobImpl(b);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Blob createBlob(InputStream ins) {
		try {
			return new CarpBlobImpl(ins);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Clob createClob(String s) {
		try {
			return new CarpClobImpl(s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Clob createClob(Reader reader) {
		try {
			return new CarpClobImpl(reader);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static java.sql.Date dateFromUtilToSql(java.util.Date date) {
		if (date == null)
			return null;
		else
			return new java.sql.Date(date.getTime());
	}

	public static java.util.Date dateFromSqlToUtil(java.sql.Date d) {
		if (d == null)
			return null;
		else
			return new java.util.Date(d.getTime());
	}

	public static java.util.Date dateFromSqlToUtil(java.sql.Time d) {
		if (d == null)
			return null;
		else
			return new java.util.Date(d.getTime());
	}

	public static java.util.Date dateFromSqlToUtil(java.sql.Timestamp d) {
		if (d == null)
			return null;
		else
			return new java.util.Date(d.getTime());
	}

	public static String getDate(java.util.Date d, String pattern) {
		if (d == null)
			return "";
		return new java.text.SimpleDateFormat(pattern).format(d);
	}
}
