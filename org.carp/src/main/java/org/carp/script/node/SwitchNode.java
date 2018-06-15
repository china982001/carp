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

import org.w3c.dom.Node;

public class SwitchNode extends BaseNode {

	@Override
	public void initValue(Node node) {
	}

	@Override
	public String parser(Map<String, Object> params, List<Object> values) throws Exception{
		for(BaseNode node: this.childNodes){
			if(node.verifyCondition(params))
				return node.parser(params,values);
		}
		return "";
	}

	@Override
	public boolean verifyCondition(Map<String, Object> param) throws Exception {
		return true;
	}

	@Override
	public BaseNode onClone() {
		SwitchNode switchnode = new SwitchNode();
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.onClone());
		}
		switchnode.childNodes = childs;
		return switchnode;
	}
}
