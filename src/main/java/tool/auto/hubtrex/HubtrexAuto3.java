package tool.auto.hubtrex;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import tool.auto.common.CommonUtils;

public class HubtrexAuto3 {

	private static final int MAX_NUM_MAIL = 120;
	private static final String EMAIL = "toithayhoavangtrencoxanh999999";
	private static final String EMAIL_PASS = "Dragon0104146890";
	private static final String REF_LINK = "https://account.hubtrex.com/FND7CA8C";

	private static List<String> emailsList;
	private static List<String> inputNamesList;
	private static WebDriver driver;

	public static void main(String[] arg) throws InterruptedException {

		emailsList = CommonUtils.getEmailsList("email_list2.txt");
		inputNamesList = CommonUtils.getInputNames();
		List<String> mewsList = CommonUtils.getMewsList("mew-list.txt");

		Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", filePath.toString());

		Path filePath1 = Paths.get("src", "main", "resources","service-tool", "chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", filePath1.toString());
		ChromeOptions options = new ChromeOptions();
		options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		
		try {
			if (emailsList.size() > 0) {
				String email;
				String ethAddress;
				boolean isSuccess = true;

				for (int i = 0; i < mewsList.size(); i++) {
					System.out.println("====== START" + (i + 1) + "=======");
					email = emailsList.get(i);
					ethAddress = mewsList.get(i);
					
					System.out.println(email);
					
					try {
						isSuccess = fillRegistForm(email, ethAddress);
						CommonUtils.waitForLoad(driver);
						TimeUnit.SECONDS.sleep(1);
						
						if (isSuccess) {
							confirmMail(driver, email);
							CommonUtils.waitForLoad(driver);
							System.out.println("===> SUCCESS");
						} else {
							System.out.println("====> FAILURE");
						}
						
						System.out.println("====== END =======");
					} catch (Exception e) {
						System.out.println("==== ERROR email: " + email + ": " + e);
						CommonUtils.writeErrorMessages(email);
						CommonUtils.waitForLoad(driver);
						driver.manage().deleteAllCookies();
					}
				}
			}

			// Confirm mail
			System.out.println("=== DONE===");
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	

	private static boolean fillRegistForm(String email, String ethMew) throws InterruptedException { 

		WebElement element;
		driver.get(REF_LINK);
		CommonUtils.waitForLoad(driver);

		element = driver.findElement(By.name("register-form[first_name]"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));

		element = driver.findElement(By.name("register-form[last_name]"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));

		element = driver.findElement(By.name("register-form[email]"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("register-form[ethereum_address]"));
		element.clear();
		element.sendKeys(ethMew);

		element = driver.findElement(By.name("register-form[password]"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("register-form[password_confirm]"));
		element.clear();
		element.sendKeys(email);

		driver.findElement(By.name("register-form[agree]")).click();
		driver.findElement(By.name("register-button")).click();
		CommonUtils.waitForLoad(driver);
		TimeUnit.MILLISECONDS.sleep(200);
		while (!driver.getCurrentUrl().equals("https://account.hubtrex.com/thank-you")) {
			TimeUnit.MILLISECONDS.sleep(100);
			if (CommonUtils.existsElement("help-block-error", 1, driver)) {
				return false;
			}
		}
		
		driver.get("https://account.hubtrex.com/user/resend");
		CommonUtils.waitForLoad(driver);
		TimeUnit.SECONDS.sleep(2);
		
		element = driver.findElement(By.name("resend-form[email]"));
		element.clear();
		element.sendKeys(email);
		element.submit();
		CommonUtils.waitForLoad(driver);
		TimeUnit.SECONDS.sleep(3);
		return true;
	}

	/**
	 * Login to gmail to confirm mail
	 * 
	 * @throws InterruptedException
	 */
	private static void confirmMail(WebDriver driver, String email) throws InterruptedException {

		System.out.println("Confirming mail");
		try {
			// Login gmail
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
			String confirmPath;
			List<String> tabList = null;
			List<WebElement> unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
			List<WebElement> confirmPathsList;

			while (unReadMailList.size() == 0 && count < 3) {
				driver.get("https://mail.google.com");
				TimeUnit.SECONDS.sleep(2);
				unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
				count++;
			}
			for (WebElement unReadMail : unReadMailList) {
				if (unReadMail.isDisplayed() && "support@hubtrex.com".equals(unReadMail.getText())) {

					// Read confirm mail
					unReadMail.click();
					CommonUtils.waitForLoad(driver);
					TimeUnit.SECONDS.sleep(2);
					confirmPathsList = driver.findElements(By.tagName("a"));
					for (WebElement elm : confirmPathsList) {
						if (elm.getText().indexOf("https://account.hubtrex.com/user/confirm") != -1) {
							
							confirmPath = elm.getAttribute("href");
							// driver.findElement(By.linkText(confirmPath)).sendKeys(selectLinkOpeninNewTab);
							JavascriptExecutor js = (JavascriptExecutor) driver;
							js.executeScript("window.open('" + confirmPath + "','_blank');");
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
						
						try {
							driver.findElement(By.xpath("//*[@id=\"sidebar\"]/div[2]/div[2]/a[2]")).click();
							CommonUtils.waitForLoad(driver);
						} catch (Exception e) {
							System.out.println("==== Failure ====");
						}
						
						driver.close();
						driver.switchTo().window(tabList.get(0));
						tabList.remove(1);
					}
					break;
				}
			}
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			confirmMail(driver, email);
		}
	}
}