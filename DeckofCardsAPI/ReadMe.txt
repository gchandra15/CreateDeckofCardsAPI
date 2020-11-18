*************************************************************************

Deck of Cards API Automation: Rest Assured framework
Note:
I have completed 2 test scenario's execute the test automation suite "SmokeTest.xml" file
	1) create new deck
	2) one to draw a card

*************************************************************************

Steps to import the project :

If the project is shared as Zip ---

1. Unzip the project (or) Clone Project from Github
2. Open eclipse
3. Got to 'File' and click on import
4. In the new window, click on maven -> existing maven project
5. Click on Browse and specify the project folder (unzipped above). You should see, it detects the pom.xml
6. Click on finish. It takes some time to import the project.
7. After import, right click on Project and select Run As --> Run Configurations
8. In the new window, select the classpath tab ->User Entries ->Advanced -> Add Folders -> ok
9. In the popup, select the DeckofCardsAPI -> src -> resources -> Ok. Then click on Apply.
10. Right click on the project -> RunAs-> Maven clean 
11. Right click on the project -> RunAs-> Maven install
12. After the build is success, right click on your "SmokeTest.xml" build file and run as it TESTNG Suite.
13. Test Results will generate today date and time stamp under "html_reports" folder.

***********************************************************************************************************