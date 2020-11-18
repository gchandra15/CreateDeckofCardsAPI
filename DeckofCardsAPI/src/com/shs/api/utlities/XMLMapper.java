package com.shs.api.utlities;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.xml.sax.SAXException;

import com.shs.api.utlities.CreateRequestBody;
import com.shs.api.utlities.ExcelReader;

public class XMLMapper {
	public static Map<String,String> dataMap= new LinkedHashMap<>();

	public static String makeXMLRequest(String templatePath, String datafile){
		String reqBody="";
		//String templatePath = System.getProperty("user.dir")+"\\src\\com.shs.api.testdata\\Settlement\\requestTemplate\\settlementCreateSinglePartTemplate.xml";
		//String datafile = System.getProperty("user.dir")+"\\src\\com.shs.api.testdata\\Settlement\\testdataFiles\\SettlementCreateSinglePartTestData.xlsx";		
		//String xmlReq = ParseXML.getStringBody(templatePath);				
		try {
			int rowCount = ExcelReader.getRowCount(datafile);
			for(int i=0;i<rowCount-1;i++){
				dataMap=ExcelReader.readTestData(datafile, i+1);				
				CreateRequestBody.createReqBody(templatePath,dataMap);
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return reqBody;	
	}	
	
	public static String makeJSONRequest(String datafile,String template) throws Exception{
		String reqBody="";				
		try {
			int rowCount = ExcelReader.getRowCount(datafile);
			for(int i=0;i<rowCount-1;i++){
				dataMap=ExcelReader.readTestData(datafile, i+1,"json");				
				reqBody=CreateRequestBody.createJsonReqBody(dataMap,template);
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return reqBody;	
	}	
	
	public static void main(String[] args) {
		//makeJSONRequest("C:\\Home Services\\Automation_Scripts\\API_Automation\\EventBroker_API_Automation\\EventBroker_API_Automation\\testdata\\EventBroker\\dataFiles\\EB_BookJobTestData.xlsx");
	}
}
