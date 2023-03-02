package org.qifu.core.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryParamBuilder;
import org.qifu.base.model.SearchBody;
import org.qifu.base.model.SortType;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.service.IEzfDsService;
import org.qifu.model.DsDriverType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EzDsConfigController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IEzfDsService<EzfDs, String> ezfDsService;
	
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
		this.getCheckControllerFieldHandler(result)
		.testField("inp_dsId", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dsId)", "請輸入編號!")
		.testField("inp_dsName", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dsName)", "請輸入名稱!")
		.testField("inp_driveType", ds, "@org.qifu.base.model.PleaseSelect@noSelect(driverType)", "請選擇Driver類別!")
		.testField("inp_dbAddr", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dbAddr)", "請輸入JDBC Url!")
		.testField("inp_dbUser", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dbUser)", "請輸入帳戶!")
		.testField("inp_dbPasswd", ds, "@org.apache.commons.lang3.StringUtils@isBlank(dbPasswd)", "請輸入密碼!")
		.throwMessage();
	}	
	
	private void save(DefaultControllerJsonResultObj<EzfDs> result, EzfDs ds) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkForCreateOrUpdate(result, ds);
		if (DsDriverType.mariaDB.equals(ds.getDriverType())) { // 3
			Class.forName("org.mariadb.jdbc.Driver");
		} else if (DsDriverType.msSqlServer.equals(ds.getDriverType())) { // 1
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} else { // 2 oracle
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		try (Connection conn = DriverManager.getConnection(ds.getDbAddr(), ds.getDbUser(), ds.getDbPasswd())) {
			DefaultResult<EzfDs> sResult = this.ezfDsService.insert(ds);
			this.setDefaultResponseJsonResult(result, sResult);
		}
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
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009A")
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
	
}
