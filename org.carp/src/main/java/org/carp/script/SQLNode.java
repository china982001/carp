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
package org.carp.script;

import java.util.ArrayList;
import java.util.List;

import org.carp.script.node.BaseNode;

public class SQLNode implements Cloneable{
	private List<BaseNode> nodes = new ArrayList<BaseNode>();
	private String module_sql_name;
	
	public SQLNode(String sqlname){
		this.module_sql_name = sqlname;
	}
	
	public void putNode(BaseNode _node){
		this.nodes.add(_node);
	}
	
	public List<BaseNode> getNodes() {
		return nodes;
	}
	public String getSqlName() {
		return module_sql_name;
	}

	@Override
	public SQLNode clone() throws CloneNotSupportedException {
		SQLNode sqlnode = (SQLNode)super.clone();
		List<BaseNode> list = new ArrayList<BaseNode>();
		for(BaseNode node : nodes){
			list.add(node.clone());
		}
		sqlnode.nodes = list;
		return sqlnode;
	}
	
	public SQLNode onClone(){
		SQLNode sqlnode = new SQLNode(this.module_sql_name);
		List<BaseNode> list = new ArrayList<BaseNode>();
		for(BaseNode node : nodes){
			list.add(node.onClone());
		}
		sqlnode.nodes = list;
		return sqlnode;
	}
}
