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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.qifu.core.entity.EzfDs;
import org.qifu.core.logic.IEzfMapperLogicService;
import org.qifu.core.service.IEzfDsService;
import org.qifu.model.DsDriverType;
import org.qifu.utils.ManualDataSourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EzDsConfigController extends BaseControllerSupport implements IPageNamespaceProvide {
	protected static Logger logger = LogManager.getLogger(EzDsConfigController.class);	
	
	@Autowired
	IEzfDsService<EzfDs, String> ezfDsService;
	
	@Autowired
	IEzfMapperLogicService ezfMapperLogicService;
	
	
	@Override
	public String viewPageNamespace() {
		return "ezf_ds";
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
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009A")
	@RequestMapping("/ezfDsConfigPage")
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
	
	private void checkForCreateOrUpdate(DefaultControllerJsonResultObj<EzfDs> result, EzfDs ds) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<EzfDs> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("inp_dsId", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dsId)", "請輸入編號!")
		.testField("inp_dsName", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dsName)", "請輸入名稱!")
		.testField("inp_driveType", ds, "@org.qifu.base.model.PleaseSelect@noSelect(driverType)", "請選擇Driver類別!")
		.testField("inp_dbAddr", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dbAddr)", "請輸入JDBC Url!")
		.testField("inp_dbUser", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dbUser)", "請輸入帳戶!")
		.testField("inp_dbPasswd", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dbPasswd)", "請輸入密碼!")
		.throwMessage();
		chk.testField("inp_dsId", ds, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09(dsId)", "編號必須為0-9,a-z,A-Z等字元!").throwMessage();
		chk.testField("inp_dsId", ds, "@org.qifu.base.model.PleaseSelect@isAllOption(dsId)", "請更改編號代號!").throwMessage();
	}	
	
	private void handlerTestJdbcConnection(EzfDs ds) throws ControllerException, AuthorityException, ServiceException, Exception {
		Class.forName(DsDriverType.getDriverClassName(ds.getDriverType()));
		try (Connection conn = DriverManager.getConnection(ds.getDbAddr(), ds.getDbUser(), ds.getDbPasswd())) {
			logger.info( this.getClass().getSimpleName() + " test " + conn.toString() );
		}
	}
	
	private void save(DefaultControllerJsonResultObj<EzfDs> result, EzfDs ds) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkForCreateOrUpdate(result, ds);
		this.handlerTestJdbcConnection(ds);
		DefaultResult<EzfDs> sResult = this.ezfDsService.insert(ds);
		this.setDefaultResponseJsonResult(result, sResult);
	}	
	
	private void update(DefaultControllerJsonResultObj<EzfDs> result, EzfDs ds) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkForCreateOrUpdate(result, ds);
		this.handlerTestJdbcConnection(ds);
		DefaultResult<EzfDs> uResult = this.ezfDsService.update(ds);
		if (YES.equals(uResult.getSuccess())) {
			ManualDataSourceUtils.remove(ds.getDsId());
		}
		this.setDefaultResponseJsonResult(result, uResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009A")
	@RequestMapping(value = "/ezfDsConfigSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfDs> doSave(HttpServletRequest request, EzfDs ds) {
		DefaultControllerJsonResultObj<EzfDs> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, ds);			
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009U")
	@RequestMapping(value = "/ezfDsConfigUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfDs> doUpdate(HttpServletRequest request, EzfDs ds) {
		DefaultControllerJsonResultObj<EzfDs> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, ds);			
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void queryList(DefaultControllerJsonResultObj<List<EzfDs>> result, Map<String, String> param) throws ControllerException, AuthorityException, ServiceException, Exception {
		SearchBody sb = new SearchBody(param);
		Map<String, Object> qParam = QueryParamBuilder.build(sb).endingLike("dsIdLike").fullLink("dsNameLike").value();
		if (MapUtils.isEmpty(qParam)) {
			qParam.putAll(param);
		}
		DefaultResult<List<EzfDs>> queryResult = this.ezfDsService.selectListByParams(qParam, "DS_ID", SortType.ASC);
		this.setDefaultResponseJsonResult(result, queryResult);
		result.setSuccess(YES);
		result.setMessage("查詢完成");
		if (CollectionUtils.isEmpty(result.getValue())) {
			result.setMessage("無資料來源配置筆數");
		}
	}
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009Q")
	@RequestMapping(value = "/ezfDsConfigQueryJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<List<EzfDs>> doQueryJson(HttpServletRequest request, @RequestBody Map<String, String> param) {
		DefaultControllerJsonResultObj<List<EzfDs>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
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
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009A")
	@RequestMapping(value = "/ezfDsConfigLoadJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfDs> doLoadJson(HttpServletRequest request, EzfDs ds) {
		DefaultControllerJsonResultObj<EzfDs> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {			
			DefaultResult<EzfDs> qResult = this.ezfDsService.selectByPrimaryKey(ds.getOid());
			qResult.getValueEmptyThrowMessage();
			this.setDefaultResponseJsonResult(result, qResult);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009D")
	@RequestMapping(value = "/ezfDsConfigDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HttpServletRequest request, EzfDs ds) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {			
			DefaultResult<Boolean> dResult = this.ezfMapperLogicService.deleteDs(ds);
			this.setDefaultResponseJsonResult(result, dResult);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}			
	
}
