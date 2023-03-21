package org.qifu.model;

public class DsDriverType {
	
	public static final String msSqlServer = "1";
	
	public static final String oracle = "2";
	
	public static final String mariaDB = "3";
	
	public static String getDriverClassName(String type) {
		if (msSqlServer.equals(type)) {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		if (oracle.equals(type)) {
			return "oracle.jdbc.driver.OracleDriver";
		}
		return "org.mariadb.jdbc.Driver";
	}
	
}
