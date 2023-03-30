package org.qifu.test;

import java.util.Map;

import com.github.underscore.U;

public class TestXml {
	
	public static void main(String args[]) throws Exception {
		String xml = "<com.dsc.nana.services.webservice.SimpleProcessInfo>\r\n"
				+ "  <processId>TIPTOPPROCESSPKG_apmt590</processId>\r\n"
				+ "  <processName>委外採購單維護作業</processName>\r\n"
				+ "  <createdTime>2021-04-22 07:47:58.0</createdTime>\r\n"
				+ "  <requesterId>T0529</requesterId>\r\n"
				+ "  <requesterName>Tester</requesterName>\r\n"
				+ "  <state>closed.completed</state>\r\n"
				+ "  <OID>0001ab55ea9710048b9316b69b3a8591</OID>\r\n"
				+ "  <serialNo>TIPTOPPROCESSPKG_apmt59000012525</serialNo>\r\n"
				+ "  <subject>TIPTOP Plant ID:GTAA 委外採購單維護作業 AV23-21040191</subject>\r\n"
				+ "  <abortComment></abortComment>\r\n"
				+ "</com.dsc.nana.services.webservice.SimpleProcessInfo>";
		Map<String, Object> xmlData = U.fromXmlMap(xml);
		System.out.println( xmlData.get("com.dsc.nana.services.webservice.SimpleProcessInfo") );
	}

}
