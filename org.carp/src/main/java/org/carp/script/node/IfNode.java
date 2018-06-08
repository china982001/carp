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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import ognl.OgnlContext;

/**
 * IF node definition class for dynamic SQL
 * When the expression in the ripening test is true, the processing of the child node is executed, or the child node of the node is ignored.
 * The test attribute is required attribute
 * For example: <if test='expression'>content</if>
 * @author zhou
 * @version 0.3
 */
public class IfNode extends BaseNode {
	private static final Logger logger = LoggerFactory.getLogger(IfNode.class);
	private String test;
	private boolean conditon = false;

	@Override
	public void initValue(Node node) {
		test =node.getAttributes().getNamedItem("test").getNodeValue().trim();
	}

	@Override
	public String parser(Map<String, Object> paramMap, List<Object> values,OgnlContext context) throws Exception{
		String content = "";
		if(conditon){// bool == true
			for(BaseNode node: this.childNodes){
				if(node.verifyCondition(paramMap))
					content += " " + node.parser(paramMap,values,context);
			}
		}
		return content;
	}

	@Override
	public boolean verifyCondition(Map<String, Object> paramMap) throws Exception {
		conditon = (Boolean)ognl.Ognl.getValue(test, paramMap);
		logger.debug("Express: "+test+" , condition: "+conditon);
		return conditon;
	}

	@Override
	public BaseNode onClone() {
		IfNode ifnode = new IfNode();
		ifnode.test = this.test;
		ifnode.conditon = this.conditon;
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.onClone());
		}
		ifnode.childNodes = childs;
		return ifnode;
	}
}
