package org.qifu.model;

import org.qifu.util.DataUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

public class ManualDs {
	
	private HikariDataSource dataSource = null;
	
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	
	public ManualDs(HikariDataSource dataSource) {
		this.dataSource = dataSource;
		try {
			this.jdbcTemplate = DataUtils.getManualJdbcTemplate(this.dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public ManualDs(HikariDataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
		if (null == this.jdbcTemplate) {
			try {
				this.jdbcTemplate = DataUtils.getManualJdbcTemplate(this.dataSource);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public HikariDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
