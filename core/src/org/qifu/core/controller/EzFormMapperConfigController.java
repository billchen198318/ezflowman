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
package org.qifu.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryParamBuilder;
import org.qifu.base.model.SearchBody;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.entity.EzfMap;
import org.qifu.core.entity.EzfMapField;
import org.qifu.core.entity.EzfMapGrd;
import org.qifu.core.entity.EzfMapGrdTblMp;
import org.qifu.core.logic.IEzfMapperLogicService;
import org.qifu.core.logic.IEzfTaskSchedService;
import org.qifu.core.runnable.EzfTaskRunnable;
import org.qifu.core.service.IEzfDsService;
import org.qifu.core.service.IEzfMapFieldService;
import org.qifu.core.service.IEzfMapGrdService;
import org.qifu.core.service.IEzfMapGrdTblMpService;
import org.qifu.core.service.IEzfMapService;
import org.qifu.utils.EZFlowWebServiceUtils;
import org.qifu.utils.EZFormSupportUtils;
import org.qifu.vo.EzForm;
import org.qifu.vo.EzFormField;
import org.qifu.vo.EzFormRecord;
import org.qifu.vo.EzFormRecordItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EzFormMapperConfigController extends BaseControllerSupport implements IPageNamespaceProvide {
	
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
	
	@Autowired
	IEzfMapperLogicService ezfMapperLogicService;
	
	@Autowired
	IEzfTaskSchedService ezfTaskSchedService;
	
	@Override
	public String viewPageNamespace() {
		return "ezf_conf";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("dsMap", this.ezfDsService.getSelectMap(true));
		if ("mainPage".equals(type)) {
			
		}
		if ("createPage".equals(type)) {
			
		}
	}
	
	@SuppressWarnings("unused")
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	private void queryList(DefaultControllerJsonResultObj<List<EzfMap>> result, Map<String, String> param) throws ControllerException, AuthorityException, ServiceException, Exception {
		SearchBody sb = new SearchBody(param);
		Map<String, Object> qParam = QueryParamBuilder.build(sb).endingLike("cnfIdLike").fullLink("cnfNameLike").value();
		if (MapUtils.isEmpty(qParam)) {
			qParam.putAll(param);
		}
		DefaultResult<List<EzfMap>> queryResult = this.ezfMapService.selectListByParams(qParam, "CNF_ID", SortType.ASC);
		this.setDefaultResponseJsonResult(result, queryResult);
		result.setSuccess(YES);
		result.setMessage("查詢完成");
		if (CollectionUtils.isEmpty(result.getValue())) {
			result.setMessage("無資配置筆數");
		}
	}	
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001Q")
	@RequestMapping(value = "/ezfMapQueryJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<List<EzfMap>> doQueryJson(HttpServletRequest request, @RequestBody Map<String, String> param) {
		DefaultControllerJsonResultObj<List<EzfMap>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {			
			this.queryList(result, param);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001Q")
	@RequestMapping("/ezfConfigQueryPage")
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("mainPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001A")
	@RequestMapping("/ezfConfigCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("createPage", mm);
			String editOid = StringUtils.defaultString( request.getParameter("oid") ).trim();
			if (!StringUtils.isBlank(editOid)) {
				mm.put("editOid", editOid);
			}
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	private void loadProcessPackageIdFromEFGP(DefaultControllerJsonResultObj<EzfMap> result, EzfMap form) throws AuthorityException, ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<EzfMap> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("efgpPkgId", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpPkgId)", "請輸入EFGP流程編號!").throwMessage();
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
		
		result.setValue(form);
		result.setSuccess( YES );
		result.setMessage( "載入EFGP流程樣板完成!" );
	}
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001A")
	@RequestMapping(value = "/ezfMapEfgpPackageIdLoadJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfMap> doEfgpPackageIdLoadJson(HttpServletRequest request, @RequestBody EzfMap form) {
		DefaultControllerJsonResultObj<EzfMap> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {			
			this.loadProcessPackageIdFromEFGP(result, form);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	private void checkForCreateOrUpdate(DefaultControllerJsonResultObj<EzfMap> result, EzfMap form) throws ControllerException, AuthorityException, ServiceException, Exception {
		CheckControllerFieldHandler<EzfMap> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("efgpPkgId", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpPkgId)", "請輸入EFGP流程編號")
		.testField("cnfId", form, "@org.apache.commons.lang3.StringUtils@isBlank(cnfId)", "請輸入配置編號")
		.testField("cnfName", form, "@org.apache.commons.lang3.StringUtils@isBlank(cnfName)", "請輸入配置名稱")
		.testField("dsId", form, "@org.qifu.base.model.PleaseSelect@noSelect(dsId)", "請輸入資料來源")
		.testField("mainTbl", form, "@org.apache.commons.lang3.StringUtils@isBlank(mainTbl)", "請輸入映射資料表")
		.testField("efgpProcessStatusField", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpProcessStatusField)", "請輸入簽核狀態欄位名稱")
		.testField("efgpProcessNoField", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpProcessNoField)", "請輸入流程序號欄位名稱")
		.testField("efgpRequesterIdField", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpRequesterIdField)", "請輸入簽核發單人欄位名稱")
		.testField("efgpOrgUnitIdField", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpOrgUnitIdField)", "請輸入簽核發單單位欄位名稱")
		.testField("efgpSubjectScript", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpSubjectScript)", "請輸入簽核發單Subject腳本")
		.testField("cronExpr", form, "@org.apache.commons.lang3.StringUtils@isBlank(cronExpr)", "請輸入排程腳本")
		.throwMessage();		
		
		if (form.getGrids() != null) {
			for (EzfMapGrd grid : form.getGrids()) {
				if (YesNo.NO.equals(grid.getGridId())) { // 因為 ezf_map_field.GRID_ID = 'N' 的代表為表單的欄位, 非grid的欄位, 所以不支持EasyFlowGP 表單設置grid的id為 "N" 的表單欄位
					throw new ControllerException("EFGP流程序號 " + form.getEfgpPkgId() + " 有包含, Grid表格 編號為 N (Grid id is N), 系統無法支持配置");
				}
			}
		}
		
	}
	
	private void save(DefaultControllerJsonResultObj<EzfMap> result, EzfMap form) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkForCreateOrUpdate(result, form);
		DefaultResult<EzfMap> cResult = this.ezfMapperLogicService.create(form);
		this.setDefaultResponseJsonResult(result, cResult);
		if (YES.equals(result.getSuccess())) {
			this.ezfTaskSchedService.removeScheduledTask(form.getCnfId());
			this.ezfTaskSchedService.scheduleTask(form.getCnfId(), new EzfTaskRunnable(form.getCnfId()), form.getCronExpr());			
		}		
	}
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001A")
	@RequestMapping(value = "/ezfMapSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfMap> doSaveJson(HttpServletRequest request, @RequestBody EzfMap form) {
		DefaultControllerJsonResultObj<EzfMap> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, form);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void update(DefaultControllerJsonResultObj<EzfMap> result, EzfMap form) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkForCreateOrUpdate(result, form);
		DefaultResult<EzfMap> uResult = this.ezfMapperLogicService.update(form);
		this.setDefaultResponseJsonResult(result, uResult);
		if (YES.equals(result.getSuccess())) {
			this.ezfTaskSchedService.removeScheduledTask(form.getCnfId());
			this.ezfTaskSchedService.scheduleTask(form.getCnfId(), new EzfTaskRunnable(form.getCnfId()), form.getCronExpr());
		}
	}	
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001U")
	@RequestMapping(value = "/ezfMapUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfMap> doUpdateJson(HttpServletRequest request, @RequestBody EzfMap form) {
		DefaultControllerJsonResultObj<EzfMap> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, form);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001D")
	@RequestMapping(value = "/ezfMapDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HttpServletRequest request, EzfMap form) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			DefaultResult<Boolean> dResult = this.ezfMapperLogicService.delete(form);
			if (dResult.getValue()) {
				this.ezfTaskSchedService.removeScheduledTask( form.getCnfId() );
			}			
			this.setDefaultResponseJsonResult(result, dResult);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}			
	
	private void handlerLoadData(DefaultControllerJsonResultObj<EzfMap> result, EzfMap paramForm) throws ControllerException, AuthorityException, ServiceException, Exception {
		EzfMap dataForm = this.ezfMapService.selectByPrimaryKey(paramForm.getOid()).getValueEmptyThrowMessage();
		paramForm.setEfgpPkgId( dataForm.getEfgpPkgId() );
		boolean successLoadFormOfEasyFlowGP = false;
		try {
			this.loadProcessPackageIdFromEFGP(result, paramForm);
			successLoadFormOfEasyFlowGP = true;
		} catch (Exception le) {
			le.printStackTrace();
		}
		if (!successLoadFormOfEasyFlowGP) {
			// 無法讀取 EasyFlowGP 流程內容 (findFormOIDsOfProcess)			
			this.prepareLoadDataNoWithFindFormOIDsOfProcess(result, dataForm);
		} else {
			// 配置 findFormOIDsOfProcess 取出的 form 內容
			this.prepareLoadDataWithFindFormOIDsOfProcess(result, paramForm, dataForm);
		}	
	}
	
	private void prepareLoadDataNoWithFindFormOIDsOfProcess(DefaultControllerJsonResultObj<EzfMap> result, EzfMap dataForm) throws ControllerException, AuthorityException, ServiceException, Exception {
		result.setMessage("無法讀取 EasyFlowGP 流程內容 (findFormOIDsOfProcess)");
		this.fillEzfMapDataForm(dataForm);		
		result.setValue(dataForm);
		result.setSuccess( YES );
	}
	
	private void prepareLoadDataWithFindFormOIDsOfProcess(DefaultControllerJsonResultObj<EzfMap> result, EzfMap inpForm, EzfMap dataForm) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.fillEzfMapDataForm(dataForm);
		result.setValue(inpForm);
		result.setMessage("載入完成");
		result.setSuccess( YES );
		
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
				if (iField.getGridId().equals(NO) && jField.getGridId().equals(NO) && iField.getFormField().equals(jField.getFormField())) {
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
		
	}
	
	private void fillEzfMapDataForm(EzfMap dataForm) throws ControllerException, AuthorityException, ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		// 填入 List<EzfMapField> fields , gridId = 'N'
		paramMap.put("cnfId", dataForm.getCnfId());
		paramMap.put("gridId", NO);
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
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001A")
	@RequestMapping(value = "/ezfMapLoadJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfMap> doLoad(HttpServletRequest request, EzfMap form) {
		DefaultControllerJsonResultObj<EzfMap> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.handlerLoadData(result, form);			
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
}
