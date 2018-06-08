package org.carp.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.carp.CarpSessionBuilder;
import org.carp.script.SQLFactory;
import org.carp.test.pojo.CarpCat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private final static Logger logger = LoggerFactory.getLogger(Test.class);
	private static DataSource ds;
	static ObjectPool<PoolableConnection> pool;
	public static void main(String[] strs) throws Exception {
		//System.out.println(System.getenv());
		testPoolDataSource();
	}
	
	public static void testPoolDataSource()throws Exception{
		createDatasource();
		long d = System.currentTimeMillis();
		System.out.println(d);
		
		for(int i=0; i<10; ++i){
			new ConnThread("name - "+i, ds).start();;
		}
		for(int i=11; i<=20; ++i){
			new ConnThread("name - "+i, ds).start();;
		}
		
		System.out.println(System.currentTimeMillis() -d);
	}
	
	public static class ConnThread extends Thread{
		private DataSource _ds;
		String _name;
		public ConnThread(String name ,DataSource ds){
			this._ds = ds;
			this._name = name;
		}
		@Override
		public void run() {
			long d = System.currentTimeMillis();
			//System.out.println(d);
			try {
				for(int i=0; i<10000; ++i){
					Connection c= _ds.getConnection();
					c.close();
				}
				System.out.println("getNumIdle == "+pool.getNumIdle());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(_name+" ==  "+(System.currentTimeMillis() -d));
		}
	}
	
	private static void createDatasource() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:mysql://localhost:3306/adms", "adms","adms");
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
		pool = connectionPool;
		poolableConnectionFactory.setPool(connectionPool);
		PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
		ds = dataSource;
		System.out.println(connectionPool.getNumActive());
	}

	public static void testLogLevelToMutilLogFile() throws Exception {
		logger.error(" Error..................");
		logger.warn(" Warn..................");
		logger.info(" Info..................");
		logger.debug(" Debug..................");
		logger.trace(" Trace..................");
		logger.error(" Error..................");
		logger.warn(" Warn..................");
		logger.info(" Info..................");
		logger.debug(" Debug..................");
		logger.trace(" Trace..................");
		logger.error(" Error..................");
		logger.warn(" Warn..................");
		logger.info(" Info..................");
		logger.debug(" Debug..................");
		logger.trace(" Trace..................");
		logger.error(" Error..................");
		logger.warn(" Warn..................");
		logger.info(" Info..................");
		logger.debug(" Debug..................");
		logger.trace(" Trace..................");
	}

	public static void testDySQLSpeed() throws Exception {
		CarpSessionBuilder builder = CarpSessionBuilder.getSessionBuilder();
		CarpCat cat = new CarpCat();
		cat.setCatWeight(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("po", cat);
		long d = System.currentTimeMillis();
		for (int i = 0; i < 100000; ++i) {

			SQLFactory.getSQL("m1/s2", map);
		}
		System.out.println(System.currentTimeMillis() - d);

	}

}
