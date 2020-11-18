package com.shs.api.tests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.XML;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.shs.api.utlities.ExcelReader;
import com.shs.api.utlities.ParseXML;
import com.shs.api.utlities.ReportWriter;
import com.shs.api.utlities.TestRow;
import com.shs.api.utlities.XMLMapper;
import com.shs.api.utlities.tagValueFromResponse;
import com.shs.api.utlities.writeExcelTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIRequestTest extends BaseTest {
	private static Logger logger = LogManager.getLogger(APIRequestTest.class);
	Map<String, String> inputMap;
	XMLTestReader xmlReaderObj;
	RequestSpecification httpRequest;
	Response httpResponse;
	public static long startTime;
	public static long endTime;
	Map<String, String> headerMap;
	@SuppressWarnings("unused")
	private Document xmlDoc;
	private DocumentBuilderFactory docBuilderFactory;
	private DocumentBuilder docBuilder;
	private String response;
	private String responseBody;
	private Map<Integer, String> ExtractMap;
	List<String> extractorList;
	ValidateServiceAPITests validationObj;
	private Map<String, String> validationMap;
	public static int totalTestCase = 0;
	static int reportCallCount;
	int passedCount = 0;
	int testCaseCount = 0;
	public static int failedCount = 0;
	APIRequestTest APIRequestTestObj;
	public int countMethodInvocation = 0;
	static Map<String, String> SOMap;
	SoftAssert softAssertion;
	public int urlLength=0;
	Map<String, String> dataMap=new HashMap<String,String>();
	public void setTestConfig(String method, String requestType, String serviceName, String methodPropertyName)
			throws Exception {
		softAssertion = new SoftAssert();
		deleteInputFiles();
		deleteOutputFiles();
		System.out.println("Executing " + methodPropertyName + " method for " + serviceName);
		logger.info("Executing " + methodPropertyName + " method for " + serviceName);
		countMethodInvocation++;
		xmlReaderObj = new XMLTestReader();
		APIRequestTest obj = new APIRequestTest();
		if (reportCallCount == 0) {
			try {
				ReportWriter.createReportTemplate(serviceName,methodPropertyName);
				reportCallCount++;
				startTime = System.currentTimeMillis();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		inputMap = new HashMap<>();
		validationMap = new HashMap<String, String>();
		inputMap = xmlReaderObj.getRequestDetails(serviceName, methodPropertyName);
		String templateName = xmlReaderObj.getRequestTemplate(serviceName, methodPropertyName);
		String dataSheetName = xmlReaderObj.getRequestdata(serviceName, methodPropertyName);
		String requestUrl = xmlReaderObj.getRequestUrl(serviceName, methodPropertyName);
		logger.info("Configuring Setting and parameters for Test....");
		logger.info("Template Name : " + templateName);
		logger.info("DataSheet Name : " + dataSheetName);
		logger.info("Url : " + requestUrl);
		logger.info("Headers Used : " + inputMap);
		try {
			validationMap = xmlReaderObj.getValidationParams(serviceName);
		} catch (Exception E) {
			System.out.println("No validation provided by the user, continuing test cases....");
			logger.info("No validation provided by the user, continuing test cases....");
		}

		ExtractMap = xmlReaderObj.getextratValues(serviceName, templateName);
		if (method.equalsIgnoreCase("POST") && requestType.equalsIgnoreCase("SOAP")) {
			// obj.postSoapUserRequest(requestUrl,templateName,dataSheetName,serviceName,inputMap,ExtractMap,validationMap);
		} else if (method.equalsIgnoreCase("POST") && requestType.equalsIgnoreCase("REST")) {
			obj.postRestUserRequest(requestUrl, dataSheetName, serviceName, inputMap, ExtractMap, validationMap,
					templateName, methodPropertyName);
		} else if (method.equalsIgnoreCase("GET") && requestType.equalsIgnoreCase("REST")) {
			obj.getRestUserRequest(requestUrl, dataSheetName, serviceName, inputMap, ExtractMap, validationMap,
					templateName, methodPropertyName);
		} else if (method.equalsIgnoreCase("GET") && requestType.equalsIgnoreCase("SOAP")) {
			obj.getSoapUserRequest();
		} else {
			System.out.println("Invalid method or request type name");
		}
	}

	/*
	 * Method name = "postRestUserRequest" This method is used to create a json
	 * request and post the request params =
	 * requestUrl,dataSheetName,serviceName,inputMap,ExtractMap,validationMap
	 */
	public void postRestUserRequest(String url, String dataSheet, String serviceName, Map<String, String> reqMap,
			Map<Integer, String> extractMap, Map<String, String> validationMap, String template, String methodPropertyName) throws Exception {
		prepareJSONTestData(serviceName, dataSheet, template);
		urlLength=url.length();
		//dataMap = ExcelReader.readTestDataExtra(serviceName,dataSheet,1,"json");	
		startTime = System.currentTimeMillis();
		logger.info("\n******************************************************************************\n");
		logger.info("Test Started");
		logger.info("\n******************************************************************************\n");

		// String postUrl=reqMap.get("url");
		headerMap = new HashMap<String, String>();
		headerMap = new HashMap<String, String>();
		for (Entry<String, String> hMap : reqMap.entrySet()) {
			headerMap.put(hMap.getKey(), hMap.getValue());
		}
		xmlReaderObj = new XMLTestReader();
		try {
			File[] testFileList;
			testFileList = getFileDetails();

			// String dataSheetName =
			// System.getProperty("user.dir")+"/src/testdata/"+serviceName+"/testdataFiles/"+dataSheet+".xlsx";
			String dataSheetName = "testdata/" + serviceName + "/dataFiles/" + dataSheet + ".xlsx";
			totalTestCase = testFileList.length - 1;
			Map<String, String> testdataMap = new HashMap<String, String>();
			//url=url+dataMap.get("UnitNumber")+"-"+dataMap.get("OrderNumber");
			for (int i = 0; i < testFileList.length; i++) {
				//testdataMap = ExcelReader.readTestData(dataSheetName, i + 1);
				url=url.substring(0,urlLength);
				//String testCaseNum = testdataMap.get("TCNo");
				//System.out.println("Executing test Case Number  = " + testCaseNum);
				//String currentTestCase = testFileList[i].getName();
				//String fileNameWithOutExt = currentTestCase.replaceFirst("[.][^.]+$", "");
				// String requestBody =
				// ParseXML.getStringBody("src/com/shs/api/testInputFiles/" +
				// currentTestCase);				
				String currentTestCase = testFileList[i].getName();
				//System.out.println(currentTestCase);
				String fileNameWithOutExt = currentTestCase.replaceFirst("[.][^.]+$", "");
				int row = ExcelReader.getCurRow(dataSheetName, fileNameWithOutExt);
				if(row == -1) continue;
				testdataMap = ExcelReader.readTestData(dataSheetName, row);
				//url=url+testdataMap.get("UnitNumber")+"-"+testdataMap.get("OrderNumber");
				
				String testCaseNum = testdataMap.get("TCNo");
				System.out.println("Executing test Case Number  = " + testCaseNum);
				Object obj = new JSONParser()
						.parse(new FileReader("src/com/shs/api/testInputFiles/" + currentTestCase));
				// typecasting obj to JSONObject
				JSONObject jo = (JSONObject) obj;
				String requestBody = jo.toString();
				org.json.JSONObject jsonObject1 = new org.json.JSONObject(requestBody);
				requestBody = jsonObject1.toString(4);
				System.out.println("Request \n" + requestBody);

				
				httpRequest = RestAssured.given().relaxedHTTPSValidation();
				httpRequest.headers(headerMap);
				httpRequest.body(requestBody);
				logger.info("Http Request Payload - \n " + requestBody);
				logger.info("Current Execution Scenario  - " + fileNameWithOutExt);
				logger.info("Host Url - " + url);
				logger.info("Headers Used - " + headerMap);
				url = url + "s4eztlncdndj" +  "/draw/";
				httpResponse = httpRequest.post(url);
				docBuilderFactory = DocumentBuilderFactory.newInstance();
				docBuilder = docBuilderFactory.newDocumentBuilder();
				Map<String, String> rMap = new HashMap<String, String>();
				try {
					int statusCode = httpResponse.getStatusCode();
					if (statusCode == 200 || statusCode == 201 || statusCode == 400 || statusCode == 500) {
						String root = "root";
						response = httpResponse.getBody().asString();
						org.json.JSONObject jsonObject = new org.json.JSONObject(response);
						// esponseBody = ParseXML.formatString(response);
						response = jsonObject.toString(4);
						System.out.println("Response: ");
						System.out.println(response);
						String xmlresponse = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<" + root + ">"
								+ XML.toString(jsonObject) + "</" + root + ">";
						responseBody = ParseXML.formatString(xmlresponse);
						logger.info("Response from service : \n" + response);
						Document document = docBuilder.parse(new InputSource(new StringReader(responseBody)));
						for (int j = 0; j < extractMap.size(); j++) {
							String a = extractMap.get(j + 1);
							NodeList list = null;
							// System.out.println("Tage Name " +a);
							try {
								if (a.contains("ns2:")) {
									list = document.getElementsByTagName(a);
									// System.out.println("LIST1 :: "+list);
									if (list.item(0) != null) {
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name1 " +a);
									} else {
										// System.out.println("Tage Name2 "
										// +a.substring(4));
										list = document.getElementsByTagName(a.substring(4));
										// System.out.println("LIST3 :: "+list);
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name3 " +a);
									}
								} else {
									list = document.getElementsByTagName(a);
									// System.out.println("LIST4 :: "+list);
									if (list.item(0) != null) {
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name4 " +a);
									} else {
										list = document.getElementsByTagName("ns2:" + a);
										// System.out.println("LIST5 :: "+list);
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name5 " +a);
									}
								}
								// NodeList list=
								// document.getElementsByTagName(a);
								// a = list.item(0).getTextContent();
								rMap.put(a, list.item(0).getTextContent());
							} catch (NullPointerException nE) {
								System.out.println("Got null pointer in extracting values....");
							}

						}
						validationObj = new ValidateServiceAPITests();
						if (validationMap.isEmpty()) {
							validationObj.validateTestCaseResult(serviceName, rMap, headerMap, testCaseNum,
									currentTestCase.replace(".json", ".xml"), response, requestBody, fileNameWithOutExt,
									responseBody);
						} else {
							validationObj = new ValidateServiceAPITests();
							validationObj.validateTestCaseResult(validationMap, serviceName, rMap, headerMap,
									testCaseNum, currentTestCase.replace(".json", ".xml"), response, requestBody,
									fileNameWithOutExt);
						}
					} else if (statusCode > 200 && statusCode < 500) {
						response = httpResponse.getBody().asString();
						// responseBody = ParseXML.formatString(response);
						System.out.println(response);
						Document document = docBuilder.parse(new InputSource(new StringReader(responseBody)));
						NodeList validationMessage = document.getElementsByTagName("messages");

						logger.info("Service returned a validation message : " + validationMessage);
						logger.info("Response from service : \n" + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
					failedCount++;
					List<TestRow> testRowList = new ArrayList<>();
					String partNumber = "";
					String failError = e.getMessage();
					boolean result = false;
					testRowList.add(new TestRow(testCaseNum, partNumber, requestBody, responseBody, result,
							XMLMapper.dataMap, failError));
					// assertTrue(false);
					// ValidateServiceAPITests validateSerObj = new
					// ValidateServiceAPITests();
					// validateSerObj.WriteDataToReport(testCaseNum,partNumber,requestBody,responseBody,result,XMLMapper.dataMap,failError);

					logger.info("Exception Occcured \n" + e.getMessage());
					// continue;
				}
			}
		} catch (Exception E) {
			System.out.println(E);
			// softAssertion.assertTrue(false);
			logger.error("Exception Occured \n " + E);
		}

	}

	public void getSoapUserRequest() {
		System.out.println("getSoapUserRequest");
	}

	public void getRestUserRequest(String url, String dataSheet, String serviceName, Map<String, String> reqMap,
			Map<Integer, String> extractMap, Map<String, String> validationMap, String template, String methodPropertyName) throws Exception {
		prepareJSONTestData(serviceName, dataSheet, template);
		urlLength=url.length();
		//dataMap = ExcelReader.readTestDataExtra(serviceName,dataSheet,1,"json");	
		startTime = System.currentTimeMillis();
		logger.info("\n******************************************************************************\n");
		logger.info("Test Started");
		logger.info("\n******************************************************************************\n");

		// String postUrl=reqMap.get("url");
		headerMap = new HashMap<String, String>();
		headerMap = new HashMap<String, String>();
		for (Entry<String, String> hMap : reqMap.entrySet()) {
			headerMap.put(hMap.getKey(), hMap.getValue());
		}
		xmlReaderObj = new XMLTestReader();
		try {
			File[] testFileList;
			testFileList = getFileDetails();

			// String dataSheetName =
			// System.getProperty("user.dir")+"/src/testdata/"+serviceName+"/testdataFiles/"+dataSheet+".xlsx";
			String dataSheetName = "testdata/" + serviceName + "/dataFiles/" + dataSheet + ".xlsx";
			totalTestCase = testFileList.length - 1;
			Map<String, String> testdataMap = new HashMap<String, String>();
			//url=url+dataMap.get("UnitNumber")+"-"+dataMap.get("OrderNumber");
			for (int i = 0; i < testFileList.length; i++) {
				//testdataMap = ExcelReader.readTestData(dataSheetName, i + 1);
				url=url.substring(0,urlLength);
				//String testCaseNum = testdataMap.get("TCNo");
				//System.out.println("Executing test Case Number  = " + testCaseNum);
				//String currentTestCase = testFileList[i].getName();
				//String fileNameWithOutExt = currentTestCase.replaceFirst("[.][^.]+$", "");
				// String requestBody =
				// ParseXML.getStringBody("src/com/shs/api/testInputFiles/" +
				// currentTestCase);				
				String currentTestCase = testFileList[i].getName();
				//System.out.println(currentTestCase);
				String fileNameWithOutExt = currentTestCase.replaceFirst("[.][^.]+$", "");
				int row = ExcelReader.getCurRow(dataSheetName, fileNameWithOutExt);
				if(row == -1) continue;
				int row1 = ((row - 1) * 2) + 1;
				testdataMap = ExcelReader.readTestData(dataSheetName, row);
				//url=url+testdataMap.get("UnitNumber")+"-"+testdataMap.get("OrderNumber");
				//String tcName = testdataMap.get("TCName");
				String tcDescription = testdataMap.get("TC_Description");
				
				String testCaseNum = testdataMap.get("TCNo");
				System.out.println("Executing test Case Number  = " + testCaseNum);
				Object obj = new JSONParser()
						.parse(new FileReader("src/com/shs/api/testInputFiles/" + currentTestCase));
				// typecasting obj to JSONObject
				JSONObject jo = (JSONObject) obj;
				String requestBody = jo.toString();
				org.json.JSONObject jsonObject1 = new org.json.JSONObject(requestBody);
				requestBody = jsonObject1.toString(4);
				System.out.println("Request \n" + requestBody);

				
				httpRequest = RestAssured.given().relaxedHTTPSValidation();
				httpRequest.headers(headerMap);
				httpRequest.body(requestBody);
				logger.info("Http Request Payload - \n " + requestBody);
				logger.info("Current Execution Scenario  - " + fileNameWithOutExt);
				logger.info("Host Url - " + url);
				logger.info("Headers Used - " + headerMap);
				
				if (methodPropertyName == "drawDeck"){
					String deck_id = testdataMap.get("deck_id");
					
					url = url + deck_id +  "/draw/";
				}else
					url = url ;
				
				httpResponse = httpRequest.get(url);
				docBuilderFactory = DocumentBuilderFactory.newInstance();
				docBuilder = docBuilderFactory.newDocumentBuilder();
				Map<String, String> rMap = new HashMap<String, String>();
				try {
					int statusCode = httpResponse.getStatusCode();
					if (statusCode == 200 || statusCode == 201 || statusCode == 400 || statusCode == 500) {
						String root = "root";
						response = httpResponse.getBody().asString();
						org.json.JSONObject jsonObject = new org.json.JSONObject(response);
						// esponseBody = ParseXML.formatString(response);
						response = jsonObject.toString(4);
						System.out.println("Response: ");
						System.out.println(response);
						String xmlresponse = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<" + root + ">"
								+ XML.toString(jsonObject) + "</" + root + ">";
						responseBody = ParseXML.formatString(xmlresponse);
						logger.info("Response from service : \n" + response);
						Document document = docBuilder.parse(new InputSource(new StringReader(responseBody)));
						for (int j = 0; j < extractMap.size(); j++) {
							String a = extractMap.get(j + 1);
							NodeList list = null;
							// System.out.println("Tage Name " +a);
							try {
								if (a.contains("ns2:")) {
									list = document.getElementsByTagName(a);
									// System.out.println("LIST1 :: "+list);
									if (list.item(0) != null) {
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name1 " +a);
									} else {
										// System.out.println("Tage Name2 "
										// +a.substring(4));
										list = document.getElementsByTagName(a.substring(4));
										// System.out.println("LIST3 :: "+list);
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name3 " +a);
									}
								} else {
									list = document.getElementsByTagName(a);
									// System.out.println("LIST4 :: "+list);
									if (list.item(0) != null) {
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name4 " +a);
									} else {
										list = document.getElementsByTagName("ns2:" + a);
										// System.out.println("LIST5 :: "+list);
										a = list.item(0).getTextContent();
										// System.out.println("Tage Name5 " +a);
									}
								}
								// NodeList list=
								// document.getElementsByTagName(a);
								// a = list.item(0).getTextContent();
								rMap.put(a, list.item(0).getTextContent());
							} catch (NullPointerException nE) {
								System.out.println("Got null pointer in extracting values....");
							}

						}
						validationObj = new ValidateServiceAPITests();
						if (validationMap.isEmpty()) {
							validationObj.validateTestCaseResult(serviceName, rMap, headerMap, testCaseNum,
									currentTestCase.replace(".json", ".xml"), response, url, fileNameWithOutExt,
									responseBody);
						} else {
							validationObj = new ValidateServiceAPITests();
							validationObj.validateTestCaseResult(validationMap, serviceName, rMap, headerMap,
									testCaseNum, currentTestCase.replace(".json", ".xml"), response, requestBody,
									fileNameWithOutExt);
						}
						
						if (methodPropertyName.equals("newDeck") && responseBody.contains("<deck_id>")) {
							// String dataSheetName =
							// System.getProperty("user.dir") +
							// "/testdata/PTLService/dataFiles/CAMSCreateSOTestData.xlsx";
							String dataSheetName1 = System.getProperty("user.dir")
									+ "/testdata/DeckofCardsAPI/dataFiles/drawDeckTestdata.xlsx";
							tagValueFromResponse tfr = new tagValueFromResponse();
							// System.out.println(tfr.tagValueString(responseBody,
							// "<serviceordernum>"));
							// System.out.println(tfr.tagValueString(responseBody,
							// "<serviceunitnum>"));
							String deck_id = tfr.tagValueString(responseBody, "<deck_id>");
							//String serviceunitnum = tfr.tagValueString(responseBody, "<serviceunitnum>");
							int rowinNewDeckSheet = ExcelReader.getRowCount(dataSheetName);
							int rowinDrawDeckSheet = ExcelReader.getRowCount(dataSheetName1);
							// int rowinReschdule = ((rowInCreateSheet - 1) * 2)
							// + 1;
							String testCase = "TC" + Integer.toString(rowinNewDeckSheet + rowinDrawDeckSheet - 1);
							// String testCase1 = "TC_" +
							// Integer.toString(rowinReschdule);
							//SOMap.put(deck_id,deck_id);
							writeExcelTest.writeResponseToRescheduleExcel(testCase,  tcDescription,  deck_id);
							/*
							 * writeExcelTest.writeResponseToExcel(testCase1,
							 * tcName, tcDescription, partnerId,
							 * partnerPwd,serviceunitnum, serviceordernum);
							 */
							++row;
							++row1;

						} else if (methodPropertyName.equals("ptlRescheduleSO")
								&& responseBody.contains("<serviceordernum>")) {
							System.out.println("not writing  to any Excel");
						}
					} else if (statusCode > 200 && statusCode < 500) {
						response = httpResponse.getBody().asString();
						// responseBody = ParseXML.formatString(response);
						System.out.println(response);
						Document document = docBuilder.parse(new InputSource(new StringReader(responseBody)));
						NodeList validationMessage = document.getElementsByTagName("messages");

						logger.info("Service returned a validation message : " + validationMessage);
						logger.info("Response from service : \n" + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
					failedCount++;
					List<TestRow> testRowList = new ArrayList<>();
					String partNumber = "";
					String failError = e.getMessage();
					boolean result = false;
					testRowList.add(new TestRow(testCaseNum, partNumber, requestBody, responseBody, result,
							XMLMapper.dataMap, failError));
					// assertTrue(false);
					// ValidateServiceAPITests validateSerObj = new
					// ValidateServiceAPITests();
					// validateSerObj.WriteDataToReport(testCaseNum,partNumber,requestBody,responseBody,result,XMLMapper.dataMap,failError);

					logger.info("Exception Occcured \n" + e.getMessage());
					// continue;
				}
			}
		} catch (Exception E) {
			System.out.println(E);
			// softAssertion.assertTrue(false);
			logger.error("Exception Occured \n " + E);
		}

	}

}
