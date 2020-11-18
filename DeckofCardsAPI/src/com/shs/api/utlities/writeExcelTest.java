package com.shs.api.utlities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import com.shs.api.dbUtilities.DBConnection;

public class writeExcelTest {

	public void writeExcel(String filePath, String fileName, String sheetName, String[] dataToWrite)
			throws IOException {

		// Create an object of File class to open xlsx file

		File file = new File(filePath + "\\" + fileName);

		// Create an object of FileInputStream class to read excel file

		FileInputStream inputStream = new FileInputStream(file);

		Workbook WorkBook = null;

		// Find the file extension by splitting file name in substring and
		// getting only extension name

		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		// Check condition if the file is xlsx file

		if (fileExtensionName.equals(".xlsx")) {

			// If it is xlsx file then create object of XSSFWorkbook class
			// XSSFWorkbook wb = new XSSFWorkbook(new File("file.xlsx"));

			WorkBook = new XSSFWorkbook(inputStream);

		}

		// Check condition if the file is xls file

		else if (fileExtensionName.equals(".xls")) {

			// If it is xls file then create object of XSSFWorkbook class

			WorkBook = new XSSFWorkbook(inputStream);

		}

		// Read excel sheet by sheet name

		Sheet sheet = WorkBook.getSheet(sheetName);

		// Get the current count of rows in excel file
		

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		

		// Get the first row from the sheet

		Row row = sheet.getRow(0);

		// Create a new row and append it at last of sheet

		Row newRow = sheet.createRow(rowCount + 1);

		// Create a loop over the cell of newly created Row
		System.out.println(">>>>" + row.getLastCellNum());

		for (int j = 0; j < row.getLastCellNum(); j++) {

			// Fill data in row

			Cell cell = newRow.createCell(j);

			cell.setCellValue(dataToWrite[j]);

		}

		// Close input stream

		inputStream.close();

		// Create an object of FileOutputStream class to create write data in
		// excel file

		FileOutputStream outputStream = new FileOutputStream(file);

		// write data in the excel file

		WorkBook.write(outputStream);

		// close output stream

		outputStream.close();

	}
	
	public static void writeResponseToExcel(String testCaseNum,  String TC_Description,  String unitNum,String serviceOrderNum) throws Exception {

		// Create an array with the data in the same order in which you expect
		// to be filled in excel file
		String NewTc_Dec=TC_Description.replace("Rescheduled","Canceled");
		
		
		String[] valueToWrite = { testCaseNum,  TC_Description.replace("ptlRescheduleSO","ptlCancelSO") };

		// Create an object of current class

		writeExcelTest objExcelFile = new writeExcelTest();

		// Write the file using file name, sheet name and the data to be filled

		objExcelFile.writeExcel(System.getProperty("user.dir") + "/testdata/PTLService/dataFiles",
				"PTLCancelSOTestdata.xlsx", "Sheet1", valueToWrite);
		System.out.println("Written in Cancel Excel File!!");

	}
	
	public static void writeResponseToRescheduleExcel(String testCaseNum,  String TC_Description,String deck_id)  {
		try
		{
		/*Timestamp tm = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date()); // Now use today date.
		cal.add(Calendar.DATE, 5); // Adding 5 days
		String reqUestDate = sdf.format(cal.getTime());
		System.out.println("Date Set for Reschedule:" + reqUestDate);*/
		//tm = DBConnection.connectNPS(serviceOrderNum,unitNum);
		//String tp = tm.toString();
		//System.out.println(tp.replace(" ", "T"));
		//TC_Description.replace("created","Rescheduled");
		String NewTc_Dec=TC_Description.replace("new deck","draw deck");
		
		String[] valueToWrite = { testCaseNum, NewTc_Dec.replace("new deck","draw deck"),deck_id};
		
		// Create an object of current class

		writeExcelTest objExcelFile = new writeExcelTest();

		// Write the file using file name, sheet name and the data to be filled
		//DOMICreate_API_Automation/src/com/shs/api/DOMI/testdata/HSSOMCreateOrderService/dataFiles
		objExcelFile.writeExcel(System.getProperty("user.dir") + "/testdata/DeckofCardsAPI/dataFiles",
			"drawDeckTestdata.xlsx", "Sheet1", valueToWrite);
          System.out.println("Written in drawDeck Excel File!!");
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}

}