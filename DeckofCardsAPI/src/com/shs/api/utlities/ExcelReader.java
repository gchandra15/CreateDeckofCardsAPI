package com.shs.api.utlities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
	static Map<String, String> inputMap = new LinkedHashMap<String, String>();
	static FileOutputStream fos;
	static PrintWriter writer;
	static Workbook workbook;
	static Sheet sheet;

	public static Map<String, String> readTestData(String datafile, int rowNum)
			throws IOException, InvalidFormatException {

		workbook = WorkbookFactory.create(new File(datafile));
		// System.out.println("Workbook has " + workbook.getNumberOfSheets() + "
		// Sheets : ");
		sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		sheet.getPhysicalNumberOfRows();
		// int num=2;
		Row headerRow = sheet.getRow(0);
		Row dataRow = sheet.getRow(rowNum);
		// for(Cell headerCell:headerRow){
		int cellCount = sheet.getRow(0).getPhysicalNumberOfCells();
		//System.out.println("Cell count = " + cellCount);

		for (int i = 0; i < cellCount; i++) {
			String cellValue = dataFormatter.formatCellValue(headerRow.getCell(i));
			String cellValue1 = dataFormatter.formatCellValue(dataRow.getCell(i));
			// System.out.println(cellValue + " === " + cellValue1);
			inputMap.put(cellValue, cellValue1);
		}

		/*
		 * for(Entry<String, String> emap:inputMap.entrySet()){
		 * System.out.println(emap.getKey() + "---> " + emap.getValue()); }
		 */

		workbook.close();
		return inputMap;
	}

	public static Map<String, String> readTestData(String datafile, int rowNum, String reqType)
			throws IOException, InvalidFormatException {

		workbook = WorkbookFactory.create(new File(datafile));
		// System.out.println("Workbook has " + workbook.getNumberOfSheets() + "
		// Sheets : ");
		sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		sheet.getPhysicalNumberOfRows();
		// int num=2;
		Row headerRow = sheet.getRow(0);
		Row dataRow = sheet.getRow(rowNum);
		// for(Cell headerCell:headerRow){
		int cellCount = sheet.getRow(0).getPhysicalNumberOfCells();
		// System.out.println("Cell count = " + cellCount);
		for (int i = 0; i < cellCount; i++) {
			String cellValue = dataFormatter.formatCellValue(headerRow.getCell(i));
			String cellValue1 = dataFormatter.formatCellValue(dataRow.getCell(i));
			// System.out.println(cellValue + " === " + cellValue1);
			inputMap.put(cellValue, cellValue1);
		}

		workbook.close();
		return inputMap;
	}

	public static int getRowCount(String dataFilePath)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		File myFile = new File(dataFilePath);
		System.out.println("Inside file method");
		workbook = WorkbookFactory.create(myFile);
		Sheet sheet = workbook.getSheetAt(0);
		new DataFormatter();
		int rowCount = sheet.getPhysicalNumberOfRows();
		// System.out.println("Row count - " + rowCount);
		return rowCount;
	}

	public static Map<String, String> readTestDataExtra(String serviceName, String datafile, int rowNum, String reqType)
			throws IOException, InvalidFormatException {

		Map<String, String> inp = new HashMap<String, String>();
		String dataFilePath = "testdata/" + serviceName + "/dataFiles/" + datafile + ".xlsx";
		workbook = WorkbookFactory.create(new File(dataFilePath));
		// System.out.println("Workbook has " + workbook.getNumberOfSheets() + "
		// Sheets : ");
		sheet = workbook.getSheetAt(1);
		DataFormatter dataFormatter = new DataFormatter();
		sheet.getPhysicalNumberOfRows();
		// int num=2;
		Row headerRow = sheet.getRow(0);
		Row dataRow = sheet.getRow(rowNum);
		// for(Cell headerCell:headerRow){
		int cellCount = sheet.getRow(0).getPhysicalNumberOfCells();
		// System.out.println("Cell count = " + cellCount);
		for (int i = 0; i < cellCount; i++) {
			String cellValue = dataFormatter.formatCellValue(headerRow.getCell(i));
			String cellValue1 = dataFormatter.formatCellValue(dataRow.getCell(i));
			// System.out.println(cellValue + " === " + cellValue1);
			inp.put(cellValue, cellValue1);
		}

		workbook.close();
		return inp;
	}

	public static int getCurRow(String dataSheetName, String name) throws InvalidFormatException, IOException {
		//System.out.println("Testing");
		workbook = WorkbookFactory.create(new File(dataSheetName));
		//System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
		sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			Row dataRow = sheet.getRow(i);
			if (dataFormatter.formatCellValue(dataRow.getCell(0)).toString().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * public static String getTestCaseNumber(){ String testCaseNum=""; workbook
	 * = WorkbookFactory.create(new File(datafile)); sheet =
	 * workbook.getSheetAt(0);
	 * 
	 * return testCaseNum;
	 * 
	 * }
	 */
	public static void main(String[] args) throws InvalidFormatException, IOException {

		getRowCount("testdata/EventBroker/testdataFiles/EB_GetAvailabilityTestData.xlsx");
	}

}