package org.qifu.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;

public class TestXmlToJson {
	
	public static void main(String args[]) throws Exception {
		
		//String testXml = "aimi150.xml";
		//String testXml = "apmt540.xml";
		String testXml = "sign_form.xml";
		
		String formXmlContent = FileUtils.readFileToString(new File("/home/git5/ezflowman/doc/"+testXml), "utf-8");
		
        JSONObject xmlJSONObj = XML.toJSONObject(formXmlContent);
        System.out.println( xmlJSONObj );		
		
		
	}
	
}
