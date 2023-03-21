package org.qifu.utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.qifu.util.DataUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ManualDataSourceUtils {
	
	private static volatile ThreadLocal<Map<String, HikariDataSource>> dsThreadLocal = new ThreadLocal<Map<String, HikariDataSource>>();
	
	private static volatile ThreadLocal<Map<String, NamedParameterJdbcTemplate>> jdbcTemplateThreadLocal = new ThreadLocal<Map<String, NamedParameterJdbcTemplate>>();
	
	static {
		dsThreadLocal.set( new HashMap<String, HikariDataSource>() );
		jdbcTemplateThreadLocal.set( new HashMap<String, NamedParameterJdbcTemplate>() );
	}
	
	public static boolean isRunning(String poolName) {
		return dsThreadLocal.get().get(poolName) == null ? false : dsThreadLocal.get().get(poolName).isRunning();
	}
	
	public static NamedParameterJdbcTemplate getJdbcTemplate(String poolName) {
		return jdbcTemplateThreadLocal.get().get(poolName);
	}
	
	public static void remove(String poolName) throws Exception {
		HikariDataSource ds = null;
		if (null == (ds = dsThreadLocal.get().get(poolName))) {
			return;
		}
		ds.close();
		ds = null;
		dsThreadLocal.get().remove(poolName);
		jdbcTemplateThreadLocal.get().remove(poolName);
	}
	
	public static HikariDataSource create(String poolName, String driverClassName, String user, String password, String jdbcUrl) throws Exception {
		if (dsThreadLocal.get().get(poolName) != null) {
			return dsThreadLocal.get().get(poolName);
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
		dsThreadLocal.get().put(poolName, ds);
		jdbcTemplateThreadLocal.get().put(poolName, DataUtils.getManualJdbcTemplate(ds));
		return ds;
	}
	
}
