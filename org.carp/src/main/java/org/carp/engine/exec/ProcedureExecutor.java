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
import java.util.ArrayList;
import java.util.List;

import org.carp.engine.ParametersProcessor;
import org.carp.engine.helper.QuerySqlHelper;
import org.carp.engine.helper.SqlHelper;
import org.carp.engine.metadata.MetaData;
import org.carp.engine.metadata.QueryMetaData;
import org.carp.engine.result.RSProcessor;
import org.carp.engine.statement.CarpStatement;
import org.carp.impl.CarpQueryImpl;
import org.carp.impl.DataSetImpl;
import org.carp.parameter.OUTParameter;
import org.carp.parameter.Parameter;
import org.carp.parameter.Parameter.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StoreProceduce Execute Class
 * @author zhou
 * @since 0.3
 */
public class ProcedureExecutor extends Executor{
	private static final Logger logger = LoggerFactory.getLogger(ProcedureExecutor.class);
	private boolean bool = false;
	private List<Object> list = new ArrayList<>();
	public ProcedureExecutor(CarpQueryImpl query) throws Exception{
		super(query);
	}

	@Override
	protected void executeStatement() throws Exception {
		this.bool = this.getQuery().getStatement().execute();
	}


	@Override
	protected void process() throws Exception {
		SqlHelper helper = new QuerySqlHelper(this.getQuery());
		helper.buildSql();
		helper.showSql();
	}
	public void executeProcedure()throws Exception{
		new CarpStatement(this.getQuery()).createProcedureStatement(); //创建Statement对象
		//设置Statement参数
		processParameters();//new SQLParameter(this.getQuery()).processSQLParameters();
		executeStatement();//执行Statement
		processOutParameters();
		processResultSets();
	}

	public List<Object> getRSObjectList() throws Exception{
		return this.list;
	}
	
	private void processResultSets()throws Exception{
		int idx = 0;
		while(this.bool){
			try(ResultSet rs = this.getQuery().getStatement().getResultSet();){
				if( idx < this.getQuery().getClazzes().length){
					QueryMetaData metadata = new QueryMetaData(this.getQuery().getClazzes()[idx],rs);
					RSProcessor processor = new RSProcessor(this.getQuery().getClazzes()[idx],metadata,rs);
					list.add(processor.list());
				}else{
					MetaData metadata = new MetaData(rs);
					list.add(new DataSetImpl(this.getQuery(), metadata, rs));
				}
				idx++;
				this.bool = this.getQuery().getStatement().getMoreResults();
			}
		}
	}
	
	/**
	 * Setting injection stored procedure parameters
	 * @throws Exception
	 */
	private void processParameters() throws Exception{
		Parameter params = this.getQuery().getParameters();
		List<Param> inlist = params.getParamList();
		ParametersProcessor paramProcessor = new ParametersProcessor(this.getQuery().getStatement());
		logger.debug("Setting store procedure in parameter....");
		for(Param p : inlist){
			logger.debug("ParameterIndex: {} , Value: {} , paramType: {}", p.getIndex(),p.getValue(),p.getCls());
			paramProcessor.setStatementParameters(p.getValue(), p.getCls(), p.getIndex());
		}
		List<Param> outlist = params.getOUTParamList();
		logger.debug("Registering store procedure out parameter....");
		for(Param p : outlist){
			logger.debug("ParameterIndex: {} , sqlType: {}", p.getIndex(),p.getSqlType());
			((CallableStatement)this.getQuery().getStatement()).registerOutParameter(p.getIndex(), p.getSqlType());
		}
	}
	
	private void processOutParameters()throws Exception{
		Parameter params = this.getQuery().getParameters();
		List<Param> outlist = params.getOUTParamList();
		logger.debug("getting store procedure OUTParameter....");
		OUTParameter outp = this.getQuery().getOutParameter();
		CallableStatement statement = (CallableStatement)this.getQuery().getStatement();
		for(Param p : outlist){
			outp.setOutParameterVlaue(p.getIndex(), statement.getObject(p.getIndex()));
		}
	}
	
	@Override
	protected void parserMetaData() throws Exception {
		
	}
}
