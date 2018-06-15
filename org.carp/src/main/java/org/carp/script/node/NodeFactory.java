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

import org.carp.exception.CarpException;

public class NodeFactory {
	
	public static BaseNode getNode(String nodeName) throws CarpException{
		switch(nodeName.toLowerCase()){
			case "#text": return new TextNode();
			case "if": return new IfNode();
			case "loop":return new LoopNode();
			case "switch":return new SwitchNode();
			case "case":return new SwitchCaseNode();
			case "default":return new SwitchDefaultNode();
			case "where":return new WhereNode();
			case "assign":return new AssignNode();
			default: throw new CarpException("Wrong tagname:"+nodeName+". Only the following tags are supported: if, loop, switch,case, default,where");
		}
	}
}
