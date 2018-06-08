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
package org.carp.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC事务隔离级别
 * @author zhou
 * @since 0.1
 */
public class IsolationLevel {

  public static final int UNSET_ISOLATION_LEVEL = -9999;

  private int isolationLevel = UNSET_ISOLATION_LEVEL;
  private int originalIsolationLevel = UNSET_ISOLATION_LEVEL;

  /**
   * 设置事务级别
   * @param isolationLevel
   */
  public void setIsolationLevel(int isolationLevel) {
    this.isolationLevel = isolationLevel;
  }

  /**
   * 应用事务隔离级别
   * @param conn
   * @throws SQLException
   */
  public void applyIsolationLevel(Connection conn) throws SQLException {
    if (isolationLevel != UNSET_ISOLATION_LEVEL) {
      originalIsolationLevel = conn.getTransactionIsolation();
      if (isolationLevel != originalIsolationLevel) {
        conn.setTransactionIsolation(isolationLevel);
      }
    }
  }

  /**
   * 恢复JDBC原来的事务隔离级别
   * @param conn
   * @throws SQLException
   */
  public void restoreIsolationLevel(Connection conn) throws SQLException {
    if (isolationLevel != originalIsolationLevel) {
      conn.setTransactionIsolation(originalIsolationLevel);
    }
  }

}
