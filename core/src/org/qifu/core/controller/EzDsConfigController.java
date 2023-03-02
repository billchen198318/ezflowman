package org.qifu.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.service.IEzfDsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
		CheckControllerFieldHandler<EzfDs> chk = this.getCheckControllerFieldHandler(result);
		
		/*
		chk
		.testField("applyDate", form, "@org.apache.commons.lang3.StringUtils@isBlank(applyDate)", "請輸入申請日期!")
		.testField("employeeId", form, "@org.apache.commons.lang3.StringUtils@isBlank(employeeId)", "請輸入申請人工號!")
		.testField("carNumber", form, "@org.apache.commons.lang3.StringUtils@isBlank(carNumber)", "請輸入車牌號碼!")
		.testField("startDate", form, "@org.apache.commons.lang3.StringUtils@isBlank(startDate)", "請輸入使用起日!")
		.testField("endDate", form, "@org.apache.commons.lang3.StringUtils@isBlank(endDate)", "請輸入使用迄日!")
		.testField("useTime1hh", form, "@org.apache.commons.lang3.StringUtils@isBlank(useTime1hh)", "請輸入使用時間(起)小時!")
		.testField("useTime2hh", form, "@org.apache.commons.lang3.StringUtils@isBlank(useTime2hh)", "請輸入使用時間(迄)小時!")
		.testField("useTime1mm", form, "@org.apache.commons.lang3.StringUtils@isBlank(useTime1mm)", "請輸入使用時間(起)分鐘!")
		.testField("useTime2mm", form, "@org.apache.commons.lang3.StringUtils@isBlank(useTime2mm)", "請輸入使用時間(迄)分鐘!")		
		.testField("reason", form, "@org.apache.commons.lang3.StringUtils@isBlank(reason)", "請輸入申請原因!")
		;
		chk.throwMessage();
		
		form.setUseTime1( form.getUseTime1hh() + ":" + form.getUseTime1mm() );
		form.setUseTime2( form.getUseTime2hh() + ":" + form.getUseTime2mm() );
		
		chk
		.testField("employeeId", form, "@org.apache.commons.lang3.StringUtils@isBlank(departmentId)", "資料錯誤,無申請人無部門資料!") // 有 employeeId, 卻沒有departmentId有問題,因為資料會透過employeeId取出.
		.throwMessage();
		*/
		
	}	
	
	private void save(DefaultControllerJsonResultObj<EzfDs> result, EzfDs ds) throws ControllerException, AuthorityException, ServiceException, Exception {
		/*
		this.checkForCreateOrUpdate(result, ds);
		DefaultResult<EzfDs> sResult = this.formLogicService.save(ds);		
		this.setDefaultResponseJsonResult(result, sResult);
		*/
	}	
	
	@ControllerMethodAuthority(check = true, programId = "EZF_A001D0009A")
	@RequestMapping(value = "/ezfDsConfigSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<EzfDs> doSave(HttpServletRequest request, EzfDs ds) {
		DefaultControllerJsonResultObj<EzfDs> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			//this.save(result, form);			
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	
}
