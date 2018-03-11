package tool.auto.horustoken;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;

import tool.auto.common.CommonUtils;


public class horustoken {
	
	private static final String EMAIL = "votoan8393xahahababylove124445@gmail.com";
	private static final String EMAIL_PASS = "callofduty4";
	private static final String REF_LINK = "http://horustoken.com/account/register.php?ref=471ad814";
	private static final String MAILCONTINUE = "vot.o.a.n.8.3.9.3.xahahaba.bylove124445@gmail.com";
	private static final String GECKO_DRIVER = "C:/geckodriver-v0.19.1-win64/geckodriver.exe";

	private static WebDriver driver = null;
	static ClassLoader classLoader = horustoken.class.getClassLoader();
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{
		
		int count = 0;
		
		String email;
		List<String> emailsList = CommonUtils.getEmailsList("email_list.txt");
		inputNamesList = CommonUtils.getInputNames();
		
		Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", filePath.toString());
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		driver = new FirefoxDriver(firefoxOptions);
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		int indexBegin = emailsList.indexOf(MAILCONTINUE) +1 ;
		
		// Go to web		
		for (int i = 2; i < 2000 ; i++) {
			try {
				count = i;
				email = emailsList.get(i);
				fillRegistForm(email);
				System.out.println(i);
			} catch (UnhandledAlertException f) {
				try {
					Alert alert = driver.switchTo().alert();
					alert.accept();
				} catch (NoAlertPresentException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.out.println("============= EROR ==============" + e);
				System.out.println(count);
			}
		}
		System.out.println("============= DONE ==============");
		System.out.println(count);
	}
	
	private static void fillRegistForm(String email) throws InterruptedException {	

		WebElement element;
		driver.get(REF_LINK);
		waitForLoad(driver);

		element = driver.findElement(By.name("email"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("password"));
		element.clear();
		element.sendKeys(email);
		TimeUnit.SECONDS.sleep(1);
		element.submit();
		TimeUnit.SECONDS.sleep(5);
	}
	
	private static List<String> getInputNames() {

		List<String> inputNamesList = new ArrayList<String>();
		Reader reader = null;
		CSVReader csvReader = null;
		try {
			Path inputNamesPath = Paths.get("src", "main", "resources", "input_name.csv");
			reader = Files.newBufferedReader(inputNamesPath);
			csvReader = new CSVReader(reader);

			// Reading Records One by One in a String array
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				inputNamesList.add(nextRecord[0].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}

				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return inputNamesList;
	}
	
	private static String getRandomName() {
		Random rand = new Random();
		int nameIndex = rand.nextInt(inputNamesList.size());
		return inputNamesList.get(nameIndex);
	}
	
	static void waitForLoad(WebDriver driver) {
		new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
	}
	
	private static List<String> listMail(String gmailInput) {
		List<String> listMail = new ArrayList<String>();
		int length = gmailInput.length();
		String gmail = gmailInput;
		int count = 0;
		String s = ".";
		String print = "";
		for (int k = 0; k <= length; k++) {
			for (int j = k; j <= length - 12; j++) {
				for (int i = k + 1; i <= gmail.length() - 11; i++) {
					print = gmail.substring(0, i) + s + gmail.substring(i);
					if (j > k && print.indexOf("..") == -1) {
						listMail.add(print);
					}
				}
				count++;
				gmail = gmail.substring(0, j + count) + s + gmail.substring(j + count);
			}
			count = 0;
			gmail = gmailInput;
		}
		return listMail;
	}
}