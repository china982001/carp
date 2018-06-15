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

/**
 * 
 * @author zhou
 * @version 0.3
 */
public class WhereNode extends BaseNode {
	private String preTrim;
	private String suffTrim;
	@Override
	public void initValue(Node node) {
		if(node.getAttributes().getNamedItem("preTrim") != null)
			preTrim = node.getAttributes().getNamedItem("preTrim").getNodeValue().trim();
		if(node.getAttributes().getNamedItem("suffTrim") != null)
			suffTrim = node.getAttributes().getNamedItem("suffTrim").getNodeValue().trim();
	}

	@Override
	public String parser(Map<String, Object> params, List<Object> values) throws Exception{
		String content = "";
		for(BaseNode node: this.childNodes){
			if(node.verifyCondition(params))
				content +=" "+ node.parser(params,values);
		}
		content = content.trim();
		content = trimToArray(content,this.preTrim,true);
		content = trimToArray(content,this.suffTrim,false);
		return content.trim().length() !=0 ? " where "+content : "";
	}

	@Override
	public boolean verifyCondition(Map<String, Object> param) throws Exception {
		return true;
	}
	
	private String trimToArray(String content, String trim,boolean pre_suff){
		if(trim == null)
			return content;
		String[] strs = trim.split(":");
		for(String s : strs)
			content = trim(content,s,pre_suff);
		return content;
	}
	
	private String trim(String content,String replace,boolean pre_suff){
		if(pre_suff){//pre trim
			if(content.startsWith(replace))
				content = content.replaceFirst(replace, "").trim();
		}else{ //suff trim
			if(content.endsWith(replace))
				content = content.substring(0, content.lastIndexOf(replace)).trim();
		}
		return content;
	}

	@Override
	public BaseNode onClone() {
		WhereNode wherenode = new WhereNode();
		wherenode.preTrim = this.preTrim;
		wherenode.suffTrim = this.suffTrim;
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.onClone());
		}
		wherenode.childNodes = childs;
		return wherenode;
	}
}
