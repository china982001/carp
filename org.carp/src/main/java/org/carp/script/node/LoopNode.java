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
import java.util.Set;

import org.carp.exception.CarpException;
import org.mvel2.MVEL;
import org.w3c.dom.Node;

public class LoopNode extends BaseNode {
	private String var; //变量名称
	private String list; // 集合
	private String filter; //过滤条件
	private Object filterExp; // filter expression condition
	private String bsign;  //起始符号
	private String esign;  //结束符号
	private int bindex = 0; //起始索引值，对于list、或数组来说
	private int eindex = -1; //结束索引值
	private String separator; //分割符号，每次循环之间插入的分割符号，如逗号，and、or等等。
	
	@Override
	public void initValue(Node node) {
		var = getAttrValue(node,"var","var");
		list = getAttrValue(node,"list","list");
		filter = getAttrValue(node,"filter",null);
		bsign = getAttrValue(node,"bsign","");
		esign = getAttrValue(node,"esign","");
		if(getAttrValue(node,"bindex",null) != null)
			bindex = Integer.valueOf(getAttrValue(node,"bindex","0"));
		if(getAttrValue(node,"eindex",null) != null)
			eindex = Integer.valueOf(getAttrValue(node,"eindex","-1"));
		separator = getAttrValue(node,"separator","");
		if(filter != null)
			this.filterExp = MVEL.compileExpression(filter);
	}
	
	@Override
	public String parser(Map<String, Object> paramMap, List<Object> values)  throws Exception{
		Object collection = paramMap.get(list);
		String content = "";
		content += bsign;
		if(collection instanceof List){
			List<?> data = (List<?>)collection;
			content = processLoopBody(content,data.toArray(),paramMap,values);
		}else if(collection instanceof Integer[]){
			content = processLoopBody(content,(Integer[])collection,paramMap,values);
		}else if(collection instanceof int[]){
			int[] tmp = (int[])collection;
			Integer[] data = new Integer[tmp.length];
			for(int i=0;i<tmp.length; ++i)data[i] = tmp[i];
			content = processLoopBody(content,data,paramMap,values);
		}else if(collection instanceof String[]){
			content = processLoopBody(content,(String[])collection,paramMap,values);
		}else if(collection instanceof Long[]){
			content = processLoopBody(content,(Long[])collection,paramMap,values);
		}else if(collection instanceof long[]){
			long[] tmp = (long[])collection;
			Long[] data = new Long[tmp.length];
			for(int i=0;i<tmp.length; ++i)data[i] = tmp[i];
			content = processLoopBody(content,data,paramMap,values);
		}else if(collection instanceof Short[]){
			content = processLoopBody(content,(Short[])collection,paramMap,values);
		}else if(collection instanceof short[]){
			short[] tmp = (short[])collection;
			Short[] data = new Short[tmp.length];
			for(int i=0;i<tmp.length; ++i)data[i] = tmp[i];
			content = processLoopBody(content,data,paramMap,values);
		}else if(collection instanceof Set){
			Set<?> data = (Set<?>)collection;
			content = processLoopBody(content,data.toArray(),paramMap,values);
		}else{
			throw new CarpException("Unsupported parameter type, only supports: list or map or array."
					+ "For example: List list = ArrayList (); Set set = HashSet (); String[] strs = new String[20];");
		}
		content += esign;
		return content;
	}

	private String processLoopBody(String content, Object[] data,Map<String, Object> paramMap, List<Object> values) throws Exception{
		bindex = bindex < 0 ? 0 :(bindex >= data.length ? 0 : bindex); //ArrayIndexOutOfBoundsException
		int count = eindex == -1 ? data.length : (eindex > data.length ? data.length : eindex );//ArrayIndexOutOfBoundsException
		for(int i = bindex; i < count; ++i){
			Object o = data[i];
			paramMap.put(var, o);
			if(filter != null){
				Boolean bool = (Boolean)MVEL.executeExpression(filterExp, paramMap);
				if(!bool) continue;           //bool == false
			}
			for(BaseNode node: this.childNodes){
				content += node.parser(paramMap,values);
			}
			content = content + " " + separator+" ";
		}
		content = content.trim();
		if(content.endsWith(separator))
			content = content.substring(0, content.lastIndexOf(separator)).trim();
		return content;
	}

	@Override
	public boolean verifyCondition(Map<String, Object> param) throws Exception {
		return true;
	}

	@Override
	public BaseNode onClone() {
		LoopNode loopnode = new LoopNode();
		loopnode.var = this.var;
		loopnode.list = this.list;
		loopnode.filter = this.filter;
		loopnode.bsign = this.bsign;
		loopnode.esign = this.esign;
		loopnode.bindex = this.bindex;
		loopnode.eindex = this.eindex;
		loopnode.separator = this.separator;
		loopnode.filterExp = this.filterExp;
		List<BaseNode> childs = new ArrayList<BaseNode>(2);
		for(BaseNode node: this.childNodes){
			childs.add(node.onClone());
		}
		loopnode.childNodes = childs;
		return loopnode;
	}

}
