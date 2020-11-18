package com.shs.api.testCases;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.shs.api.tests.APIRequestTest;
import com.shs.api.tests.BaseTest;
import com.shs.api.tests.ValidateServiceAPITests;
import com.shs.api.utlities.DeleteSheetContents;
import com.shs.api.utlities.ReportWriter;

@Listeners(com.shs.api.utlities.Listeners.class)
public class DeckofCardsAPI {
	public static long endTime;
	static {
		PropertyConfigurator
				.configure(System.getProperty("user.dir") + "/src/com/shs/api/resources/log4j.properties");
	}
	//deckofcardsapi.com/
	@BeforeSuite
	public void startTest() {
		System.out.println("Started execution of DeckofCardsAPI Service ......");
	}

	// @Ignore
	@Test(priority = 0)
	public void newDeck() throws Exception {
		DeleteSheetContents.deleteSheet("/testdata/DeckofCardsAPI/dataFiles/drawDeckTestdata.xlsx", "newDeck");
		APIRequestTest apiCreateTest = new APIRequestTest();
		apiCreateTest.setTestConfig("GET", "REST", "DeckofCardsAPI", "newDeck");
	}

	@Test(priority = 1)
	public void drawDeck() throws Exception {
		APIRequestTest apiCreateTest = new APIRequestTest();
		apiCreateTest.setTestConfig("GET", "REST", "DeckofCardsAPI", "drawDeck");
	}

	@AfterSuite
	public void setConfigs() throws IOException {
		endTime = System.currentTimeMillis();
		String time = endTime - APIRequestTest.startTime + "ms";
		int passedTestCaseCount = ValidateServiceAPITests.passTestCount;
		int failedTestCaseCount = ValidateServiceAPITests.failedTestCount + APIRequestTest.failedCount;
		int totalTestCaseCount = passedTestCaseCount + failedTestCaseCount;
		ReportWriter.writeReportSummary(time, passedTestCaseCount, totalTestCaseCount);
		BaseTest.deleteInputFiles();
		BaseTest.deleteOutputFiles();
	}
}
