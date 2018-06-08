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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Clob 接口实现类
 * @author Administrator
 *
 */
public class CarpClobImpl implements Clob {

	private Reader reader;
	private int length;
	private boolean needsReset = false;

	public CarpClobImpl(String string) {
		reader = new StringReader(string);
		length = string.length();
	}

	public CarpClobImpl(Reader reader, int length) {
		this.reader = reader;
		this.length = length;
	}
	
	public CarpClobImpl(Reader reader) {
		if(reader == null)
			return;
		this.length = 0;
		java.io.StringWriter writer = new StringWriter();
		char[] c = new char[2048];
		try{
			for(int len = -1; (len = reader.read(c, 0, 2048)) != -1;){
				this.length += len;
				writer.write(c, 0, len);
			}
			this.reader = new StringReader(writer.toString());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @see java.sql.Clob#length()
	 */
	public long length() throws SQLException {
		return length;
	}

	/**
	 * @see java.sql.Clob#truncate(long)
	 */
	public void truncate(long pos) throws SQLException {
		excep();
	}

	/**
	 * @see java.sql.Clob#getAsciiStream()
	 */
	public InputStream getAsciiStream() throws SQLException {
		try {
			if (needsReset) reader.reset();
		}
		catch (IOException ioe) {
			throw new SQLException("could not reset reader");
		}
		needsReset = true;
		return new ReaderInputStream(reader);
	}

	public class ReaderInputStream extends InputStream{
		private Reader read;
		public ReaderInputStream(Reader reader){
			this.read = reader;
		}
		@Override
		public int read() throws IOException {
			return read.read();
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
		try {
			if (needsReset) reader.reset();
		}
		catch (IOException ioe) {
			throw new SQLException("could not reset reader");
		}
		needsReset = true;
		return reader;
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
		excep(); return null;
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
	public int setString(long pos, String string, int i, int j)
	throws SQLException {
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
		// TODO Auto-generated method stub
		
	}
	/**
	 * @see java.sql.Clob#getCharacterStream(long, long)
	 */
	public Reader getCharacterStream(long pos, long length) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}






