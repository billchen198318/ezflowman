package org.qifu.test;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.qifu.utils.EZFormSupportUtils;
import org.qifu.vo.EzForm;

public class TestXmlToJson2 {
	
	public static void main(String args[]) throws Exception {
		//String testXml = "aimi150.xml";
		String testXml = "apmt540.xml";
		//String testXml = "sign_form.xml";
		//String testXml = "Car_form.xml";
		//String testXml = "Car_form-TEST.xml";
		
		String formXmlContent = FileUtils.readFileToString(new File("/home/git5/ezflowman/doc/"+testXml), "utf-8");	
		
		EzForm formObj = EZFormSupportUtils.loadFromXml(formXmlContent);
		
		System.out.println( formObj.toString() );
		
		System.out.println( "-------------------------------------------------------------------------------------" );
		
		//Map<String, Object> formMap = EZFormSupportUtils.loadFromObject(formObj);
		//System.out.println( formMap.toString() );
		
		System.out.println( "re-convert to json>>" );
		System.out.println( EZFormSupportUtils.loadJsonFromObject(formObj) );
		
		System.out.println( "re-convert to xml>>" );
		System.out.println( EZFormSupportUtils.loadXmlFromObject(formObj) );
		
	}
	
}
