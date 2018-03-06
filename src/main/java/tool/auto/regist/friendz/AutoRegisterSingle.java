package tool.auto.regist.friendz;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import tool.auto.common.CommonUtils;

public class AutoRegisterSingle {

	private static final int MAX_NUM_MAIL = 120;
	private static final String EMAIL = "pleasetellmywhyplease123456789";
	private static final String EMAIL_PASS = "Dragon0104146890";

	private static final int PHONE_NUM_LENGTH = 11;

	List<String> emailsList;
	List<String> inputNamesList;

	public static void main(String[] arg) throws InterruptedException {

		List<String> emailsList = CommonUtils.getEmailsList();
		List<String> inputNamesList = CommonUtils.getInputNames();

		Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", filePath.toString());
		List<String> refUrlsList = new ArrayList<String>();
		refUrlsList.add("https://steward.friendz.io/go?r=NzE2OTE3");

		int fromIndex;
		int toIndex;
		int numOfEmail = emailsList.size();
		
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

			autoRegister(refUrlsList.get(i), fromIndex, toIndex, emailsList, inputNamesList, driver);
		}
	}
	
	/**
	 * Using FirefoxDriver to control register process
	 * 
	 * @param refUrl
	 * @param emailsList
	 * @param inputNamesList
	 * @param fromIndex
	 * @param toIndex
	 */
	private static void autoRegister(String refUrl, int fromIndex, int toIndex, List<String> emailsList,
			List<String> inputNamesList, WebDriver driver) {

		try {

			if (emailsList.size() > 0) {
				String email;

				// Go to web
				driver.get(refUrl);
				while (!("https://friendz.io/").equals(driver.getCurrentUrl())) {
					TimeUnit.SECONDS.sleep(3);
				}

				driver.get("https://steward.friendz.io/register");
				CommonUtils.waitForLoad(driver);

				for (int i = fromIndex; i < toIndex; i++) {
					
					System.out.println("====== START" + (i + 1) + "=======");
					email = emailsList.get(i);
					System.out.println(email);
					try {
						fillRegistForm(inputNamesList, email, driver);
						while (!("https://steward.friendz.io/waitVerification").equals(driver.getCurrentUrl())) {

							if (CommonUtils.existsElement("has-error", 1, driver)) {
								if (i < emailsList.size() - 1) {
									email = emailsList.get(++i);
									fillRegistForm(inputNamesList, email, driver);
									TimeUnit.SECONDS.sleep(3);
								} else {
									break;
								}
							}
						}

						// Confirm mail
						confirmMail(driver);

						TimeUnit.SECONDS.sleep(2);
						driver.get("https://steward.friendz.io/logout");
						CommonUtils.waitForLoad(driver);

						driver.get("https://steward.friendz.io/register");
						CommonUtils.waitForLoad(driver);
						System.out.println("====== END =======");
					} catch (Exception e) {
						System.out.println("==== ERROR email: " + email + ": " + e);
						CommonUtils.writeErrorMessages(email);
						
						driver.get("https://steward.friendz.io/logout");
						while (!("https://steward.friendz.io/login").equals(driver.getCurrentUrl())) {
							TimeUnit.SECONDS.sleep(3);
						}

						driver.get("https://steward.friendz.io/register");
						while (!("https://steward.friendz.io/register").equals(driver.getCurrentUrl())) {
							TimeUnit.SECONDS.sleep(2);
						}
					}
				}
			}

			// Confirm mail
			confirmMail(driver);
			System.out.println("=== DONE: " + refUrl);
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	private static void fillRegistForm(List<String> inputNamesList, String email, WebDriver driver) {

		WebElement element;
		element = driver.findElement(By.name("name"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));

		element = driver.findElement(By.name("email"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("phone"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomPhoneNum(PHONE_NUM_LENGTH));

		element = driver.findElement(By.name("country"));
		element.sendKeys("DE - GERMANY");

		element = driver.findElement(By.name("password"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("password_confirmation"));
		element.clear();
		element.sendKeys(email);
		element.submit();
		CommonUtils.waitForLoad(driver);
	}

	/**
	 * Login to gmail to confirm mail
	 * 
	 * @throws InterruptedException
	 */
	private static void confirmMail(WebDriver driver) throws InterruptedException {

		String confirmPath;
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
		List<String> tabList;
		List<WebElement> confirmPathsList;

		try {
			
			// Open Gmail
			driver.get("https://mail.google.com");
			CommonUtils.waitForLoad(driver);
			TimeUnit.SECONDS.sleep(1);
			if (!"https://mail.google.com/mail/u/0/#inbox".equals(driver.getCurrentUrl())) {
				WebElement gmail = driver.findElement(By.id("identifierId"));
				gmail.sendKeys(EMAIL);
				driver.findElement(By.id("identifierNext")).click();
				CommonUtils.waitForLoad(driver);
				TimeUnit.MILLISECONDS.sleep(500);
				WebElement gmailPass = driver.findElement(By.name("password"));
				gmailPass.sendKeys(EMAIL_PASS);
				driver.findElement(By.id("passwordNext")).click();
			}
			CommonUtils.waitForLoad(driver);
			TimeUnit.SECONDS.sleep(1);
			
			int count = 0;
			List<WebElement> unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));

			while (unReadMailList.size() == 0 && count < 3) {
				driver.get("https://mail.google.com");
				TimeUnit.SECONDS.sleep(2);
				unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
				count++;
			}

			for (WebElement unReadMail : unReadMailList) {
				if (unReadMail.isDisplayed() && "FriendzDashboard".equals(unReadMail.getText())) {

					// Read confirm mail
					unReadMail.click();
					CommonUtils.waitForLoad(driver);
					TimeUnit.SECONDS.sleep(2);
					confirmPathsList = driver.findElements(By.tagName("a"));
					for (WebElement k : confirmPathsList) {
						if (k.getText().indexOf("https://steward.friendz.io/verificationUser") != -1) {
							confirmPath = k.getText();
							driver.findElement(By.linkText(confirmPath)).sendKeys(selectLinkOpeninNewTab);
							TimeUnit.SECONDS.sleep(1);
							break;
						}
					}

					// Close tabs that is used to confirm mail except main tab
					tabList = new ArrayList<String>();
					tabList.addAll(driver.getWindowHandles());
					while(tabList.size() > 1) {
						driver.switchTo().window(tabList.get(1));
						CommonUtils.waitForLoad(driver);
						driver.close();
						driver.switchTo().window(tabList.get(0));
						tabList.remove(1);
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR:" + e);
			confirmMail(driver);
		}
	}
}