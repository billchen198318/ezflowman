/* 
 * Copyright 2019-2023 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.qifu.model.ManualDs;
import org.qifu.util.DataUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ManualDataSourceUtils {
	
	private static volatile ThreadLocal<Map<String, ManualDs>> dsThreadLocal = new ThreadLocal<Map<String, ManualDs>>();
	
	static {
		dsThreadLocal.set( new HashMap<String, ManualDs>() );
	}
	
	public static boolean isRunning(String poolName) {
		return (dsThreadLocal.get().get(poolName) == null || dsThreadLocal.get().get(poolName).getDataSource() == null) ? false : dsThreadLocal.get().get(poolName).getDataSource().isRunning();
	}
	
	public static NamedParameterJdbcTemplate getJdbcTemplate(String poolName) {
		return dsThreadLocal.get().get(poolName).getJdbcTemplate();
	}
	
	public static void remove(String poolName) throws Exception {
		HikariDataSource ds = null;
		if (null == (ds = dsThreadLocal.get().get(poolName).getDataSource())) {
			return;
		}
		ds.close();
		ds = null;
		dsThreadLocal.get().get(poolName).setDataSource( null );
		dsThreadLocal.get().get(poolName).setJdbcTemplate( null );
		dsThreadLocal.get().remove(poolName);		
	}
	
	public static HikariDataSource create(String poolName, String driverClassName, String user, String password, String jdbcUrl) throws Exception {
		if (dsThreadLocal.get().get(poolName) != null && dsThreadLocal.get().get(poolName).getDataSource() != null) {
			return dsThreadLocal.get().get(poolName).getDataSource();
		}
		Properties props = new Properties();
		props.setProperty("dataSource.user", user);
		props.setProperty("dataSource.password", password);
		props.put("dataSource.logWriter", new PrintWriter(System.out));
		HikariConfig config = new HikariConfig(props);
		config.setDriverClassName("org.mariadb.jdbc.Driver");
		config.setPoolName(poolName);
		config.setJdbcUrl(jdbcUrl);
		config.setMaximumPoolSize(4);
		HikariDataSource ds = new HikariDataSource(config);
		ManualDs mds = new ManualDs(ds, DataUtils.getManualJdbcTemplate(ds));
		dsThreadLocal.get().put(poolName, mds);
		return ds;
	}
	
}
