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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Clob 接口实现类
 * @author Administrator
 *
 */
public class CarpClobImpl implements Clob {

	private String _data = ""; //default empty string， length be 0

	public CarpClobImpl(final String data) throws SQLException {
		if(data == null)
			throw new SQLException("Argument be null. data:"+data);
		_data = data;
	}

	/**
	 * @see java.sql.Clob#length()
	 */
	public long length() throws SQLException {
		return this._data.length();
	}

	/**
	 * @see java.sql.Clob#truncate(long)
	 */
	public void truncate(long pos) throws SQLException {
		if(pos == this._data.length())
			return;
		if(pos < 0 || pos > this._data.length())
			throw new SQLException("OutOfRangeArgument. pos: " +pos);
		this._data = this._data.substring(0, (int)pos);
	}

	/**
	 * @see java.sql.Clob#getAsciiStream()
	 */
	public InputStream getAsciiStream() throws SQLException {
		try {
			return new ByteArrayInputStream(_data.getBytes("US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * @see java.sql.Clob#setAsciiStream(long)
	 */
	public OutputStream setAsciiStream(long pos) throws SQLException {
		excep(); return null;
	}

	/**
	 * @see java.sql.Clob#getCharacterStream()
	 */
	public Reader getCharacterStream() throws SQLException {
		return new StringReader(this._data);
	}

	/**
	 * @see java.sql.Clob#setCharacterStream(long)
	 */
	public Writer setCharacterStream(long pos) throws SQLException {
		excep(); return null;
	}

	/**
	 * @see java.sql.Clob#getSubString(long, int)
	 */
	public String getSubString(long pos, int len) throws SQLException {
		if(pos == 1 && len == this._data.length())
			return this._data;
		if(pos < 1 || pos > this._data.length())
			throw new SQLException("OutOfRangeArgument. pos: " +pos);
		int idx = (int)pos - 1;  //The start index of clob is 1 more than that of string
		if(len < 0 || len >_data.length() -idx)
			throw new SQLException("OutOfRangeArgument. len: "+len);
		return this._data.substring(idx, idx+len);
	}

	/**
	 * @see java.sql.Clob#setString(long, String)
	 */
	public int setString(long pos, String string) throws SQLException {
		excep(); return 0;
	}

	/**
	 * @see java.sql.Clob#setString(long, String, int, int)
	 */
	public int setString(long pos, String string, int i, int j) throws SQLException {
		excep(); return 0;
	}

	/**
	 * @see java.sql.Clob#position(String, long)
	 */
	public long position(String string, long pos) throws SQLException {
		excep(); return 0;
	}

	/**
	 * @see java.sql.Clob#position(Clob, long)
	 */
	public long position(Clob colb, long pos) throws SQLException {
		excep(); return 0;
	}


	private static void excep() {
		throw new UnsupportedOperationException("Blob may not be manipulated from creating session");
	}
	/**
	 * @see java.sql.Clob#free()
	 */
	public void free() throws SQLException {
		this._data = null;
	}
	/**
	 * @see java.sql.Clob#getCharacterStream(long, long)
	 */
	public Reader getCharacterStream(long pos, long length) throws SQLException {
		return new StringReader(this.getSubString(pos, (int)length));
	}

}






