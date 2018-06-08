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
package org.carp.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.carp.CarpSessionBuilder;
import org.carp.exception.CarpException;
import org.carp.intercept.Interceptor;
import org.carp.security.IPasswordDecryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;


/**
 * Carp configuration
 * This class is used to parse the XML based carp configuration file, or a property type object.
 * @author zhou
 */
public final class CarpConfig {
	private static final Logger logger = LoggerFactory.getLogger(CarpConfig.class);
	//数据库连接配置文件: carp.conf.xml or carp.properties ；默认carp.conf.xml
	public final static String CONNECT_CONFIG = "carp.conf.xml";
	private Document doc;
	private Interceptor interceptor;
	private CarpSetting setting = null;
	
	/**
	 * 初始化Carp配置
	 * @throws CarpException
	 */
	public CarpConfig() throws CarpException{
		this(CONNECT_CONFIG);
	}
	
	/**
	 * 根据carp配置文件，初始化carp配置
	 * @param conf XML based configuration file.
	 * @throws CarpException
	 * @throws IOException 
	 */
	public CarpConfig(String filename) throws CarpException{
		if(filename == null)
			throw new CarpException("parameter: filename is null！");
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filename);
		if(stream == null)
			throw new CarpException("Carp Configuration file："+filename +" not exist!");
		this.parser(stream);
		logger.info("Carp Configuration file：{}", filename);
	}
	
	/**
	 * 根据carp配置文件，初始化carp配置
	 * @param conf XML based configuration file.
	 * @throws CarpException
	 * @throws IOException 
	 */
	public CarpConfig(File file) throws CarpException{
		if(file == null || !file.exists() || file.isDirectory())
			throw new CarpException("parameter: file is null or not exist");
		try {
			this.parser(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new CarpException("Can't parser Carp Configuration file.",e);
		}
		logger.info("Carp Configuration file：{}",file.getAbsolutePath());
	}
	
	/**
	 * 加载配置文件
	 * @param p
	 * @throws CarpException 
	 */
	public CarpConfig(Properties p) throws CarpException{
		setting =  new CarpSetting(p);
	}
	/**
	 * 根据carp配置文件输入流，初始化Carp配置
	 * @param stream
	 * @throws CarpException
	 */
	public CarpConfig(InputStream stream) throws CarpException{
		if(stream == null)
			throw new CarpException("stream is null！");
		this.parser(stream);
	}
	
	/**
	 * 解析carp文件输入流
	 * @param resName carp文件输入流
	 * @throws CarpException
	 */
	private void parser(InputStream resName) throws CarpException{
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			doc = factory.newDocumentBuilder().parse(resName);
			setting = new CarpSetting(doc);
		} catch (Exception e) {
			throw new CarpException("Carp configuration parser error!",e);
		}
	}
	
	/**
	 * 根据Carp配置，创建会话构建器对象
	 * @return CarpSessionBuilder  会话构建器对象
	 * @throws CarpException
	 */
	public CarpSessionBuilder getSessionBuilder() throws CarpException{
		CarpSessionBuilder builder = new CarpSessionBuilder(setting);
		logger.info("Create CarpSessionBuilder...");
		this.setting.setInterceptor(this.interceptor);
		logger.info("Create gloab Interceptor... {}",this.interceptor);
		return builder;
	}
	
	/**
	 * 设置拦截器对象
	 * @param interceptor 拦截器对象
	 * @return
	 */
	public CarpConfig setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
		return this;
	}
	
	public CarpSetting getSetting(){
		return this.setting;
	}
	
	public CarpConfig setDecryptor(IPasswordDecryptor decryptor){
		this.setting.setDecryptor(decryptor);
		return this;
	}
}
