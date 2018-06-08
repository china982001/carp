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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.carp.exception.CarpException;
import org.carp.intercept.Interceptor;
import org.carp.script.XmlSqlProcesser;
import org.carp.security.IPasswordDecryptor;
import org.carp.sql.CarpSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Carp Configuration class
 * @author zhou
 * @since 0.1
 */
public final class CarpSetting {
	private static final Logger logger = LoggerFactory.getLogger("CarpConfig: ");
	private Map<String,String> elemMap = new HashMap<String,String>(24);
	
	/**
	 * database connection property
	 */
	private Properties p = new Properties();
	/**
	 * Connection pool property for dbcp or c3p0
	 */
	private Properties connPoolProperty = new Properties();
	
	
	/** 数据库连接用户	 */
	private String userName;
	/** 数据库连接密码	 */
	private String password;
	/** 数据库链接密码是否加密处理  */
	private boolean pwdEncode;
	/** 数据库连接驱动类	 */
	private String driverClass;
	/** 数据库连接字符串URL	 */
	private String url;
	/** 数据库连接的数据源名	 */
	private String dataSource;
	/** 数据库特定sql生成类*/
	private Class<? extends CarpSql> databaseDialect;//
	/** 连接池	 */
	private String pool;
	/** 数据库事务，默认使用JDBC事务	 */
	private String carpTransaction = Constant.TRANSACTION_JDBC;
	/** 事务隔离级别	 */
	private Integer transIsoLationLevel;
	/** 是否显示被执行的Sql在控制台	 */
	private boolean showSql = true;
	/** 事务工厂	 */
	private String transFactory;
	/** Carp使用的Cache	 */
	private String cache;
	/** 批处理大小，默认值：2000	 */
	private int batchSize = 2000;
	/** 查询数据时的Fetch大小，默认不设置	 */
	private int fetchSize = 0;
	/** 是否允许滚动结果集，默认：false	 */
	private boolean enableScrollableResultSet = false;
	/** 数据库当前的catalog	 */
	private String catalog;
	/** 数据库的当前schema	 */
	private String schema;
	/**
	 * statement query timeout
	 */
	private Integer timeout;

	/** 数据库连接的拦截器对象	 */
	private Interceptor interceptor;
	private IPasswordDecryptor decryptor;
	/**
	 * auto commit
	 */
	private Boolean autoCommit;
	
	
	/**
	 * load Carp Configuration based XML
	 * @param doc
	 * @throws CarpException
	 */
	public CarpSetting(Document doc) throws CarpException{
		parserCarpXMLConfig(doc.getDocumentElement());
		this.initConfig();
	}
	
	/**
	 * load Carp Configuration based properties
	 * @param prop
	 * @throws CarpException
	 */
	public CarpSetting(Properties prop) throws CarpException{
		parserCarpPropConfig(prop);
		this.initConfig();
	}
	

	/**
	 * initial carp configuration value
	 * @throws CarpException
	 */
	@SuppressWarnings("unchecked")
	private void initConfig() throws CarpException{
		String value = getNodeValue(Constant.USER_NAME);
		if(value != null)
			this.userName = value;
		value = getNodeValue(Constant.USER_PASSWORD);
		if(value != null)
			this.password = value;
		value = getNodeValue(Constant.PWD_ENCODE);
		if(value != null)
			this.pwdEncode = new Boolean(value);
		value = getNodeValue(Constant.DRIVER_CLASS);
		if(value != null)
			this.driverClass = value;
		value = getNodeValue(Constant.CONNECTIION_URL);
		if(value != null)
			this.url = value;
		value = getNodeValue(Constant.DATASOURCE);
		if(value != null)
			this.dataSource = value;
		value = getNodeValue(Constant.DIALECT);
		if(value != null){
			try {
				this.databaseDialect = (Class<CarpSql>) Class.forName(value);
			} catch (ClassNotFoundException e) {
				throw new CarpException("不受支持的数据库sql生成类:"+value,e);
			}
		}
		value = getNodeValue(Constant.CONNECT_POOL);
		if(value != null)
			this.pool = value.toLowerCase();
		value = getNodeValue(Constant.CARP_TRANSACTION);
		if(value != null)
			this.carpTransaction = value;
		value = getNodeValue(Constant.CARP_TRANS_ISOLATIONLEVEL);
		if(value != null)
			this.transIsoLationLevel = Integer.parseInt(value);
		value = getNodeValue(Constant.SHOW_SQL);
		if(value != null)
			this.showSql = value.equalsIgnoreCase("true")?true:false;
		value = getNodeValue(Constant.CARP_JDBC_BATCH_SIZE);
		if(value != null)
			this.batchSize = Integer.parseInt(value);
		value = getNodeValue(Constant.CARP_JDBC_FETCH_SIZE);
		if(value != null)
			this.fetchSize = Integer.parseInt(value);
		value = getNodeValue(Constant.ENABLED_SCROLL_RESULTSET);
		if(value != null)
			this.enableScrollableResultSet = Boolean.parseBoolean(value);
		value = getNodeValue(Constant.CARP_CATALOG);
		if(value != null)
			this.catalog = value;
		value = getNodeValue(Constant.CARP_SCHEMA);
		if(value != null)
			this.schema = value;
		value = getNodeValue(Constant.AUTO_COMMIT);
		if(value != null)
			this.autoCommit = new Boolean(value);
		value = getNodeValue(Constant.QUERY_TIMEOUT);
		if(value != null)
			this.timeout = Integer.parseInt(value);
	}
	
	/**
	 * parser carp configuration DOM based XML
	 * @param root carp configuration xml root element
	 * @throws CarpException 
	 */
	private void parserCarpXMLConfig(Element root) throws CarpException{
		NodeList elems = root.getChildNodes();
		for(int i= 0, count = elems.getLength(); i < count; ++i){
			Node elem = elems.item(i);
			//System.out.println(elem.getTextContent()+" - "+elem.getNodeName()+" - "+elem.getNodeType()+" - "+elem.getNodeValue());
			String nodeName = elem.getNodeName();
			switch(nodeName){
			case "property":
				parserPropertyNode(elem);
				break;
			case "sqlfile": 
				new XmlSqlProcesser(elem).parser();
				break;
			default:;
			}			
		}
	}
	
	/**
	 * parser xml property's node
	 * @param node
	 */
	private void parserPropertyNode(Node node){
		String key = node.getAttributes().getNamedItem("name").getNodeValue();
		String value = node.getTextContent().trim();
		if(value.equals(""))
			return;
		if(key.startsWith("property."))
			this.p.setProperty(key.substring(9), value);
		else if(key.startsWith("pool."))
			this.connPoolProperty.setProperty(key.substring(5), value);
		else
			elemMap.put(key, value);
		if("carp.password".equals(key))
			logger.info("{} : ******",key);
		else
			logger.info("{} : {}",key,value);
	}
	
	/**
	 * parser carp configuration DOM based Properties
	 * @param p
	 */
	private void parserCarpPropConfig(Properties p){
		Enumeration<?> enums = p.propertyNames();
		while(enums.hasMoreElements()){
			String key = enums.nextElement()+"";
			String value = p.getProperty(key);
			if(value == null || value.trim().equals(""))
				continue;
			if(key.startsWith("property."))
				p.setProperty(key.substring(9), value.trim());
			else if(key.startsWith("pool."))
				this.connPoolProperty.setProperty(key.substring(5), value);
			else
				elemMap.put(key, value.trim());
			if("carp.password".equals(key))
				logger.info("{} : ******",key);
			else
				logger.info("{} : {}",key,value.trim());
		}
	}
	
	private String getNodeValue(String name){
		return elemMap.get(name.toLowerCase());
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public boolean isPwdEncode() {
		return pwdEncode;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getUrl() {
		return url;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getPool() {
		return pool;
	}

	public String getCarpTransaction() {
		return carpTransaction;
	}

	public Integer getTransIsoLationLevel() {
		return transIsoLationLevel;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public String getTransFactory() {
		return transFactory;
	}

	public String getCache() {
		return cache;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}
	
	public Integer getTimeout(){
		return this.timeout;
	}

	public Interceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	public boolean isEnableScrollableResultSet() {
		return enableScrollableResultSet;
	}

	public Class<? extends CarpSql> getDatabaseDialect() {
		return databaseDialect;
	}
	
	public void setDatabaseDialect(Class<? extends CarpSql> dialect){
		this.databaseDialect = dialect;
	}
	
	public Properties getExtProperty(){
		return p;
	}
	
	public Boolean isAutoCommit(){
		return this.autoCommit;
	}

	public IPasswordDecryptor getDecryptor() {
		return decryptor;
	}

	public void setDecryptor(IPasswordDecryptor decryptor) {
		this.decryptor = decryptor;
	}

	public Properties getConnPoolProperty() {
		return connPoolProperty;
	}
}
