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

import org.carp.exception.CarpException;
import org.carp.script.node.BaseNode;
import org.carp.script.node.NodeFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser SQL configuration file.
 * @author zhoubin
 *
 */
public class SqlFileParser {
	private Document sqldoc;
	public SqlFileParser(Document doc){
		this.sqldoc = doc;
	}
	
	/**
	 * parser sql defined
	 * @throws CarpException 
	 */
	public void process() throws CarpException{
		NodeList nodes = sqldoc.getElementsByTagName("module");
		for(int idx = 0, count = nodes.getLength(); idx < count; ++idx){
			Node n = nodes.item(idx);
			String moduleName = n.getAttributes().getNamedItem("name").getNodeValue();
			NodeList childs = n.getChildNodes();
			for(int i=0;i<childs.getLength(); ++i){
				if(!isValidNode(childs.item(i)))
					continue;
				if("sql".equals(childs.item(i).getNodeName())){
					String sqlname = childs.item(i).getAttributes().getNamedItem("name").getNodeValue();
					NodeList sqls = childs.item(i).getChildNodes();
					SQLNode sqlNode = new SQLNode(moduleName+"/"+sqlname);
					for(int j=0;j<sqls.getLength();++j){
						if(!isValidNode(sqls.item(j)))
							continue;
						Node sql = sqls.item(j);
						BaseNode sqlPartNode= NodeFactory.getNode(sql.getNodeName());
						sqlPartNode.initValue(sql);
						sqlNode.putNode(sqlPartNode);
						parserSqlPartNode(sqlPartNode,sql);
					}
					SQLFactory.putSQLNode(sqlNode.getSqlName(), sqlNode);
				}
			}
		}
	}
	
	
	private void parserSqlPartNode(BaseNode sqlPartNode,Node node) throws CarpException{
		NodeList childs = node.getChildNodes();
		//System.out.println(node.getNodeValue()+ "= "+childs.getLength());
		for(int i=0; i<childs.getLength(); ++i){
			if(!isValidNode(childs.item(i)))
				continue;
			BaseNode _baseNode = NodeFactory.getNode(childs.item(i).getNodeName());
			_baseNode.initValue(childs.item(i));
			sqlPartNode.putChildNode(_baseNode);
			parserSqlPartNode(_baseNode,childs.item(i));
			//System.out.println(childs.item(i).getNodeName());
		}
	}
	
	/**
	 * Valid sql node fragment
	 * @param node
	 * @return
	 */
	private boolean isValidNode(Node node){
		int nodeType = node.getNodeType();
		if(nodeType == 1)
			return true;
		if(nodeType == 3){
			String value = node.getNodeValue();
			if(value != null && value.trim().length() > 0){
				return true;
			}
		}
		return false;
	}
}
