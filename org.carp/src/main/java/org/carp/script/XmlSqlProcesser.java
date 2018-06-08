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

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.carp.exception.CarpException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parse classes for sqlfile nodes in the Carp configuration file.
 * Resolves the configuration file name of sql for subsequent sql parsing
 * @author zhou
 * @version 0.3
 */
public class XmlSqlProcesser {
	private static final Logger logger = LoggerFactory.getLogger("SQLFileProcessor:");
	private Node files;
	public XmlSqlProcesser(Node _files){
		this.files = _files;
	}
	
	/**
	 * Traversing the child nodes of a sqlfile node
	 * @throws CarpException
	 */
	public void parser() throws CarpException{
		NodeList nodes = files.getChildNodes();
		for(int i=0, count=nodes.getLength(); i < count; ++i){
			Node node = nodes.item(i);
			if("sql".equals(node.getNodeName())){
				parserSqlNode(node);
			}
		}
	}
	
	/**
	 * Parser sql node file
	 * @param sqlNode
	 * @throws CarpException
	 */
	private void parserSqlNode(Node sqlNode) throws CarpException{
		NamedNodeMap attrs = sqlNode.getAttributes();
		if(attrs.getLength() == 0 || attrs.getLength() >1){
			logger.error("Sqlfile node missing attribute(url or classpath or file) in configuration file");
			throw new CarpException("Sqlfile node missing attribute(url or classpath or file) in configuration file");
		}
		String attrName = attrs.item(0).getNodeName();
		if(!"url".equals(attrName) && !"classpath".equals(attrName) && !"file".equals(attrName)){
			logger.error("The sql node in the configuration file loses the attribute url or classpath or file");
			throw new CarpException("The sql node in the configuration file loses the attribute url or classpath or file");
		}
		InputStream stream = null;
		try {
			if("url".equals(attrName)){
				stream = new URL(attrs.item(0).getNodeValue()).openStream();
			}else if("file".equals(attrName)){
				stream = new FileInputStream(attrs.item(0).getNodeValue());
			}else{
				stream = this.getClass().getClassLoader().getResourceAsStream(attrs.item(0).getNodeValue());
			}
			logger.info("Parsering sqlfile. File : {}",attrs.item(0).getNodeValue());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			SqlFileParser parser = new SqlFileParser(factory.newDocumentBuilder().parse(stream));
			parser.process();
		} catch (Exception e) {
			logger.error("Parser file failed! File : {}",attrs.item(0).getNodeValue());
			throw new CarpException("Parser file failed! File:"+attrs.item(0).getNodeValue(),e);
		}
	}
	
}
