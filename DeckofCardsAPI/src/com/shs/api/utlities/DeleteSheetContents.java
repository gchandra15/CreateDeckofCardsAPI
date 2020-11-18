package com.shs.api.utlities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DeleteSheetContents {

	public static void deleteSheet(String path,String method) throws IOException {
		FileInputStream file = null;
		Workbook WorkBook = null;
		FileOutputStream out = null;
		try {
			file = new FileInputStream(new File(System.getProperty("user.dir") + path));
			String fileExtensionName = path.substring(path.indexOf("."));
			if (fileExtensionName.equals(".xlsx")) {
				// If it is xlsx file then create object of XSSFWorkbook class
				// XSSFWorkbook wb = new XSSFWorkbook(new File("file.xlsx"));
				WorkBook = new XSSFWorkbook(file);
			}
			// Check condition if the file is xls file
			else if (fileExtensionName.equals(".xls")) {
				// If it is xls file then create object of XSSFWorkbook class
				WorkBook = new XSSFWorkbook(file);
			}
			// wb = new HSSFWorkbook(file);
			XSSFSheet sheet = (XSSFSheet) WorkBook.getSheetAt(0);
			if(method=="newDeck"){
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					Row row = sheet.getRow(i);
					deleteRow(sheet, row);
				}				
			}else if(method == "ptlRescheduleSO"){
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					Row row = sheet.getRow(i);
					deleteRow(sheet, row);
				}
			}
			
			/*for (int i = 7; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				deleteRow(sheet, row);
			}*/
			out = new FileOutputStream(new File(System.getProperty("user.dir") + path));
			WorkBook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Deleting file : " + path);
		file.close();
		out.close();
		WorkBook.close();
	}

	public static void deleteRow(XSSFSheet sheet, Row row) {
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum != 0 && lastRowNum > 0) {
			int rowIndex = row.getRowNum();
			Row removingRow = sheet.getRow(rowIndex);
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}

		}
	}
}
