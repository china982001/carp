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
package org.carp.engine.event;

import java.sql.Blob;
import java.sql.Clob;
import java.util.List;

import org.carp.beans.ColumnsMetadata;
import org.carp.engine.ParametersProcessor;
import org.carp.engine.cascade.Cascade;
import org.carp.engine.cascade.UpdateCascade;
import org.carp.exception.CarpException;
import org.carp.impl.CarpSessionImpl;
import org.carp.sql.OracleCarpSql;

public class UpdateEvent extends Event {
//	private static final Logger logger = LoggerFactory.getLogger(UpdateEvent.class);
	public UpdateEvent(CarpSessionImpl session,Object entity) throws CarpException{
		super(session,entity,"update");
	}
	

	/**
	 * 处理Statement参数、包括主键值
	 */
	@Override
	public void processStatmentParameters(ParametersProcessor psProcess)throws Exception {
		this.processFieldValues(psProcess);
		this.processPrimaryValues(psProcess);
	}

	/**
	 * 级联操作
	 */
	@Override
	public void cascadeAfterOperator() throws Exception {
		Object key = this.getBean().getPrimaryValue(getEntity());//主键值
		Cascade cascade = new UpdateCascade(this.getSession(),this.getBean(),this.getEntity(),key);
		cascade.cascadeOTMOperator().cascadeOTOOperator();
	}
	

	@Override
	public void cascadeBeforeOperator() throws Exception {
		
	}

	@Override
	protected void executeBefore() throws Exception {
		this.processBlob();
		if(this.getSession().getJdbcContext().getContext().getConfig().getInterceptor() != null)
			this.getSession().getJdbcContext().getContext().getConfig().getInterceptor().onBeforeUpdate(getEntity(), getSession());
		if(this.getSession().getInterceptor() != null)
			this.getSession().getInterceptor().onBeforeUpdate(getEntity(), getSession());
	}

	@Override
	protected void executeAfter() throws Exception {
		if(this.getSession().getJdbcContext().getContext().getConfig().getInterceptor() != null)
			this.getSession().getJdbcContext().getContext().getConfig().getInterceptor().onAfterUpdate(getEntity(), getSession());
		if(this.getSession().getInterceptor() != null)
			this.getSession().getInterceptor().onAfterUpdate(getEntity(), getSession());
	}
	
	/**
	 * 如果数据库为oracle的情况下，单独处理blob和clob类型的字段
	 * @throws Exception
	 */
	private void processBlob()throws Exception{
		if(!this.getSession().getJdbcContext().getContext().dialectClass().equals(OracleCarpSql.class))
			return;
		List<ColumnsMetadata>  cols = this.getBean().getColumns();
		for(ColumnsMetadata col : cols){
			if(col.getFieldType().equals(Blob.class)){
				Object value = col.getValue(this.getEntity());
				if(value == null)
					continue;
				Blob blob = (Blob)value;
				byte[] bytes = blob.getBytes(1, (int)blob.length());
				blob = this.getSession().getConnection().createBlob();
				blob.setBytes(1, bytes);
				col.setValue(this.getEntity(), blob);
			}else if(col.getFieldType().equals(Clob.class)){
				Object value = col.getValue(this.getEntity());
				if(value == null)
					continue;
				Clob clob = (Clob)value;
				String tmp = clob.getSubString(1, (int)clob.length());
				clob = this.getSession().getConnection().createClob();
				clob.setString(1, tmp);
				col.setValue(this.getEntity(), clob);
			}
		}
	}
	
	@Override
	protected boolean validate() throws Exception {
		List<ColumnsMetadata> cols = this.getBean().getColumns();
		for(int i = 0, count = cols.size(); i<count; ++i){
			ColumnsMetadata m = cols.get(i);
			Object value = m.getValue(this.getEntity());
			if(!m.isNull() && value == null)
				throw new CarpException("Field: "+m.getFieldName()+" could not was NULL.");
			if(m.getLength() != 0 && value.toString().length() > m.getLength())
				throw new CarpException("The length of field '"+m.getFieldName()+"' value: "+ value.toString().length() +" > "+m.getLength());
		}
		return true;
	}
}
