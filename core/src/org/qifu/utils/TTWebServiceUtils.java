package org.qifu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.model.YesNo;
import org.qifu.util.LoadResources;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;

import ognl.Ognl;
import ognl.OgnlContext;
import tw.com.dsc.www.tiptop.TIPTOPServiceGateWay.TIPTOPServiceGateWayLocator;
import tw.com.dsc.www.tiptop.TIPTOPServiceGateWay.TIPTOPServiceGateWayPortType;

/**
 * 因 TIPTOP webService WSDL/soap 版本太舊了,
 * apache cxf 已經不支持這種舊版本的WSDL/soap, 故改用 axis 來調用TIPTOP接口
 */
public class TTWebServiceUtils {
	
	protected static Logger logger = LogManager.getLogger(TTWebServiceUtils.class);
	
	private static final String ACCESS_AUTH_USER = "tiptop";
	private static final String ACCESS_AUTH_PASS = "tiptop";
	private static final String ACCESS_CONN_APP = "dataexws";
	
	private static String hostAddress = "";
	
	private static Map<String, String> srcDataMap;
	
	static {
		try {
			srcDataMap = LoadResources.objectMapperReadValue("tiptopwsaddr.json", LinkedHashMap.class, TTWebServiceUtils.class);
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
	
	public static TIPTOPServiceGateWayPortType getTIPTOPServiceGateWayPortTypeService() throws Exception {
		TIPTOPServiceGateWayLocator wsLocator = new TIPTOPServiceGateWayLocator();
		wsLocator.setTIPTOPServiceGateWayPortTypeEndpointAddress( srcDataMap.get("TIPTOPWsdlAddress") );
		TIPTOPServiceGateWayPortType ws = wsLocator.getTIPTOPServiceGateWayPortType();
		return ws;
	}
	
}
