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
package org.qifu.core.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseLogicService;
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
import org.qifu.utils.EZFlowWebServiceUtils;
import org.qifu.utils.EZFormSupportUtils;
import org.qifu.utils.ManualDataSourceUtils;
import org.qifu.vo.EzForm;
import org.qifu.vo.EzFormField;
import org.qifu.vo.EzFormRecord;
import org.qifu.vo.EzFormRecordItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ServiceAuthority(check = false)
@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfMapperLogicServiceImpl extends BaseLogicService implements IEzfMapperLogicService {
	protected Logger logger = LogManager.getLogger(EzfMapperLogicServiceImpl.class);
	
	private final int MAX_EZF_MAP_RECORD = 16;
	
	@Autowired
	IEzfDsService<EzfDs, String> ezfDsService;
	
	@Autowired
	IEzfMapService<EzfMap, String> ezfMapService;
	
	@Autowired
	IEzfMapFieldService<EzfMapField, String> ezfMapFieldService;	
	
	@Autowired
	IEzfMapGrdService<EzfMapGrd, String> ezfMapGrdService;
	
	@Autowired
	IEzfMapGrdTblMpService<EzfMapGrdTblMp, String> ezfMapGrdTblMpService;
	
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} ) 	
	@Override
	public DefaultResult<Boolean> deleteDs(EzfDs ds) throws ServiceException, Exception {
		if (null == ds || this.isBlank(ds.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		ds = this.ezfDsService.selectByPrimaryKey(ds.getOid()).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dsId", ds.getDsId());
		if ( this.ezfMapService.count(paramMap) > 0 ) {
			throw new ServiceException(ds.getDsId() + " 正在被使用,無法刪除!");
		}		
		ManualDataSourceUtils.remove(ds.getDsId());
		return this.ezfDsService.delete(ds);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<EzfMap> create(EzfMap form) throws ServiceException, Exception {
		if (null == form) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		EzfDs ds = new EzfDs();
		ds.setDsId( form.getDsId() );
		ds = this.ezfDsService.selectByUniqueKey(ds).getValueEmptyThrowMessage();
		
		if (this.isBlank(form.getEfgpPkgId())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("efgpPkgId", form.getEfgpPkgId());
		if (this.ezfMapService.count(paramMap) > 0) {
			throw new ServiceException("EFGP流程序號 " + form.getEfgpPkgId() + " 已存在,不可建立重複配置.");
		}
		
		paramMap.clear();
		if (this.ezfMapService.count(paramMap) >= MAX_EZF_MAP_RECORD) {
			throw new ServiceException( "最多允許" + MAX_EZF_MAP_RECORD + "個配置" );
		}
		
		DefaultResult<EzfMap> mResult = this.ezfMapService.insert(form);
		form = mResult.getValueEmptyThrowMessage();
		this.createDetail(form);		
		
		return mResult;
	}

	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<EzfMap> update(EzfMap form) throws ServiceException, Exception {
		if (null == form || this.isBlank(form.getOid())) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		EzfMap beforeEzfMap = this.ezfMapService.selectByPrimaryKey(form.getOid()).getValueEmptyThrowMessage();
		if (!beforeEzfMap.getEfgpPkgId().equals(form.getEfgpPkgId())) {
			throw new ServiceException( "更新時EFGP流程序號 " + form.getEfgpPkgId() + " 與之前建立資料不匹配." );
		}
		this.deleteGridConfig(beforeEzfMap);
		this.deleteGridMasterAndDetailTableMapperConfig(beforeEzfMap);
		this.deleteFormFieldAndGridField(beforeEzfMap);
		this.createDetail(form);			
		return this.ezfMapService.update(form);
	}
	
	private void createDetail(EzfMap form) throws ServiceException, Exception {
		for (EzfMapGrd grid : form.getGrids()) {					
			if (StringUtils.isBlank(grid.getDtlTbl())) { // 沒有detail 資料表, 不處理grid配置
				logger.warn(grid.getGridId() + " 沒有輸入 Sub table");
				continue;
			}
			if (YesNo.NO.equals(grid.getGridId())) { // 因為 ezf_map_field.GRID_ID = 'N' 的代表為表單的欄位, 非grid的欄位, 所以不支持EasyFlowGP 表單設置grid的id為 "N" 的表單欄位
				throw new ServiceException("EFGP流程序號 " + form.getEfgpPkgId() + " 有包含, Grid表格 編號為 N (Grid id is N), 系統無法支持配置");				
			}
			grid.setCnfId( form.getCnfId() );
			grid = this.ezfMapGrdService.insert(grid).getValueEmptyThrowMessage();
			for (EzfMapField field : grid.getItems()) {
				if (StringUtils.isBlank(field.getTblField())) {
					continue;
				}
				field.setGridId(grid.getGridId());
				field.setCnfId(form.getCnfId());
				this.ezfMapFieldService.insert(field);
			}
			for (EzfMapGrdTblMp tblMp : grid.getTblmps()) {
				if (StringUtils.isBlank(tblMp.getMstFieldName()) || StringUtils.isBlank(tblMp.getDtlFieldName())) {
					throw new ServiceException(grid.getGridId() + " 沒有輸入主表與明細表where條件欄位");
				}
				tblMp.setGridId(grid.getGridId());
				tblMp.setCnfId(form.getCnfId());				
				this.ezfMapGrdTblMpService.insert(tblMp);
			}
		}
		int baseRecord = 0;
		for (EzfMapField field : form.getFields()) {
			if (StringUtils.isBlank(field.getTblField())) {
				continue;
			}
			field.setGridId(YesNo.NO);
			field.setCnfId(form.getCnfId());			
			this.ezfMapFieldService.insert(field);
			baseRecord++;
		}
		if (baseRecord < 1) {
			throw new ServiceException( "最少需輸入一筆field對應配置" );
		}		
	}
	
	private void deleteFormFieldAndGridField(EzfMap form) throws ServiceException, Exception {
		if (null == form || this.isBlank(form.getOid()) || this.isBlank(form.getCnfId())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cnfId", form.getCnfId());
		List<EzfMapField> searchList = this.ezfMapFieldService.selectListByParams(paramMap).getValue();
		for (EzfMapField f : searchList) {
			this.ezfMapFieldService.delete(f);
		}
	}
	
	private void deleteGridConfig(EzfMap form) throws ServiceException, Exception {
		if (null == form || this.isBlank(form.getOid()) || this.isBlank(form.getCnfId())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cnfId", form.getCnfId());
		List<EzfMapGrd> searchList = this.ezfMapGrdService.selectListByParams(paramMap).getValue();
		for (EzfMapGrd g : searchList) {
			this.ezfMapGrdService.delete(g);
		}
	}
	
	private void deleteGridMasterAndDetailTableMapperConfig(EzfMap form) throws ServiceException, Exception {
		if (null == form || this.isBlank(form.getOid()) || this.isBlank(form.getCnfId())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cnfId", form.getCnfId());
		List<EzfMapGrdTblMp> searchList = this.ezfMapGrdTblMpService.selectListByParams(paramMap).getValue();
		for (EzfMapGrdTblMp gmp : searchList) {
			this.ezfMapGrdTblMpService.delete(gmp);
		}
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} ) 	
	@Override
	public DefaultResult<Boolean> delete(EzfMap form) throws ServiceException, Exception {
		if (null == form || this.isBlank(form.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		form = this.ezfMapService.selectByPrimaryKey(form.getOid()).getValueEmptyThrowMessage();
		this.deleteGridConfig(form);
		this.deleteGridMasterAndDetailTableMapperConfig(form);
		this.deleteFormFieldAndGridField(form);
		return this.ezfMapService.delete(form);
	}
	
	public DefaultResult<EzfMap> prepareLoadDataNoWithFindFormOIDsOfProcess(EzfMap dataForm) throws ServiceException, Exception {
		DefaultResult<EzfMap> result = new DefaultResult<EzfMap>(); 
		result.setMessage( BaseSystemMessage.searchNoData() );
		this.fillEzfMapDataForm(dataForm);
		result.setMessage("無法讀取 EasyFlowGP 流程內容 (findFormOIDsOfProcess)");
		result.setValue(dataForm);
		result.setSuccess( YesNo.YES );
		return result;
	}
	
	public DefaultResult<EzfMap> prepareLoadDataWithFindFormOIDsOfProcess(EzfMap inpForm, EzfMap dataForm) throws ServiceException, Exception {
		DefaultResult<EzfMap> result = new DefaultResult<EzfMap>(); 
		result.setMessage( BaseSystemMessage.searchNoData() );
		this.fillEzfMapDataForm(dataForm);
		result.setValue(inpForm);
		result.setMessage("載入完成");
		result.setSuccess( YesNo.YES );
		
		// 處理 dataForm 有的value 放到 inpForm
		//BeanUtils.copyProperties(inpForm, dataForm);
		inpForm.setOid( dataForm.getOid() );
		inpForm.setCnfId( dataForm.getCnfId() );
		inpForm.setCnfName( dataForm.getCnfName() );
		inpForm.setDsId( dataForm.getDsId() );
		inpForm.setEfgpPkgId( dataForm.getEfgpPkgId() );
		inpForm.setMainTbl( dataForm.getMainTbl() );
		inpForm.setEfgpProcessStatusField( dataForm.getEfgpProcessStatusField() );
		inpForm.setEfgpProcessNoField( dataForm.getEfgpProcessNoField() );
		inpForm.setEfgpRequesterIdField( dataForm.getEfgpRequesterIdField() );
		inpForm.setEfgpOrgUnitIdField( dataForm.getEfgpOrgUnitIdField() );
		inpForm.setEfgpSubjectScript( dataForm.getEfgpSubjectScript() );
		inpForm.setCronExpr( dataForm.getCronExpr() );
		
		if (inpForm.getFields() == null) {
			inpForm.setFields( new ArrayList<EzfMapField>() );
		}
		
		// 處理 Field
		for (int i = 0; inpForm.getFields() != null && i < inpForm.getFields().size(); i++) {
			EzfMapField iField = inpForm.getFields().get(i);
			iField.setCnfId( dataForm.getCnfId() );
			for (int j = 0; dataForm.getFields() != null && j < dataForm.getFields().size(); j++) {
				EzfMapField jField = dataForm.getFields().get(j);
				if (iField.getGridId().equals(YesNo.NO) && jField.getGridId().equals(YesNo.NO) && iField.getFormField().equals(jField.getFormField())) {
					iField.setTblField( jField.getTblField() );
				}
			}
		}
		
		// 處理Grid
		for (int g = 0; inpForm.getGrids() != null && g < inpForm.getGrids().size(); g++) {
			EzfMapGrd gGrid = inpForm.getGrids().get(g);
			gGrid.setCnfId( dataForm.getCnfId() );
			for (int j = 0; dataForm.getGrids() != null && j < dataForm.getGrids().size(); j++) {
				EzfMapGrd jGrid = dataForm.getGrids().get(j);
				if (gGrid.getGridId().equals(jGrid.getGridId())) {
					//BeanUtils.copyProperties(gGrid, jGrid);
					gGrid.setOid( jGrid.getOid() );
					gGrid.setDtlTbl( jGrid.getDtlTbl() );
					
					if (gGrid.getItems() == null) {
						gGrid.setItems( new ArrayList<EzfMapField>() );
					}
					if (gGrid.getTblmps() == null) {
						gGrid.setTblmps( new ArrayList<EzfMapGrdTblMp>() );
					}
					
					// 處理Grid的Field
					for (int gf = 0; gGrid.getItems() != null && gf < gGrid.getItems().size(); gf++) {
						EzfMapField gfField = gGrid.getItems().get(gf);
						gfField.setCnfId( dataForm.getCnfId() );
						for (int jf = 0; jGrid.getItems() != null && jf < jGrid.getItems().size(); jf++) {
							EzfMapField jfField = jGrid.getItems().get(jf);
							if (gfField.getGridId().equals(jfField.getGridId()) && gfField.getFormField().equals(jfField.getFormField())) {
								gfField.setTblField( jfField.getTblField() );
							}
						}
					}
					
					// 處理 Tblmps / EzfMapGrdTblMp
					for (int gp = 0; gGrid.getTblmps() != null && gp < gGrid.getTblmps().size(); gp++) {
						EzfMapGrdTblMp gpTblMp = gGrid.getTblmps().get(gp);
						gpTblMp.setCnfId( dataForm.getCnfId() );
						for (int jp = 0; jGrid.getTblmps() != null && jp < jGrid.getTblmps().size(); jp++) {
							EzfMapGrdTblMp jpTblMp = jGrid.getTblmps().get(jp);
							if (gpTblMp.getGridId().equals(jpTblMp.getGridId())) {
								gpTblMp.setMstFieldName( jpTblMp.getMstFieldName() );
								gpTblMp.setDtlFieldName( jpTblMp.getDtlFieldName() );
							}
						}
					}
					
				}
			}
		}
		return result;
	}
	
	public void fillEzfMapDataForm(EzfMap dataForm) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		// 填入 List<EzfMapField> fields , gridId = 'N'
		paramMap.put("cnfId", dataForm.getCnfId());
		paramMap.put("gridId", YesNo.NO);
		dataForm.setFields( this.ezfMapFieldService.selectListByParams(paramMap).getValue() );
		
		// 填入 List<EzfMapGrd> grids
		paramMap.clear();
		paramMap.put("cnfId", dataForm.getCnfId());
		dataForm.setGrids( this.ezfMapGrdService.selectListByParams(paramMap).getValue() );
				
		// 填入 grids -> List<EzfMapField> items , gridId = EzfMapGrd.gridId		
		// 填入 grids -> List<EzfMapGrdTblMp> tblmps 
		for (int g = 0; dataForm.getGrids() != null && g < dataForm.getGrids().size(); g++) {
			EzfMapGrd grid = dataForm.getGrids().get(g);
			paramMap.clear();
			paramMap.put("cnfId", dataForm.getCnfId());
			paramMap.put("gridId", grid.getGridId());
			grid.setItems( this.ezfMapFieldService.selectListByParams(paramMap).getValue() );
			grid.setTblmps( this.ezfMapGrdTblMpService.selectListByParams(paramMap).getValue() );			
		}		
	}

	@Override
	public void getFormFieldTemplateConvert2EzfMap(EzfMap form) throws ServiceException, Exception {
		if (null == form || this.isBlank(form.getEfgpPkgId())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		String pProcessPackageId = form.getEfgpPkgId();
		String formOid = EZFlowWebServiceUtils.findFormOIDsOfProcess(pProcessPackageId);
		if (StringUtils.isBlank(formOid)) {
			throw new ServiceException("無法取得暫存流程表單OID");
		}
		if (formOid.indexOf(",")>-1) { // findFormOIDsOfProcess 有可能帶回多筆 oid 所以取最後一筆
			String arr[] = formOid.split(",");
			formOid = arr[arr.length-1];
		}
		String formXml = EZFlowWebServiceUtils.getFormFieldTemplate(formOid);
		if (StringUtils.isBlank(formXml)) {
			throw new ServiceException("無法取得流程表單樣板xml");
		}
		
		EzForm ezform = EZFormSupportUtils.loadFromXml(formXml);
		
		// 處理頁面要顯示用的欄位
		form.setMainTbl("");
		form.setCronExpr( "0 0/3 * * * ?" );
		if (form.getFields() != null) {
			form.getFields().clear();
		}
		if (form.getGrids() != null) {
			form.getGrids().clear();
		}
		if (ezform != null && ezform.getFields() != null && ezform.getFields().size() > 0) {
			for (EzFormField ezfield : ezform.getFields()) {
				EzfMapField field = new EzfMapField();
				field.setFormField( ezfield.getId() );
				field.setTblField("");
				field.setGridId(YesNo.NO);
				form.getFields().add(field);
			}
		}
		if (ezform != null && ezform.getRecords() != null && ezform.getRecords().size() > 0) {
			for (EzFormRecord ezRecord : ezform.getRecords()) {
				if (ezRecord.getItems() == null) {
					continue;
				}
				EzfMapGrd grid = new EzfMapGrd();
				grid.setDtlTbl("");
				grid.setGridId(ezRecord.getGridId());
				grid.setRecordId(ezRecord.getRecordId());
				form.getGrids().add(grid);
				for (EzFormRecordItem ezRecordItem : ezRecord.getItems()) {
					EzfMapField gridItem = new EzfMapField();
					gridItem.setGridId(grid.getGridId());
					gridItem.setTblField("");
					gridItem.setFormField(ezRecordItem.getId());
					grid.getItems().add(gridItem);
				}
				
				if (grid.getTblmps().size() == 0) { // 最少補一筆 EzfMapGrdTblMp
					EzfMapGrdTblMp tblMp = new EzfMapGrdTblMp();
					tblMp.setCnfId("");
					tblMp.setGridId(grid.getGridId());
					tblMp.setMstFieldName("");
					tblMp.setDtlFieldName("");
					grid.getTblmps().add(tblMp);
				}
				
			}
		}		
	}
	
}
