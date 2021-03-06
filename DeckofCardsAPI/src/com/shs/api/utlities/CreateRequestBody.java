package com.shs.api.utlities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CreateRequestBody {
	static int index = 1;

	static public String createJsonReqBody(Map<String, String> inputMap, String template) throws Exception {
		String jsonReq = "";
		File initialFile = new File(System.getProperty("user.dir") + template);
		InputStream targetStream = new FileInputStream(initialFile);
		JSONTokener tokener = new JSONTokener(targetStream);
		JSONObject object = new JSONObject(tokener);
		for (Map.Entry<String, String> entry : inputMap.entrySet()) {
			jsonReq = CreateJsonRequestBody.createJsonPayload(object, entry.getKey().toString(),
					entry.getValue().toString(), inputMap);
		}
		File newTextFile = new File("src/com/shs/api/testInputFiles/" + inputMap.get("TCNo") + ".json");
		FileWriter fw = new FileWriter(newTextFile);
		fw.write(jsonReq);
		fw.close();
		return jsonReq;
	}

	static public String createReqBody(String templatePath, Map<String, String> inputMap) {
		String requestBody = "";
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		DocumentBuilder docBuilder = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		Document document;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(new File(templatePath));
			NodeList nodeList = document.getElementsByTagName("*"); // parent
																	// element
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					// eElement.setTextContent(inputMap.get(eElement.getTextContent()));
					boolean hasChilds = hasChildElements(eElement);
					if (hasChilds) {
						NodeList childList = eElement.getElementsByTagName(node.getNodeName()); // immediate
																								// child
																								// of
																								// parent
																								// element
						for (int j = 0; j < childList.getLength(); j++) {
							Node node1 = childList.item(j);
							if (node1.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement1 = (Element) node1;
								eElement1.setTextContent(inputMap.get(eElement1.getTextContent()));
								// System.out.println(eElement1.getNodeName()+
								// "--> "+eElement1.getTextContent());
							}
						}
					} else {
						eElement.setTextContent(inputMap.get(eElement.getTextContent()));
						// System.out.println(eElement.getNodeName()+ "-->
						// "+eElement.getTextContent());
					}
				}
			}

			// write the content into xml file
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(
					new File("src/com/shs/api/testInputFiles/" + inputMap.get("TCNo") + ".xml"));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestBody;
	}

	public static boolean hasChildElements(Element el) {
		NodeList children = el.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
				return true;
		}
		return false;
	}

	/*
	 * public static void main(String[] args) { Map<String,String> hmsp=new
	 * LinkedHashMap(); hmsp.put("1","manas"); hmsp.put("2","sarma");
	 * createReqBody(System.getProperty("user.dir")+
	 * "/src/resources/settlementTemplate.xml",hmsp); }
	 */
}
