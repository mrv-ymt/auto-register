package tool.auto.regist.friendz;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;

public class AutoRegister {

	private static final String EMAIL = "myhearwillgoonceledion88889999";
	private static final String EMAIL_PASS = "Dragon0104146890";

	private static final String REF_LINK = "https://steward.friendz.io/go?r=NDkxOTAy";
	private static final String INPUT_EMAIL_PATH = "email_list.csv";
	private static final String INPUT_NAME_PATH = "C:\\Users\\RedDevil\\Desktop\\input_name.csv";
	private static final String GECKO_DRIVER = "D:\\Store\\geckodriver-v0.19.1-win64\\geckodriver.exe";
	private static final int PHONE_NUM_LENGTH = 11;

	private static WebDriver driver = null;
	static ClassLoader classLoader = AutoRegister.class.getClassLoader();

	public static void main(String[] arg) throws InterruptedException {

		String email;
		List<String> emailsList = getEmailsList(INPUT_EMAIL_PATH);
		List<String> inputNamesList = getInputNames(INPUT_NAME_PATH);

		System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		if (emailsList.size() > 0) {
			int numOfAcc = 0;

			// Go to web
			driver.get(REF_LINK);
			while (!driver.getCurrentUrl().equals("https://friendz.io/")) {
				TimeUnit.SECONDS.sleep(3);
			}

			driver.get("https://steward.friendz.io/register");
			waitForLoad(driver);

			for (int i = 0; i < emailsList.size(); i++) {

				email = emailsList.get(i);
				fillRegistForm(inputNamesList, email);
				if (existsElement("has-error", false)) {
					if (i < emailsList.size() - 1) {
						email = emailsList.get(++i);
						fillRegistForm(inputNamesList, email);
					} else {
						break;
					}
				}

//				while (!driver.getCurrentUrl().equals("https://steward.friendz.io/waitVerification")) {
//
//					if (existsElement("has-error", false)) {
//						if (i < emailsList.size() - 1) {
//							email = emailsList.get(++i);
//							fillRegistForm(inputNamesList, email);
//							TimeUnit.SECONDS.sleep(3);
//						} else {
//							break;
//						}
//					}
//				}
				numOfAcc++;

				// Confirm mail
				if (numOfAcc == 2) {
					confirmMail();
					numOfAcc = 0;
					driver.get("https://steward.friendz.io/logout");
				}

				driver.get("https://steward.friendz.io/logout");
				waitForLoad(driver);

				driver.get("https://steward.friendz.io/register");
				waitForLoad(driver);
			}
		}

		// Confirm mail
		confirmMail();

		System.out.println("============= DONE ==============");
	}

	private static void fillRegistForm(List<String> inputNamesList, String email) {

		WebElement element;
		element = driver.findElement(By.name("name"));
		element.clear();
		element.sendKeys(getRandomName(inputNamesList));

		element = driver.findElement(By.name("email"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("phone"));
		element.clear();
		element.sendKeys(getRandomPhoneNum(PHONE_NUM_LENGTH));

		element = driver.findElement(By.name("country"));
		element.sendKeys("DE - GERMANY");

		element = driver.findElement(By.name("password"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("password_confirmation"));
		element.clear();
		element.sendKeys(email);
		element.submit();
		waitForLoad(driver);
	}

	private static void confirmMail() throws InterruptedException {

		List<WebElement> confirmPathsList;
		String confirmPath;
		
		try {
			// Login gmail
			driver.get("https://mail.google.com");
			waitForLoad(driver);
			if (!"https://mail.google.com/mail/u/0/#inbox".equals(driver.getCurrentUrl())) {
				WebElement gmail = driver.findElement(By.id("identifierId"));
				gmail.sendKeys(EMAIL);
				driver.findElement(By.id("identifierNext")).click();

				TimeUnit.SECONDS.sleep(2);
				WebElement gmailPass = driver.findElement(By.name("password"));
				gmailPass.sendKeys(EMAIL_PASS);
				driver.findElement(By.id("passwordNext")).click();
			}

			waitForLoad(driver);
			List<WebElement> unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));

			// Open inbox
			for (WebElement i : unReadMailList) {
				if (i.isDisplayed() == true) {
					if ("FriendzDashboard".equals(i.getText())) {
						i.click();
						TimeUnit.SECONDS.sleep(2);
						confirmPathsList = driver.findElements(By.tagName("a"));

						for (WebElement k : confirmPathsList) {
							if (k.getText().indexOf("https://steward.friendz.io/verificationUser") != -1) {
								confirmPath = k.getText();
								String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
								driver.findElement(By.linkText(confirmPath)).sendKeys(selectLinkOpeninNewTab);
							}
						}
					}
				}
			}

			List<String> tabList = new ArrayList<String>();
			tabList.addAll(driver.getWindowHandles());

			while(tabList.size() > 1) {
				driver.switchTo().window(tabList.get(1));
				waitForLoad(driver);
				driver.close();
				driver.switchTo().window(tabList.get(0));
				tabList.remove(1);
			}
		} catch(Exception e) {
			confirmMail();
		}
	}

	private static List<String> getEmailsList(String csvFilePath) {

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

	private static List<String> getInputNames(String inputNamePath) {

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

	private static String getRandomPhoneNum(int length) {

		String numChar = "0123456789";
		StringBuilder phoneNum = new StringBuilder("0");
		Random rand = new Random();
		char charAt;

		for (int i = 0; i < length; i++) {
			charAt = numChar.charAt(rand.nextInt(numChar.length()));
			while (i == 0 && charAt == '0') {
				charAt = numChar.charAt(rand.nextInt(numChar.length()));
			}
			phoneNum.append(charAt);
		}

		return phoneNum.toString();
	}

	private static String getRandomName(List<String> inputNamesList) {
		Random rand = new Random();
		int nameIndex = rand.nextInt(inputNamesList.size());
		int lastIndex = rand.nextInt(inputNamesList.size());
		return inputNamesList.get(nameIndex) + " " + inputNamesList.get(lastIndex);
	}

	private static boolean existsElement(String name, boolean isId) {

		try {
			if (isId) {
				driver.findElement(By.id(name));
			} else {
				driver.findElement(By.className(name));
			}
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	
	static void waitForLoad(WebDriver driver) {
		new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
	}
}