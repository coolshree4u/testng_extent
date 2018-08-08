package com.login.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class HomePageTest {

	
	ExtentReports extent;
	ExtentTest logger;
	
	
	public WebDriver driver;

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentRep.html",true);
		
		extent.addSystemInfo("Host Name","Testing").addSystemInfo("Environment","Automation Testing").addSystemInfo("Username","Shree");
		extent.loadConfig(new File(System.getProperty("user.dir")+"/extent-config.xml"));
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(dataProvider="Authentication")
	public void testUserLogin(String username,String password) throws IOException, InterruptedException {
		logger = extent.startTest("Test Started");
		logger.log(LogStatus.PASS, "Navigate to url");
		driver.navigate().to("http://0.0.0.0:4000/");
		
		logger.log(LogStatus.PASS, "Finding username");
		WebElement usernameInput= driver.findElement(By.cssSelector("input[name='username']"));
		usernameInput.sendKeys(username);
		
		WebElement passwordInput= driver.findElement(By.cssSelector("input[name='password']"));
		passwordInput.sendKeys(password);
		
		WebElement submit= driver.findElement(By.cssSelector("input[type='submit']"));
		submit.click();
		
		Thread.sleep(2000);
		WebElement logout= driver.findElement(By.cssSelector("a[href='/logout']"));
		logout.click();
		
		
		
		Thread.sleep(2000);
	}

	@AfterClass
	public void cleanUp() {
		driver.close();
		driver.quit();
		
		extent.flush();
		extent.close();
	}

	public List<HashMap<String, String>> readExcel(String filePath, String sheetName) throws IOException {
		File file = new File(filePath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbookExcel = new XSSFWorkbook(inputStream);
		Sheet sheet = workbookExcel.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		System.out.println("Printing rowcount" + rowCount);
		Row header = sheet.getRow(0);

		List<HashMap<String, String>> testData = new ArrayList<HashMap<String, String>>();

		for (int i = 1; i < rowCount; i++) {
			Row data = sheet.getRow(i);
			HashMap<String, String> mapTestData = new HashMap<String, String>();
			for (int j = 0; j < header.getLastCellNum(); j++) {

				mapTestData.put(header.getCell(j).getStringCellValue(), data.getCell(j).getStringCellValue());

			}
			testData.add(mapTestData);
		}

		System.out.println(testData);
		return testData;

	}

	@DataProvider
	    public Object[][] Authentication() throws Exception{
		 HomePageTest homePageTest=new HomePageTest();
	         Object[][] testObjArray = homePageTest.returnDataProvider();
	 
	         return (testObjArray);
	 
			}

	public static Object[][] returnDataProvider() throws IOException {
		HomePageTest homePageTest = new HomePageTest();
		List<HashMap<String, String>> testHashMapData = homePageTest
				.readExcel(System.getProperty("user.dir") + "/resources/test_data/UserList.xlsx", "UserList");

		Object[][] testdata = new Object[testHashMapData.size()][2];
		for (int i = 0; i < testHashMapData.size(); i++) {
			testdata[i][0] = testHashMapData.get(i).get("Username");
			testdata[i][1] = testHashMapData.get(i).get("Password");
		}
		return testdata;
	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception{
		if(result.getStatus()== ITestResult.FAILURE){
			logger.log(LogStatus.FAIL, "Test case Failed is"+ result.getName());
			logger.log(LogStatus.FAIL, "Test case Failed is"+ result.getThrowable());
			String screenshotPath = HomePageTest.getScreenshot(driver, result.getName());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotPath));
		}else if(result.getStatus()== ITestResult.SKIP){
			logger.log(LogStatus.SKIP, "Test case Skipped is"+ result.getName());
		}
		
		extent.endTest(logger);
		
	}
	
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		// Returns the captured file path
		return destination;
	}
}
