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

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ScriptTypeCode;
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
import org.qifu.model.DsDriverType;
import org.qifu.model.EFGPSimpleProcessInfoState;
import org.qifu.model.EzfTaskResource;
import org.qifu.util.ScriptExpressionUtils;
import org.qifu.util.SimpleUtils;
import org.qifu.utils.EZFlowWebServiceUtils;
import org.qifu.utils.EZFormSupportUtils;
import org.qifu.utils.ManualDataSourceUtils;
import org.qifu.vo.EzForm;
import org.qifu.vo.EzFormField;
import org.qifu.vo.EzFormGrid;
import org.qifu.vo.EzFormRecord;
import org.qifu.vo.EzFormRecordItem;
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
		String queryStateSql = EzfTaskResource.getSelectMasterStateCommand(ezfDs.getDriverType(), dataForm.getMainTbl(), dataForm.getEfgpProcessStatusField(), dataForm.getEfgpProcessNoField());
		List<Map<String, Object>> checkStateList = jdbcTemplate.queryForList(queryStateSql, paramMap);	
		for (Map<String, Object> m : checkStateList) {
			Object efgpProcessNoVal = Ognl.getValue(dataForm.getEfgpProcessNoField(), m);
			Object pkFieldVal = m.get(dataForm.getMainTblPkField());
			if (null == pkFieldVal) {
				logger.warn("資料錯誤,主表: " + dataForm.getMainTbl() + " , 主鍵: " + dataForm.getMainTblPkField() + " 沒有獲得PK值, 資料無法處理(processState)");
				continue;
			}			
			if (!StringUtils.isBlank((String)efgpProcessNoVal)) {
				String efgpSimpleProcessInfoXml = "";
				try {
					efgpSimpleProcessInfoXml = StringUtils.defaultString(EZFlowWebServiceUtils.fetchProcInstanceWithSerialNo((String)efgpProcessNoVal));
				} catch (Exception e) {
					e.printStackTrace();
					if (StringUtils.defaultString(e.getMessage()).indexOf("com.dsc.nana.services.exception.WSException") > -1) {
						this.updateMasterTableState(ezfDs, dataForm, EFGPSimpleProcessInfoState.EZFLOW_EXCEPTION_STATE, pkFieldVal);
						continue;
					}
				}
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
					this.updateMasterTableState(ezfDs, dataForm, stateVal, pkFieldVal);
				}
			}
		}
	}
	
	private void updateMasterTableState(EzfDs ezfDs, EzfMap dataForm, Object stateVal, Object pkFieldVal) throws ServiceException, Exception {
		NamedParameterJdbcTemplate jdbcTemplate = this.getJdbcTemplate(ezfDs);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String updateStateSql = EzfTaskResource.getUpdateMasterStateCommand(ezfDs.getDriverType(), dataForm.getMainTbl(), dataForm.getMainTblPkField(), dataForm.getEfgpProcessStatusField());
		paramMap.clear();
		paramMap.put("stateVal", stateVal);
		paramMap.put("mainTblPkFieldVal", pkFieldVal);
		//paramMap.put("pProcessInstanceSerialNo", efgpProcessNoVal);
		jdbcTemplate.update(updateStateSql, paramMap);		
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
		Map<String, String> formFieldTemplateMap = new HashMap<String, String>();
		Map<String, String> gridSqlTempMap = new HashMap<String, String>();
		String selectMasterTableSql = EzfTaskResource.getSelectMasterCommand(ezfDs.getDriverType(), dataForm.getMainTbl(), dataForm.getEfgpProcessStatusField());
		List<Map<String, Object>> queryMasterList = jdbcTemplate.queryForList(selectMasterTableSql, paramMap);		
		for (Map<String, Object> mData : queryMasterList) {		
			Object pkFieldVal = mData.get(dataForm.getMainTblPkField());
			if (null == pkFieldVal) {
				logger.warn("資料錯誤,主表: " + dataForm.getMainTbl() + " , 主鍵: " + dataForm.getMainTblPkField() + " 沒有獲得PK值, 資料無法處理(process)");
				continue;
			}
			String formXml = formFieldTemplateMap.get(dataForm.getEfgpPkgId());
			String formOid = EZFlowWebServiceUtils.findFormOIDsOfProcess( dataForm.getEfgpPkgId() );
			if (StringUtils.isBlank(formOid)) {
				throw new ServiceException("無法取得暫存流程表單OID");
			}
			if (formOid.indexOf(",")>-1) { // findFormOIDsOfProcess 有可能帶回多筆 oid 所以取最後一筆
				String arr[] = formOid.split(",");
				formOid = arr[arr.length-1];
			}			
			if (StringUtils.isBlank(formXml)) {	// 不需要每次都重新取getFormFieldTemplate
				formXml = EZFlowWebServiceUtils.getFormFieldTemplate(formOid);
				if (StringUtils.isBlank(formXml)) {
					throw new ServiceException("無法取得流程表單樣板xml");
				}		
				formFieldTemplateMap.put(dataForm.getEfgpPkgId(), formXml);
			}
			
			/*
			 * 將內容處理轉為 EzForm, EzFormField, EzFormRecord, EzFormRecordItem
			 */			
			EzForm ezform = EZFormSupportUtils.loadFromXml(formXml);
			if ( CollectionUtils.isEmpty(ezform.getFields()) ) {
				logger.warn("無可處理填入的表單,流程: " + dataForm.getEfgpPkgId());
				continue;
			}
			for (EzFormField fField : ezform.getFields() ) {
				for (EzfMapField dField : dataForm.getFields()) {
					if ( fField.getId().equals(dField.getFormField()) ) {
						fField.setText( this.fieldValue(Ognl.getValue(dField.getTblField(), mData), false) );
					}
				}
			}
			// 填入表單Grid資料
			if (CollectionUtils.isEmpty(ezform.getGrids())) {
				logger.warn("EzFormGrid沒有可處理之Grid: " + ezform.getFormId());
				continue;
			}
			
			// --------------------------------------------------------------------------------------

			List<EzFormGrid> cnfGridList = new LinkedList<EzFormGrid>();// 保留配置需要用的
			for (EzFormGrid fgGrid : ezform.getGrids()) {
				EzFormGrid cnfGrid = new EzFormGrid();
				cnfGrid.setGridId(fgGrid.getGridId());
				for (EzFormRecord gRecord : fgGrid.getRecords()) {
					EzFormRecord cnfRecord = new EzFormRecord();
					cnfRecord.setRecordId(gRecord.getRecordId());
					cnfGrid.getRecords().add(cnfRecord);
					for (EzFormRecordItem gItem : gRecord.getItems()) {
						EzFormRecordItem cnfItem = new EzFormRecordItem();
						cnfItem.setId(gItem.getId());
						cnfItem.setDataType(gItem.getDataType());
						cnfItem.setPerDataProId(gItem.getPerDataProId());
						cnfItem.setText(gItem.getText());
						cnfRecord.getItems().add(cnfItem);
					}
				}
				cnfGridList.add(cnfGrid);
			}
			for (int gx = 0; ezform.getGrids() != null && gx < ezform.getGrids().size(); gx++) { // 請除grid 的 items
				EzFormGrid ezGrid = ezform.getGrids().get(gx);
				for (int rx = 0; ezGrid.getRecords() != null && rx < ezGrid.getRecords().size(); rx++) {
					ezGrid.getRecords().get(rx).getItems().clear();
					ezGrid.getRecords().clear();
				}
			}	
			// --------------------------------------------------------------------------------------
			
			for (int x = 0; cnfGridList != null && x < cnfGridList.size(); x++) {
				EzFormGrid ezGrid = ezform.getGrids().get(x);
				final EzFormGrid cnfGrid = cnfGridList.get(x);
				if (CollectionUtils.isEmpty(cnfGrid.getRecords())) {
					logger.warn("EzFormGrid沒有Records,無法處理: " + cnfGrid.getGridId());
					continue;
				}
				for (int g = 0; dataForm.getGrids() != null && g < dataForm.getGrids().size(); g++) {
					EzfMapGrd dGrid = dataForm.getGrids().get(g);
					if (!cnfGrid.getGridId().equals(dGrid.getGridId())) {
						continue;
					}
					if (CollectionUtils.isEmpty(dGrid.getItems())) {
						logger.warn("EzfMapGrd.field沒有配置,無法處理GridId: " + dGrid.getGridId());
						continue;
					}
					if ( dGrid.getTblmps() == null || CollectionUtils.isEmpty(dGrid.getTblmps()) ) {
						throw new ServiceException("無配置EzfMapGrdTblMp,無法處理明細資料");
					}
					EzfMapGrdTblMp tblMp = dGrid.getTblmps().get(0); // 目前只會有一筆 EzfMapGrdTblMp 配置
					if (gridSqlTempMap.get(dGrid.getGridId()) == null) {
						gridSqlTempMap.put(dGrid.getGridId(), EzfTaskResource.getSelectDetailCommand(ezfDs.getDriverType(), dGrid.getDtlTbl(), tblMp.getDtlFieldName(), tblMp.getMstFieldName()));
					}
					String selectDetailTableSql = gridSqlTempMap.get(dGrid.getGridId());
					paramMap.clear();
					paramMap.put(tblMp.getDtlFieldName(), Ognl.getValue(tblMp.getMstFieldName(), mData));
					List<Map<String, Object>> queryDetailList = jdbcTemplate.queryForList(selectDetailTableSql, paramMap);
					if (CollectionUtils.isEmpty(queryDetailList)) {
						logger.warn( "沒有明細資料, 明細表: " + dGrid.getDtlTbl() + " , 條件欄位: " + tblMp.getDtlFieldName() + " , 值: " + paramMap.get(tblMp.getDtlFieldName()) );
						continue;
					}					
					
					List<EzfMapField> dGridItemList = dGrid.getItems();
					
					// 填寫Grid items
					for (EzFormRecord cRecord : cnfGrid.getRecords()) {						 
						if (CollectionUtils.isEmpty(cRecord.getItems())) {
							logger.warn("EzFormRecord沒有items,無法處理: " + cnfGrid.getGridId() + " , recordId: " + cRecord.getRecordId());
							continue;
						}
						int recordCount = 0;
						for (Map<String, Object> dtlDataMap : queryDetailList) {	
							EzFormRecord dRecord = new EzFormRecord();
							dRecord.setRecordId( cnfGrid.getGridId() + "_" + recordCount);
							for (EzFormRecordItem oriItem : cRecord.getItems()) {
								for (EzfMapField ezfMapGridField : dGridItemList) {
									if (ezfMapGridField.getFormField().equals(oriItem.getId())) {
										EzFormRecordItem rItem = new EzFormRecordItem();
										BeanUtils.copyProperties(rItem, oriItem);										
										rItem.setText( StringUtils.defaultString( this.fieldValue(Ognl.getValue(ezfMapGridField.getTblField(), dtlDataMap), rItem.getDataType(), true) ) );
										dRecord.getItems().add(rItem);
									}
								}									
							}
							if (dRecord.getItems().size() > 0) {
								ezGrid.getRecords().add(dRecord);
								recordCount++;
							}
						}
					}	
					
				}
				
			}
			
			try {
				this.processInvokeForm(formOid, dataForm, ezfDs, ezform, mData);
			} catch (ServiceException e) {
				e.printStackTrace();
				this.updateMasterTableState(ezfDs, dataForm, EFGPSimpleProcessInfoState.EZFLOW_EXCEPTION_STATE, pkFieldVal);
			} catch (Exception e) {
				e.printStackTrace();
				this.updateMasterTableState(ezfDs, dataForm, EFGPSimpleProcessInfoState.EZFLOW_EXCEPTION_STATE, pkFieldVal);
			}
			
		}
		
		logger.info(this.getClass().getSimpleName() + " >>> CNF_ID: " + this.cnfId + " - process end...");
	}
	
	private String fieldValue(Object valueObj, boolean normalizeSpace) throws Exception {
		return this.fieldValue(valueObj, "", normalizeSpace);
	}
	
	private String fieldValue(Object valueObj, String dataType, boolean normalizeSpace) throws Exception {
		if (null == valueObj) {
			return "";
		}
		if ("java.util.Date".equals(dataType)) {
			if (valueObj instanceof Date) {
				return SimpleUtils.getStrYMD((Date)valueObj, "/");
			}
		}
		String str = StringEscapeUtils.escapeHtml4( StringEscapeUtils.escapeXml11( String.valueOf(valueObj) ) );
		if (normalizeSpace) {
			str = StringUtils.normalizeSpace(str);
		}
		return str;
	}
	
	private void processInvokeForm(String formOIDsOfProcess, EzfMap dataForm, EzfDs ds, EzForm form, Map<String, Object> masterTableDataMap) throws ServiceException, Exception {
		String subjectParamVar = "_subject_val_" + SimpleUtils.createRandomString(7);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String formXml = EZFormSupportUtils.loadXmlFromObject(form);
		String scriptText = subjectParamVar + "=" + dataForm.getEfgpSubjectScript();
		resultMap.put(subjectParamVar, "");
		/*
		System.out.println("-----------------------------------------------------------------");
		System.out.println(formXml);
		System.out.println("-----------------------------------------------------------------");	
		*/
		logger.info("轉出xml>>>");
		logger.info(formXml);
		ScriptExpressionUtils.execute(ScriptTypeCode.GROOVY, scriptText, resultMap, masterTableDataMap);
		String subjectText = String.valueOf( resultMap.get(subjectParamVar) );
		String processSerialNumber = EZFlowWebServiceUtils.invokeProcess(
				dataForm.getEfgpPkgId(), 
				String.valueOf(masterTableDataMap.get(dataForm.getEfgpRequesterIdField())), 
				String.valueOf(masterTableDataMap.get(dataForm.getEfgpOrgUnitIdField())), 
				formOIDsOfProcess,
				formXml, 
				subjectText);
		processSerialNumber = StringUtils.defaultString(processSerialNumber).trim();
		if (!processSerialNumber.startsWith(dataForm.getEfgpPkgId())) {
			throw new ServiceException("processInvokeForm 錯誤的流程序號");
		}
		logger.info("processInvokeForm 流程序號: " + processSerialNumber);
		
		// 更新 MAIN_TBL 紀錄的table的 EFGP_PROCESS_STATUS_FIELD / EFGP_PROCESS_NO_FIELD 對印的欄位
		String updateSql = EzfTaskResource.getUpdateMasterProcessNoAndStateCommand(ds.getDriverType(), dataForm.getMainTbl(), dataForm.getMainTblPkField(), dataForm.getEfgpProcessNoField(), dataForm.getEfgpProcessStatusField());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stateVal", EFGPSimpleProcessInfoState.getValue(EFGPSimpleProcessInfoState.OPEN_RUNNING));
		paramMap.put("processNoVal", processSerialNumber);
		paramMap.put("mainTblPkFieldVal", masterTableDataMap.get(dataForm.getMainTblPkField()));
		NamedParameterJdbcTemplate jdbcTemplate = this.getJdbcTemplate(ds);
		jdbcTemplate.update(updateSql, paramMap);
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
	
}
