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
import org.qifu.utils.EZFlowWebServiceUtils;
import org.qifu.utils.EZFormSupportUtils;
import org.qifu.vo.EzForm;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EzFormMapperConfigController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Override
	public String viewPageNamespace() {
		return "ezf_conf";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
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
	
	private void loadProcessPackageIdFromEFGP(DefaultControllerJsonResultObj<EzForm> result, EzForm form) throws AuthorityException, ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<EzForm> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("pProcessPackageId", form, "@org.apache.commons.lang3.StringUtils@isBlank(efgpProcessPackageId)", "請輸入EFGP流程編號!").throwMessage();
		String pProcessPackageId = form.getEfgpProcessPackageId();
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
		
		form = EZFormSupportUtils.loadFromXml(formXml);
		form.setEfgpProcessPackageId(pProcessPackageId);
		result.setValue(form);
		result.setSuccess( YES );
		result.setMessage( "載入EFGP流程樣板完成!" );
		//System.out.println( form.toString() );
	}
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0001A")
	@RequestMapping(value = "/ezfMapEfgpPackageIdLoadJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzForm> doEfgpPackageIdLoadJson(HttpServletRequest request, EzForm form) {
		DefaultControllerJsonResultObj<EzForm> result = this.getDefaultJsonResult(this.currentMethodAuthority());
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
