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
package org.carp.engine.exec;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.carp.engine.ParametersProcessor;
import org.carp.engine.metadata.MetaData;
import org.carp.engine.metadata.QueryMetaData;
import org.carp.engine.result.ResultSetProcessor;
import org.carp.exception.CarpException;
import org.carp.impl.CarpQueryImpl;
import org.carp.impl.DataSetImpl;
import org.carp.parameter.OUTParameter;
import org.carp.parameter.Parameter;
import org.carp.parameter.Parameter.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The execution class of a stored procedure. 
 * Implementation of parameter injection, out parameter registration, return value acquisition, result set and other acquisition functions
 * @author zhou
 * @version 0.3
 */
public class ProcedureExecutor {
	private static final Logger logger = LoggerFactory.getLogger(ProcedureExecutor.class);
	CarpQueryImpl _query;
	CallableStatement statement;
	List<Object> rsList = new ArrayList<>(2);
	boolean bool = false;
	public ProcedureExecutor(CarpQueryImpl query){
		this._query = query;
	}
	
	/**
	 * Execute stored procedure
	 * @throws Exception
	 */
	public void executeStatement()throws Exception{
		createStatement();
		processParameters();
		bool = statement.execute();
		processOutParams();
		processResultSet();
		this._query.setStatement(statement);
	}
	
	/**
	 * Parse the result set and convert the result set to an object collection or Dataset
	 * @throws Exception
	 */
	private void processResultSet()throws Exception{
		int len = 0;
		logger.debug("There are [ {} ] ResultSet that need to be converted into object collection",this._query.getClazzes().length);
		logger.debug("The number of objects that will be converted to dataset is unknown.");
		while(bool){//exec suce， has resultset.
			ResultSet rs = statement.getResultSet();
			if(this._query.getClazzes().length > len){
				logger.debug("Turning the [{}] ResultSet into an object collection of class: {}",len,this._query.getClazzes()[len]);
				try{
					QueryMetaData  metadata = new QueryMetaData(this._query.getClazzes()[len],rs);
					ResultSetProcessor process = new ResultSetProcessor(this._query.getClazzes()[len],metadata,rs);
					rsList.add(process.list());
				}catch(Exception ex){
					throw new CarpException("Result set metadata does not match class field, resolution fails.",ex);
				}
			}else{
				logger.debug("Converting the [{}] ResultSet to a Dataset object",len);
				MetaData meta = new MetaData(rs);
				DataSetImpl ds = new DataSetImpl(this._query,meta,rs);
				rsList.add(ds);
			}
			++len;
			rs.close();
			bool = statement.getMoreResults();// has more ResultSet.
		}
	}
	
	/**
	 * Processing of out type parameter values
	 * @throws Exception
	 */
	private void processOutParams()throws Exception{
		Parameter params = this._query.getParameters();
		List<Param> list = params.getOUTParamList();
		logger.debug("getting store procedure OUTParameter....");
		OUTParameter outp = this._query.getOutParameter();
		for(Param p : list){
			logger.debug("ParameterIndex: {} , sqlType: {}", p.getIndex(), p.getSqlType());
			switch(p.getSqlType()){
				case Types.VARCHAR:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getString(p.getIndex())); break;
				}
				case Types.BIGINT:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getLong(p.getIndex())); break;
				}
				case Types.BINARY:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBytes(p.getIndex())); break;
				}
				case Types.BIT:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBoolean(p.getIndex())); break;
				}
				case Types.BLOB:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBytes(p.getIndex())); break;
				}
				case Types.CHAR:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getString(p.getIndex())); break;
				}
				case Types.CLOB:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getString(p.getIndex())); break;
				}
				case Types.DATE:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getDate(p.getIndex())); break;
				}
				case Types.DECIMAL:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBigDecimal(p.getIndex())); break;
				}
				case Types.DOUBLE:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getDouble(p.getIndex())); break;
				}
				case Types.FLOAT:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getDouble(p.getIndex())); break;
				}
				case Types.INTEGER:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getInt(p.getIndex())); break;
				}
				case Types.JAVA_OBJECT:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getObject(p.getIndex())); break;
				}
				case Types.LONGVARBINARY:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBytes(p.getIndex())); break;
				}
				case Types.LONGVARCHAR:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getString(p.getIndex())); break;
				}
				case Types.NUMERIC:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBigDecimal(p.getIndex())); break;
				}
				case Types.OTHER:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getObject(p.getIndex())); break;
				}
				case Types.REAL:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getFloat(p.getIndex())); break;
				}
				case Types.SMALLINT:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getShort(p.getIndex())); break;
				}
				case Types.TIME:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getTime(p.getIndex())); break;
				}
				case Types.TIMESTAMP:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getTimestamp(p.getIndex())); break;
				}
				case Types.TINYINT:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getByte(p.getIndex())); break;
				}
				case Types.VARBINARY:{
					outp.setOutParameterVlaue(p.getIndex(), statement.getBytes(p.getIndex())); break;
				}
				default:outp.setOutParameterVlaue(p.getIndex(), statement.getObject(p.getIndex()));
			}
		}
	}
	/**
	 * create CallableStatement object.
	 * @throws Exception
	 */
	private void createStatement() throws Exception{
		statement = this._query.getSession().getConnection().prepareCall(this._query.getSql());
	}
	
	/**
	 * Setting injection stored procedure parameters
	 * @throws Exception
	 */
	private void processParameters() throws Exception{
		Parameter params = this._query.getParameters();
		List<Param> inlist = params.getParamList();
		ParametersProcessor paramProcessor = new ParametersProcessor(statement);
		logger.debug("Setting store procedure in parameter....");
		for(Param p : inlist){
			logger.debug("ParameterIndex: {} , Value: {} , paramType: {}", p.getIndex(),p.getValue(),p.getCls());
			paramProcessor.setStatementParameters(p.getValue(), p.getCls(), p.getIndex());
		}
		List<Param> outlist = params.getOUTParamList();
		logger.debug("Registering store procedure out parameter....");
		for(Param p : outlist){
			logger.debug("ParameterIndex: {} , sqlType: {}", p.getIndex(),p.getSqlType());
			this.statement.registerOutParameter(p.getIndex(), p.getSqlType());
		}
	}

	/**
	 * get resultset, maybe from 0 to more
	 * @return
	 */
	public List<Object> getRSObjectList() {
		return rsList;
	}
}
