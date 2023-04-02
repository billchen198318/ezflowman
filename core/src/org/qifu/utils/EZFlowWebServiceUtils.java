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
	
	public static String fetchProcInstanceWithSerialNo(String pProcessInstanceSerialNo) throws ServiceException, Exception {
		if (StringUtils.isBlank(pProcessInstanceSerialNo)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		return getWorkflowService().fetchProcInstanceWithSerialNo(pProcessInstanceSerialNo);
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
	
	private static String getMemoOrDescription(String srcTxt) {
		if (StringUtils.isBlank(srcTxt)) {
			return "";
		}
		if (srcTxt.length() > 500) {
			srcTxt = srcTxt.substring(0, 500);
		}
		return srcTxt;
	}
	
	public static String invokeProcess(String efgpPkgId, String efgpRequesterId, String efgpOrgUnitId, String formOIDsOfProcess, String formXml, String subject) throws Exception {
		String processSerialNumber = getWorkflowService().invokeProcess(efgpPkgId, efgpRequesterId, efgpOrgUnitId, formOIDsOfProcess, formXml, subject);
		if (StringUtils.isBlank(processSerialNumber)) {
			throw new ServiceException("EasyFlowGP invokeProcess 失敗");
		}
		return processSerialNumber;
	}
	
}
