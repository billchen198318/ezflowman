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
package org.qifu.utils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.YesNo;
import org.qifu.util.LoadResources;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;

import com.dsc.nana.webservice.TipTopIntegration;
import com.dsc.nana.webservice.TipTopIntegrationServiceLocator;
import com.dsc.nana.webservice.WorkflowService;
import com.dsc.nana.webservice.WorkflowServiceServiceLocator;
import com.dsc.vo.efgp.ouc.ComDscNanaDataTransferOrganizationUnitForInvokingListDTO;
import com.dsc.vo.efgp.ouc.ComDscNanaServicesWebserviceOrganizationUnitCollection;

import ognl.Ognl;
import ognl.OgnlContext;

/**
 * 因 EasyFlow GP webService WSDL/soap 版本太舊了,
 * apache cxf 已經不支持這種舊版本的WSDL/soap, 故改用 axis 來調用EasyFlow GP接口
 */
public class EZFlowWebServiceUtils {
	
	protected static Logger logger = LogManager.getLogger(EZFlowWebServiceUtils.class);	
	
	private static Map<String, String> srcDataMap;	

	private static String hostAddress = "";	
	
	static {
		try {
			srcDataMap = LoadResources.objectMapperReadValue("ezflowwsaddr.json", LinkedHashMap.class, EZFlowWebServiceUtils.class);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			hostAddress = "127.0.0.1";
		}		
	}	
	
	private static WorkflowService getWorkflowService() throws Exception {
		WorkflowServiceServiceLocator wsLocator = new WorkflowServiceServiceLocator();
		wsLocator.setWorkflowServiceEndpointAddress( srcDataMap.get("WorkflowServiceWsdlAddress") );
		WorkflowService ws = wsLocator.getWorkflowService();
		return ws;
	}
	
	private static TipTopIntegration getEFGPTipTopIntegrationService() throws Exception {
		TipTopIntegrationServiceLocator wsLocator = new TipTopIntegrationServiceLocator();
		wsLocator.setTipTopIntegrationEndpointAddress( srcDataMap.get("EFGPTipTopIntegrationServiceWsdlAddress") );
		TipTopIntegration ws = wsLocator.getTipTopIntegration();
		return ws;
	}
	
	public static String findManagerByAppLvlXml(String employeeId, String orgUnitOid, String levelType) throws ServiceException, Exception {
		if (StringUtils.isBlank(employeeId) || StringUtils.isBlank(orgUnitOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		return getWorkflowService().findManagerByAppLvl(employeeId, orgUnitOid, levelType, "FunctionLevel", false);
	}
	
	public static String fetchOrgUnitOfUserIdXml(String employeeId) throws ServiceException, Exception {
		if (StringUtils.isBlank(employeeId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		return getWorkflowService().fetchOrgUnitOfUserId(employeeId);
	}
	
	public static List<ComDscNanaDataTransferOrganizationUnitForInvokingListDTO> fetchOrgUnitOfUserId(String employeeId) throws ServiceException, Exception {
		String resXml = fetchOrgUnitOfUserIdXml(employeeId);
		JAXBContext jaxbContext = JAXBContext.newInstance(ComDscNanaServicesWebserviceOrganizationUnitCollection.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ComDscNanaServicesWebserviceOrganizationUnitCollection result = (ComDscNanaServicesWebserviceOrganizationUnitCollection)
				jaxbUnmarshaller.unmarshal( new ByteArrayInputStream(resXml.getBytes(Constants.BASE_ENCODING)) );
		return result.getOrganizationUnits().getComDscNanaDataTransferOrganizationUnitForInvokingListDTO();
	}
	
	public static String findFormOIDsOfProcess(String processPackageId) throws ServiceException, Exception {
		if (StringUtils.isBlank(processPackageId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		return getWorkflowService().findFormOIDsOfProcess(processPackageId);		
	}
	
	public static String findManagerByAppLvlUserId(String employeeId, String orgUnitOid, String levelType) throws ServiceException, Exception {
		String xml = StringUtils.defaultString( findManagerByAppLvlXml(employeeId, orgUnitOid, levelType) ).trim();
		int f1 = xml.indexOf("<userId>");
		int f2 = xml.indexOf("</userId>");
		if (f1 == -1 || f2 == -1) {
			throw new ServiceException("無法取得EFGP - findManagerByAppLvl");
		}
		String str = xml.substring(f1, f2);
		str = str.replaceAll("<userId>", "");
		str = str.replaceAll("</userId>", "");		
		if (StringUtils.isBlank(str)) {
			throw new ServiceException("無法取得EFGP - findManagerByAppLvl");
		}
		return str;
	}	
	
	// 因為用 JAXB 轉表單xml了 , 所以不需要呼叫這個method
	public static String getFormFieldTemplate(String formOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(formOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		return getWorkflowService().getFormFieldTemplate(formOid);
	}
	
	private static <T> T fillFormElement(T obj, Object value) throws Exception {
		String fieldVal = null;		
		if (null != value) {
			if (value instanceof BigDecimal) {
				fieldVal = ( (BigDecimal) value ).toString();
			}
			if (value instanceof BigInteger) {
				fieldVal = ( (BigInteger) value ).toString();
			}
			if (value instanceof Float || value instanceof Double || value instanceof Integer 
					|| value instanceof Long || value instanceof String) {
				fieldVal = (String) value;
			}			
		}
		String id = obj.getClass().getSimpleName().toLowerCase();
		OgnlContext ognlContext = OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext();		
		Ognl.setValue("id", ognlContext, obj, id);
		Ognl.setValue("dataType", ognlContext, obj, String.class.getName()); // AIMI150 整張表都是 String 欄位
		Ognl.setValue("value", ognlContext, obj, StringEscapeUtils.escapeXml11(StringUtils.defaultString(fieldVal)));
		return (T) obj;
	}
	
	private static String n2b(String value) {
		return StringUtils.defaultString(value);
	}
	
	/*
	private static String getCarFormMemo(AfForm form, AfBuBase bub) throws Exception {
		StringBuilder m = new StringBuilder();
		m.append( "1".equals(form.getType()) ? "◎時碩工業/科技" : "◎外包廠商" ).append("\n");
		m.append( "1".equals(form.getWorkType()) ? "◎外出洽公" : "◎請假" ).append("\n");
		m.append( "1".equals(form.getCarType()) ? "◎公務車（" + bub.getBuName() + "）"  : "◎私車" ).append("\n");
		m.append("【車牌號碼】：").append(form.getCarNumber()).append("\n");
		m.append("【使用日期】：").append(form.getStartDate()).append(" ～ ").append(form.getEndDate()).append("\n");
		m.append("【使用時間】：").append(form.getUseTime1()).append(" ～ ").append(form.getUseTime2()).append("\n");
		m.append("【申請原因說明】：").append("\n");
		m.append(getMemoOrDescription(form.getReason())).append("\n");
		m.append("\n");		
		m.append( YesNo.YES.equals(form.getBagItem()) ? "◎攜帶公物資產外出" : "◎[沒有]攜帶公物資產外出" ).append("\n");
		if (YesNo.YES.equals(form.getBagItem())) {
			m.append("【拜訪廠商名稱】：").append(form.getBagVisit()).append("\n");
			m.append("【公物資產描敘】：").append("\n");
			m.append(getMemoOrDescription(form.getBagDescription())).append("\n");
			m.append("\n");
		}
		return m.toString();
	}
	*/
	
	private static String getMemoOrDescription(String srcTxt) {
		if (StringUtils.isBlank(srcTxt)) {
			return "";
		}
		if (srcTxt.length() > 500) {
			srcTxt = srcTxt.substring(0, 500);
		}
		return srcTxt;
	}
	
	/**
	 * buCar.bu 進入這裡, bu變數已經被取代為中文, 為了要送簽表單顯示用資料. 
	 * 
	 * @param form
	 * @param buCar
	 * @param ezflowMainOrgId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	/*
	public static String createCarFlow(AfForm form, AfBuBase bub, AfBuCar buCar, String buCarEmployeeName, String ezflowMainOrgId, String managerUserIdAndAgentUserId, String noticeUsers) throws ServiceException, Exception {
		if (StringUtils.isBlank(managerUserIdAndAgentUserId)) {
			throw new ServiceException(form.getEmployeeId()+"無法取得副理級或以上之簽核人員配置,請洽IT.");
		}
		if (StringUtils.isBlank(noticeUsers)) {
			throw new ServiceException(form.getEmployeeId()+"無法取得簽核通知人員,請洽IT.");
		}		
		String formXml = carFormXml;
		if (StringUtils.isBlank(formXml)) {
			throw new Exception("無表單樣板xml資料,請聯絡IT單位");
		}
		
		formXml = StringUtils.replaceOnce(formXml, "${formId}", n2b(form.getFormId()));
		formXml = StringUtils.replaceOnce(formXml, "${applyDate}", n2b(form.getApplyDate()));
		formXml = StringUtils.replaceOnce(formXml, "${departmentName}", n2b(form.getDepartmentId()) + "-" + n2b(form.getDepartmentName()));
		formXml = StringUtils.replaceOnce(formXml, "${employeeName}", n2b(form.getEmployeeName()));
		formXml = StringUtils.replaceOnce(formXml, "${memo}", getCarFormMemo(form, bub));
		formXml = StringUtils.replaceOnce(formXml, "${employeeId}", n2b(form.getEmployeeId()));
		formXml = StringUtils.replaceOnce(formXml, "${carNumber}", n2b(form.getCarNumber()));				
		formXml = StringUtils.replaceOnce(formXml, "${carEmployeeId}", n2b(buCar.getCarEmployeeId()));
		formXml = StringUtils.replaceOnce(formXml, "${carEmployeeName}", n2b(buCarEmployeeName));
		formXml = StringUtils.replaceOnce(formXml, "${managerUserId}", n2b(managerUserIdAndAgentUserId));
		formXml = StringUtils.replaceOnce(formXml, "${noticeUsers}", n2b(noticeUsers));
		
		//String flowId = "CARFLOW";
		String flowId = "OCFLOW"; // 2022-12-05 , user不想看到CAR開頭
		
		String formOIDsOfProcess = findFormOIDsOfProcess(flowId);
		String subject = "公務車申請簽核單 - " + n2b(form.getFormId()) + " , 申請人: " + n2b(form.getEmployeeId()) + " - " + n2b(form.getEmployeeName());
		
		String processSerialNumber = getWorkflowService().invokeProcess(flowId, n2b(form.getEmployeeId()), ezflowMainOrgId, formOIDsOfProcess, formXml, subject);
		if (StringUtils.isBlank(processSerialNumber)) {
			throw new ServiceException("EasyFlowGP 簽核發單失敗1,請聯絡IT單位");
		}
		if (!processSerialNumber.startsWith("OCFLOW")) {
			throw new ServiceException("EasyFlowGP 簽核發單失敗2,請聯絡IT單位");
		}
		return processSerialNumber;
	}
	*/
	
}
