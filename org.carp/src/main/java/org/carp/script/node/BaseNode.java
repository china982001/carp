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
package org.carp.script.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.carp.script.SQLParam;
import org.mvel2.MVEL;
import org.w3c.dom.Node;

/**
 * The underlying definition class for the SQL fragment, which all sql definition nodes inherit
 * @author zhou
 * @version 0.3
 */
public abstract class BaseNode implements Cloneable{
	public List<BaseNode> childNodes = new ArrayList<BaseNode>(2);
	private boolean condition = false; //Verify that the condition is valid
	
	public void putChildNode(BaseNode node){
		childNodes.add(node);
	}
	
	/**
	 * Parsing sql nodes to generate sql statements
	 * @param buffer  sql buffer
	 * @param paramMap  parameter HashMap
	 * @param values    parameter values
	 * @param context   OgnlContext For parameter storage and expression evaluation
	 * @return
	 * @throws Exception
	 */
	public abstract String parser(Map<String,Object> paramMap, List<Object> values)throws Exception;
	
	/**
	 * expression evaluation，Verify that the condition is valid
	 * @param param  parameter values
	 * @return  true/false
	 * @throws Exception
	 */
	public abstract boolean verifyCondition(Map<String,Object> param)throws Exception;
	
	/**
	 * Parse the sql node, initializing the node property value
	 * @param node
	 */
	public abstract void initValue(Node node)throws Exception;

	/**
	 * Gets the value from the parameter collection based on the paramname.
	 * The corresponding value is obtained from the parameter set according to paramname, 
	 * and the SQLParam object is generated for parsing the dynamic condition of the sql statement.
	 * @param paramname
	 * @param params   Map<String,Object>
	 * @return
	 * @throws Exception
	 */
	protected SQLParam getParam(String paramname, Map<String,Object> params) throws Exception{
		Object value= MVEL.eval(paramname.trim(), params);
		SQLParam p = new SQLParam();
		p.setValue(value);
		p.setName(paramname);
		if(value != null)
			p.setClz(value.getClass());
		return p;
	}
	
	/**
	 * get node attributeValue by attributename.
	 * if attributeValue == null, return defaultValue
	 * @param node
	 * @param attrName
	 * @param defaultValue
	 * @return
	 */
	protected String getAttrValue(Node node,String attrName,String defaultValue){
		Node attrNode = node.getAttributes().getNamedItem(attrName);
		if(attrNode != null){
			return attrNode.getNodeValue().trim();
		}
		return defaultValue;
	}
	/**
	 * Clone a node object for the parsing of the current SQL statement.
	 */
	@Override
	public BaseNode clone() throws CloneNotSupportedException {
		BaseNode baseNode = (BaseNode) super.clone();
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.clone());
		}
		baseNode.childNodes = childs;
		return baseNode;
	}
	
	/**
	 * Clone a node object for the parsing of the current SQL statement.
	 * @return
	 */
	public abstract BaseNode onClone();

	public boolean isConditionValiad() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}
}
