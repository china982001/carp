package org.carp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.carp.CarpSessionBuilder;
import org.carp.cfg.CarpConfig;
import org.carp.test.anno.AnnoTest;
import org.carp.test.batch.BatchObjectTest;
import org.carp.test.batch.BatchSQLTest;
import org.carp.test.blob.TestBlobClob;
import org.carp.test.config.ConfigTest;
import org.carp.test.delete.DeleteCase;
import org.carp.test.dynamicsql.DynamicSqlTest;
import org.carp.test.encrypt.PasswordEncrypt;
import org.carp.test.find.FindCase;
import org.carp.test.jdbc.JDBCTest;
import org.carp.test.jdbc.TableTest;
import org.carp.test.map.MapObjectTest;
import org.carp.test.metadata.MetadataTest;
import org.carp.test.query.ClassQueryCase;
import org.carp.test.query.DatasetQueryCase;
import org.carp.test.save.SaveTest;
import org.carp.test.update.CarpUpdateCase;

public class TestMain {
	static {
        try {
            ConfigurationSource source;
            File log4jFile = new File("src/resources/log4j2.xml");
            if (log4jFile.exists()) {
                source = new ConfigurationSource(new FileInputStream(log4jFile), log4jFile);
                Configurator.initialize(null, source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
	public static void main(String[] args) throws Exception {
		CarpConfig config = new CarpConfig(new File("src/resources/carp.conf.xml"));
		CarpSessionBuilder  builder = config.getSessionBuilder();
		/**
		 * 
		 */
		// 注解测试
		AnnoTest.test();
		
		//密码加解密测试
		PasswordEncrypt.test();
		
        //配置文件测试
		ConfigTest.test();
		
		//测试SQL配置
//		SqlTest.testBaseSql();
		
		// Sql 执行测试，创建测试表。
		TableTest.test(builder);
		
		// BlOB,Clob类型测试
		TestBlobClob.test(builder);
		
		// 对象插入测试
		BatchObjectTest.test(builder);
		
		// Sql 批量插入测试
		BatchSQLTest.test(builder);
		
		// 删除记录
		DeleteCase.test(builder);
		
		// find by id
		FindCase.test(builder);
		
		// 连接测试
		JDBCTest.testSession(builder);
		
		//Save Map
		MapObjectTest.test(builder);
		
		//Query
		ClassQueryCase.test(builder);
		
		//dynamicSQL
		DynamicSqlTest.test(builder);
		
		//DataSetQuery
		DatasetQueryCase.test(builder);
		
		//SaveObject
		SaveTest.test(builder);
		
		//UPdate Object
		CarpUpdateCase.test(builder);
		
		//Get MetaData
		MetadataTest.test(builder);
		
		//Store Procedure
//		TestProcedure.test(builder);
		
	}
	
}
