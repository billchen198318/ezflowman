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
package org.qifu.core.runnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.entity.EzfMap;
import org.qifu.core.entity.EzfMapField;
import org.qifu.core.entity.EzfMapGrd;
import org.qifu.core.entity.EzfMapGrdTblMp;
import org.qifu.core.logic.IEzfMapperLogicService;
import org.qifu.core.service.IEzfDsService;
import org.qifu.core.service.IEzfMapFieldService;
import org.qifu.core.service.IEzfMapGrdService;
import org.qifu.core.service.IEzfMapGrdTblMpService;
import org.qifu.core.service.IEzfMapService;
import org.qifu.core.util.TemplateUtils;
import org.qifu.model.DsDriverType;
import org.qifu.model.EFGPSimpleProcessInfoState;
import org.qifu.utils.EZFlowWebServiceUtils;
import org.qifu.utils.ManualDataSourceUtils;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.github.underscore.U;

import ognl.Ognl;

public class EzfTaskRunnable implements Runnable {
	protected Logger logger = LogManager.getLogger(EzfTaskRunnable.class);
	
	private static final String _EFGP_SPI_Xml_PackageElemTagName = "com.dsc.nana.services.webservice.SimpleProcessInfo"; 
	
	private String cnfId;
	
	private IEzfDsService<EzfDs, String> ezfDsService;
	
	private IEzfMapService<EzfMap, String> ezfMapService;
	
	private IEzfMapFieldService<EzfMapField, String> ezfMapFieldService;	
	
	private IEzfMapGrdService<EzfMapGrd, String> ezfMapGrdService;
	
	private IEzfMapGrdTblMpService<EzfMapGrdTblMp, String> ezfMapGrdTblMpService;	
	
	private IEzfMapperLogicService ezfMapperLogicService;
	
	public EzfTaskRunnable() {
		super();
	}
	
	public EzfTaskRunnable(String cnfId) {
		super();
		this.cnfId = cnfId;
	}
	
	private void initBeans() throws BeansException, Exception {
		if (null == this.ezfDsService) {
			this.ezfDsService = (IEzfDsService<EzfDs, String>) AppContext.getBean(IEzfDsService.class);
		}
		if (null == this.ezfMapService) {
			this.ezfMapService = (IEzfMapService<EzfMap, String>) AppContext.getBean(IEzfMapService.class);
		}
		if (null == this.ezfMapFieldService) {
			this.ezfMapFieldService = (IEzfMapFieldService<EzfMapField, String>) AppContext.getBean(IEzfMapFieldService.class);
		}
		if (null == this.ezfMapGrdService) {
			this.ezfMapGrdService = (IEzfMapGrdService<EzfMapGrd, String>) AppContext.getBean(IEzfMapGrdService.class);
		}
		if (null == this.ezfMapGrdTblMpService) {
			this.ezfMapGrdTblMpService = (IEzfMapGrdTblMpService<EzfMapGrdTblMp, String>) AppContext.getBean(IEzfMapGrdTblMpService.class);
		}
		if (null == this.ezfMapperLogicService) {
			this.ezfMapperLogicService = (IEzfMapperLogicService) AppContext.getBean(IEzfMapperLogicService.class);
		}
	}
	
	@Override
	public void run() {
		try {
			this.initBeans();	
			this.process();
		} catch (BeansException e) {
			e.printStackTrace();
			logger.error( e != null && e.getMessage() != null ? e.getMessage() : "null" );
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error( e != null && e.getMessage() != null ? e.getMessage() : "null" );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error( e != null && e.getMessage() != null ? e.getMessage() : "null" );
		}
	}
	
	private void processState(EzfDs ezfDs, EzfMap dataForm) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		NamedParameterJdbcTemplate jdbcTemplate = this.getJdbcTemplate(ezfDs);
		String queryStateSql = this.getSelectMasterStateCommand(ezfDs.getDriverType(), dataForm.getMainTbl(), dataForm.getEfgpProcessStatusField(), dataForm.getEfgpProcessNoField());
		List<Map<String, Object>> checkStateList = jdbcTemplate.queryForList(queryStateSql, paramMap);	
		for (Map<String, Object> m : checkStateList) {
			Object efgpProcessNoVal = Ognl.getValue(dataForm.getEfgpProcessNoField(), m);
			if (!StringUtils.isBlank((String)efgpProcessNoVal)) {
				String efgpSimpleProcessInfoXml = StringUtils.defaultString(EZFlowWebServiceUtils.fetchProcInstanceWithSerialNo((String)efgpProcessNoVal));
				if (efgpSimpleProcessInfoXml.indexOf(_EFGP_SPI_Xml_PackageElemTagName) == -1) {
					logger.warn("無可處理回復(fetchProcInstanceWithSerialNo): " + efgpSimpleProcessInfoXml);
					continue;
				}
				Map<String, Object> simpleProcessInfoMap = U.fromXmlMap(efgpSimpleProcessInfoXml);
				Map<String, Object> processInfoMap = (Map<String, Object>) simpleProcessInfoMap.get(_EFGP_SPI_Xml_PackageElemTagName);
				if (!EFGPSimpleProcessInfoState.isState((String)processInfoMap.get("state"))) {
					logger.warn("無可處理回復(fetchProcInstanceWithSerialNo/state): " + efgpSimpleProcessInfoXml);
					continue;
				}
				String stateVal = EFGPSimpleProcessInfoState.getValue((String)processInfoMap.get("state"));
				if (!"1".equals(stateVal) && !StringUtils.isBlank(stateVal)) {
					String updateStateSql = this.getUpdateMasterStateCommand(ezfDs.getDriverType(), dataForm.getMainTbl(), dataForm.getEfgpProcessStatusField(), dataForm.getEfgpProcessNoField());
					paramMap.clear();
					paramMap.put("stateVal", stateVal);
					paramMap.put("pProcessInstanceSerialNo", efgpProcessNoVal);
					jdbcTemplate.update(updateStateSql, paramMap);
				}
			}
		}
	}
	
	private void process() throws ServiceException, Exception {
		logger.info(this.getClass().getSimpleName() + " >>> CNF_ID: " + this.cnfId + " - process start...");
		if (StringUtils.isBlank(this.cnfId)) {
			return;
		}
		EzfMap dataForm = new EzfMap();
		dataForm.setCnfId( this.cnfId );
		dataForm = this.ezfMapService.selectByUniqueKey(dataForm).getValueEmptyThrowMessage();
		
		EzfDs ezfDs = new EzfDs();
		ezfDs.setDsId(dataForm.getDsId());
		ezfDs = this.ezfDsService.selectByUniqueKey(ezfDs).getValueEmptyThrowMessage();		
		
		/*
		 * 取得資料來源
		 */		
		NamedParameterJdbcTemplate jdbcTemplate = this.getJdbcTemplate(ezfDs);
		if (null == jdbcTemplate) {
			throw new ServiceException("無法取得取得資料來源");
		}		
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		
		// --------------------------------------------------------------------------------------------------------------
		/*
		 * 處理更新 fetchProcInstanceWithSerialNo
		 */
		try {
			this.processState(ezfDs, dataForm);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// --------------------------------------------------------------------------------------------------------------
		
		
		this.ezfMapperLogicService.fillEzfMapDataForm(dataForm);
		
		/*
		 * 查詢看有沒有需要處理的資料
		 */
		Map<String, String> gridSqlTempMap = new HashMap<String, String>();
		String selectMasterTableSql = this.getSelectMasterCommand(ezfDs.getDriverType(), dataForm.getMainTbl(), dataForm.getEfgpProcessStatusField());
		List<Map<String, Object>> queryMasterList = jdbcTemplate.queryForList(selectMasterTableSql, paramMap);
		
		for (Map<String, Object> mData : queryMasterList) {
			for (int g = 0; dataForm.getGrids() != null && g < dataForm.getGrids().size(); g++) {
				EzfMapGrd dGrid = dataForm.getGrids().get(g);
				if ( dGrid.getTblmps() == null || CollectionUtils.isEmpty(dGrid.getTblmps()) ) {
					throw new ServiceException("無配置EzfMapGrdTblMp,無法處理明細資料");
				}
				EzfMapGrdTblMp tblMp = dGrid.getTblmps().get(0); // 目前只會有一筆 EzfMapGrdTblMp 配置
				if (gridSqlTempMap.get(dGrid.getGridId()) == null) {
					gridSqlTempMap.put(dGrid.getGridId(), this.getSelectDetailCommand(ezfDs.getDriverType(), dGrid.getDtlTbl(), tblMp.getDtlFieldName(), tblMp.getMstFieldName()));
				}
				String selectDetailTableSql = gridSqlTempMap.get(dGrid.getGridId());
				paramMap.clear();
				paramMap.put(tblMp.getDtlFieldName(), Ognl.getValue(tblMp.getMstFieldName(), mData));
				List<Map<String, Object>> queryDetailList = jdbcTemplate.queryForList(selectDetailTableSql, paramMap);
				
				//System.out.println("-----------------------------------------------------------------");
				//System.out.println("selectDetailTableSql>>>" +selectDetailTableSql);
				//System.out.println(queryDetailList);
				
			}			
		}		
		
		
		/*
		 * 將 inpForm 內容處理轉為 EzForm, EzFormField, EzFormRecord, EzFormRecordItem
		 */
		
		
		
		logger.info(this.getClass().getSimpleName() + " >>> CNF_ID: " + this.cnfId + " - process end...");
	}
	
	private NamedParameterJdbcTemplate getJdbcTemplate(EzfDs ezfDs) throws Exception {
		if (!ManualDataSourceUtils.isRunning(ezfDs.getDsId())) {
			ManualDataSourceUtils.create(ezfDs.getDsId(), DsDriverType.getDriverClassName(ezfDs.getDriverType()), ezfDs.getDbUser(), ezfDs.getDbPasswd(), ezfDs.getDbAddr());
		}
		return ManualDataSourceUtils.getJdbcTemplate(ezfDs.getDsId());
	}	
	
	private NamedParameterJdbcTemplate getJdbcTemplate(String dsId) throws Exception {
		if (!ManualDataSourceUtils.isRunning(dsId)) {
			EzfDs ezfDs = new EzfDs();
			ezfDs.setDsId(dsId);
			ezfDs = this.ezfDsService.selectByUniqueKey(ezfDs).getValueEmptyThrowMessage();
			ManualDataSourceUtils.create(ezfDs.getDsId(), DsDriverType.getDriverClassName(ezfDs.getDriverType()), ezfDs.getDbUser(), ezfDs.getDbPasswd(), ezfDs.getDbAddr());
		}
		return ManualDataSourceUtils.getJdbcTemplate(dsId);
	}
	
	private String getUpdateMasterStateCommand(String dsDriverType, String tableName, String efgpProcessStatusField, String efgpProcessNoField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		paramMap.put("efgpProcessNoField", efgpProcessNoField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", this.getClass().getClassLoader(), "org/qifu/core/runnable/update_master.ftl", paramMap);		
	}
	
	private String getSelectMasterStateCommand(String dsDriverType, String tableName, String efgpProcessStatusField, String efgpProcessNoField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		paramMap.put("efgpProcessNoField", efgpProcessNoField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", this.getClass().getClassLoader(), "org/qifu/core/runnable/select_master_sign_state.ftl", paramMap);
	}	
	
	private String getSelectMasterCommand(String dsDriverType, String tableName, String efgpProcessStatusField) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("mainTbl", tableName);
		paramMap.put("efgpProcessStatusField", efgpProcessStatusField);
		return TemplateUtils.processTemplate("getSelectMasterCommand", this.getClass().getClassLoader(), "org/qifu/core/runnable/select_master.ftl", paramMap);
	}
	
	private String getSelectDetailCommand(String dsDriverType, String tableName, String detailTableFieldName, String masterTableFieldName) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverType", dsDriverType);
		paramMap.put("dtlTbl", tableName);
		paramMap.put("dtlFieldName", detailTableFieldName);
		paramMap.put("mstFieldName", masterTableFieldName);
		return TemplateUtils.processTemplate("getSelectDetailCommand", this.getClass().getClassLoader(), "org/qifu/core/runnable/select_detail.ftl", paramMap);
	}
	
}
