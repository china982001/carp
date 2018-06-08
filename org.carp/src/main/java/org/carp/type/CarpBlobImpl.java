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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * blob接口实现类
 * @author zhou
 */
public class CarpBlobImpl implements Blob {

	private InputStream stream;
	private int length;
	private boolean needsReset = false;

	public CarpBlobImpl(byte[] bytes) {
		this.stream = new ByteArrayInputStream(bytes);
		this.length = bytes.length;
	}

	public CarpBlobImpl(InputStream stream, int length) {
		this.stream = stream;
		this.length = length;
	}
	
	public CarpBlobImpl(InputStream stream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.length = 0;
		try{
			if(stream == null)
				return;
			byte[] b = new byte[4096];
			for(int len = -1; (len = stream.read(b, 0, 4096)) != -1;){
				this.length += len;
				baos.write(b, 0, len);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.stream = new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * @see java.sql.Blob#length()
	 */
	public long length() throws SQLException {
		return length;
	}

	/**
	 * @see java.sql.Blob#truncate(long)
	 */
	public void truncate(long pos) throws SQLException {
	}

	/**
	 * @see java.sql.Blob#getBytes(long, int)
	 */
	public byte[] getBytes(long pos, int len) throws SQLException {
		byte[] b = new byte[len-(int)pos];
		try {
			this.stream.read(b, (int)pos, len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * @see java.sql.Blob#setBytes(long, byte[])
	 */
	public int setBytes(long pos, byte[] bytes) throws SQLException {
		return 0;
	}

	/**
	 * @see java.sql.Blob#setBytes(long, byte[], int, int)
	 */
	public int setBytes(long pos, byte[] bytes, int i, int j)
	throws SQLException {
		return 0;
	}

	/**
	 * @see java.sql.Blob#position(byte[], long)
	 */
	public long position(byte[] bytes, long pos) throws SQLException {
		return 0;
	}

	/**
	 * @see java.sql.Blob#getBinaryStream()
	 */
	public InputStream getBinaryStream() throws SQLException {
		try {
			if (needsReset) stream.reset();
		}
		catch (IOException ioe) {
			throw new SQLException("could not reset reader");
		}
		needsReset = true;
		return stream;
	}

	/**
	 * @see java.sql.Blob#setBinaryStream(long)
	 */
	public OutputStream setBinaryStream(long pos) throws SQLException {
		return null;
	}

	/**
	 * @see java.sql.Blob#position(Blob, long)
	 */
	public long position(Blob blob, long pos) throws SQLException {
		return 0;
	}
	/**
	 * @see java.sql.Blob#free()
	 */
	public void free() throws SQLException {
		this.stream = null;
		this.length = 0;
	}

	/**
	 * @see java.sql.Blob#getBinaryStream(long,long)
	 */
	public InputStream getBinaryStream(long pos, long length)
			throws SQLException {
		return this.stream;
	}

}






