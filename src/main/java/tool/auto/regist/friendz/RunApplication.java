package tool.auto.regist.friendz;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.opencsv.CSVReader;

public class RunApplication {

	private static final int MAX_NUM_MAIL = 120;

	public static void main(String[] arg) throws InterruptedException {

		List<String> emailsList = getEmailsList();
		List<String> inputNamesList = getInputNames();

		Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", filePath.toString());
		List<String> refUrlsList = new ArrayList<String>();
		refUrlsList.add("https://steward.friendz.io/go?r=NzExMTU5");

		int fromIndex;
		int toIndex;
		int numOfEmail = emailsList.size();
		AutoRegister autoRegister;
		
//		FirefoxOptions firefoxOptions = new FirefoxOptions();
//		firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
//		WebDriver driver = new FirefoxDriver(firefoxOptions);
		
		Path filePath1 = Paths.get("src", "main", "resources","service-tool", "chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", filePath1.toString());
		ChromeOptions options = new ChromeOptions();
		options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		WebDriver driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		for (int i = 0; i < refUrlsList.size(); i++) {
			fromIndex = i * MAX_NUM_MAIL;
			toIndex = i * MAX_NUM_MAIL + (MAX_NUM_MAIL - 1);

			if (fromIndex >= numOfEmail) {
				break;
			}
			if (toIndex >= numOfEmail) {
				toIndex = numOfEmail;
			}

			//autoRegister = new AutoRegisterRunnable(refUrlsList.get(i), fromIndex, toIndex, emailsList, inputNamesList);
			autoRegister = new AutoRegister(refUrlsList.get(i), fromIndex, toIndex, emailsList, inputNamesList, driver);
			autoRegister.run();
		}
	}

	private static List<String> getEmailsList() {

		List<String> emailsList = new ArrayList<String>();
		Reader reader = null;
		CSVReader csvReader = null;
		try {

			// Get file from resources folder
			Path inputEmailsPath = Paths.get("src", "main", "resources", "email_list.csv");
			reader = Files.newBufferedReader(inputEmailsPath);
			csvReader = new CSVReader(reader);

			// Reading Records One by One in a String array
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				emailsList.add(nextRecord[0]);
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

		return emailsList;
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
}