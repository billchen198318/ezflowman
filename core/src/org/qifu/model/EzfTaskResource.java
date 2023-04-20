package org.qifu.model;

import java.util.HashMap;
import java.util.Map;

import org.qifu.core.util.TemplateUtils;

public class EzfTaskResource {
	
	private static final String UpdateMasterStateCommand = "org/qifu/core/runnable/update_master_state.ftl";
	
	private static final String UpdateMasterProcessNoAndStateCommand = "org/qifu/core/runnable/update_master_processno_and_state.ftl";
	
	private static final String SelectMasterStateCommand = "org/qifu/core/runnable/select_master_sign_state.ftl";
	
	private static final String SelectMasterCommand = "org/qifu/core/runnable/select_master.ftl";
	
	private static final String SelectDetailCommand = "org/qifu/core/runnable/select_detail.ftl";
	
	public static String getUpdateMasterStateCommand(String dsDriverType, String tableName, String mainTblPkField, String efgpProcessStatusField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("mainTblPkField", mainTblPkField);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", EzfTaskResource.class.getClassLoader(), UpdateMasterStateCommand, paramMap);			
	}
	
	public static String getUpdateMasterProcessNoAndStateCommand(String dsDriverType, String tableName, String mainTblPkField, String efgpProcessNoField, String efgpProcessStatusField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("mainTblPkField", mainTblPkField);
		paramMap.put("efgpProcessNoField", efgpProcessNoField);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", EzfTaskResource.class.getClassLoader(), UpdateMasterProcessNoAndStateCommand, paramMap);		
	}		
	
	public static String getSelectMasterStateCommand(String dsDriverType, String tableName, String efgpProcessStatusField, String efgpProcessNoField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		paramMap.put("efgpProcessNoField", efgpProcessNoField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", EzfTaskResource.class.getClassLoader(), EzfTaskResource.SelectMasterStateCommand, paramMap);
	}	
	
	public static String getSelectMasterCommand(String dsDriverType, String tableName, String efgpProcessStatusField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", EzfTaskResource.class.getClassLoader(), EzfTaskResource.SelectMasterCommand, paramMap);
	}
	
	public static String getSelectDetailCommand(String dsDriverType, String tableName, String detailTableFieldName, String masterTableFieldName) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("dtlTbl", tableName);
		paramMap.put("dtlFieldName", detailTableFieldName);
		paramMap.put("mstFieldName", masterTableFieldName);
		return TemplateUtils.processTemplate("getSelectDetailCommand", EzfTaskResource.class.getClassLoader(), EzfTaskResource.SelectDetailCommand, paramMap);
	}	
	
}
