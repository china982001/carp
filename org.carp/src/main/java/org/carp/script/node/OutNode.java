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

import org.carp.exception.CarpException;
import org.mvel2.MVEL;
import org.w3c.dom.Node;

public class OutNode extends BaseNode{
	private Object valueExp;
	private Object testExp;
	
	@Override
	public void initValue(Node node) throws Exception{
		String value = this.getAttrValue(node, "value", null);
		String test = this.getAttrValue(node, "test", null);
		if(value == null)
			throw new CarpException("assign tag: value cannot be null, But: [value="+value+"]");
		valueExp = MVEL.compileExpression(value);
		if(test != null)
			testExp = MVEL.compileExpression(test);
	}
	
	@Override
	public boolean verifyCondition(Map<String, Object> param) throws Exception {
		this.setCondition(true);
		if(testExp != null)
			 this.setCondition((Boolean)MVEL.executeExpression(testExp, param));
		return this.isConditionValiad();
	}
	
	@Override
	public String parser(Map<String, Object> params, List<Object> values)  throws Exception{
		Object objValue = MVEL.executeExpression(valueExp, params);
		return objValue+"";
	}

	@Override
	public BaseNode onClone() {
		OutNode textnode = new OutNode();
		textnode.valueExp = this.valueExp;
		textnode.testExp = this.testExp;
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.onClone());
		}
		textnode.childNodes = childs;
		return textnode;
	}
}
