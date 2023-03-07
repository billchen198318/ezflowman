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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.entity.EzfMap;
import org.qifu.core.entity.EzfMapField;
import org.qifu.core.entity.EzfMapGrd;
import org.qifu.core.service.IEzfDsService;
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
			}
		}
		
		
		result.setValue(form);
		result.setSuccess( YES );
		result.setMessage( "載入EFGP流程樣板完成!" );
		//System.out.println( form.toString() );
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
	
	
}
