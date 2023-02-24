package org.qifu.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;

import com.github.underscore.U;

public class TestXmlToJson {
	
	public static void main(String args[]) throws Exception {
		
		//String testXml = "aimi150.xml";
		//String testXml = "apmt540.xml";
		//String testXml = "sign_form.xml";
		//String testXml = "Car_form.xml";
		String testXml = "Car_form-TEST.xml";
		
		String formXmlContent = FileUtils.readFileToString(new File("/home/git5/ezflowman/doc/"+testXml), "utf-8");		
        
		/*
		JSONObject xmlJSONObj = XML.toJSONObject(formXmlContent);                
        System.out.println("--------------------------------------------------------------------");
        System.out.println( xmlJSONObj );		
        System.out.println("--------------------------------------------------------------------");
		
        String json2xml = XML.toString( xmlJSONObj );
        System.out.println( json2xml );
        */
		
		String jsonStr = U.xmlToJson( formXmlContent );
        System.out.println("--------------------------------------------------------------------");
        System.out.println( jsonStr );		
        System.out.println("--------------------------------------------------------------------");        
        
        String xmlStr = U.jsonToXml( jsonStr );
        System.out.println( xmlStr );		
        
	}
	
}
