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
import java.util.StringTokenizer;

import org.carp.script.SQLParam;
import org.w3c.dom.Node;

public class TextNode extends BaseNode{
	private String text;
	
	@Override
	public void initValue(Node node) {
		text = node.getNodeValue().trim();
	}
	
	@Override
	public boolean verifyCondition(Map<String, Object> param) throws Exception {
		return true;
	}
	
	@Override
	public String parser(Map<String, Object> params, List<Object> values)  throws Exception{
		StringTokenizer tokenizer = new StringTokenizer(text,"#{}",true);
		List<String> list = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()){
			if(tokenizer.nextToken().equals("#")){
				if(tokenizer.nextToken().equals("{"))
					list.add(tokenizer.nextToken());
			}
		}
		String content = text;
		for(String p : list){
			content = content.replaceFirst("#\\{"+p+"\\}", "?");
			SQLParam param = this.getParam(p.trim(),params);
			values.add(param);
		}
		return content;
	}

	@Override
	public BaseNode onClone() {
		TextNode textnode = new TextNode();
		textnode.text = this.text;
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.onClone());
		}
		textnode.childNodes = childs;
		return textnode;
	}
}
