package tool.auto.alpha;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.auto.common.CommonUtils;


public class alpha_eventum_network {
	
	private static final String EMAIL = "votoan8393xahahababylove124445@gmail.com";
	private static final String EMAIL_PASS = "callofduty4";
	private static final String REF_LINK = "https://alpha.eventum.network/?ref=5GJakgge";

	private static WebDriver driver = null;
	static ClassLoader classLoader = alpha_eventum_network.class.getClassLoader();
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{
		
		int count = 0;
		
		String email;
		List<String> emailsList = CommonUtils.getEmailsList("email_list_alpha.txt");
		
		Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", filePath.toString());
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		driver = new FirefoxDriver(firefoxOptions);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// Go to web		
		for (int i = 0; i < 2000 ; i++) {
			try {
				count = i;
				email = emailsList.get(i);
				fillRegistForm(email);
				CommonUtils.waitForLoad(driver);
//				confirmMail(email);
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
		CommonUtils.waitForLoad(driver);
		element = driver.findElement(By.className("button--m"));
		element.click();
		CommonUtils.waitForLoad(driver);
		int count = 0;

		List<WebElement> elementList = driver.findElements(By.className("input--text-field"));
		elementList.get(0).sendKeys(email);
		elementList.get(1).sendKeys(email);
		elementList.get(2).sendKeys(email);
		elementList.get(2).submit();
		TimeUnit.SECONDS.sleep(1);
		
//		while(CommonUtils.existsElement("/html/body/div/aside/div/div[1]/article/form/button", 3, driver) && count < 10) {
//			elementList.get(2).submit();
//			count++;
//		}
		
		TimeUnit.SECONDS.sleep(10);
	}

	private static void confirmMail(String email) throws InterruptedException {
		
		// Login gmail
		driver.get("https://accounts.google.com/signin/v2/identifier?continue="
				+ "https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
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

		int count = 0;
		List<String> tabList = null;
		List<WebElement> listATag;
		List<WebElement> unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		while (unReadMailList.size() == 0 && count < 3) {
			driver.get("https://mail.google.com");
			TimeUnit.SECONDS.sleep(2);
			unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
			count++;
		}

		for (WebElement j : unReadMailList) {
			if (j.isDisplayed() == true) {
				if ("team@eventum.network".equals(j.getAttribute("email"))) {

					// Open synapse mail
					j.click();
					CommonUtils.waitForLoad(driver);
					listATag = driver.findElements(By.linkText("Confirm your email"));
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();", listATag.get(listATag.size() - 1));
					CommonUtils.waitForLoad(driver);
					
					try {

						// Go to tab 2
						tabList = new ArrayList<String>();
						tabList.addAll(driver.getWindowHandles());
						driver.switchTo().window(tabList.get(1));
						element = driver.findElement(By.className("fa-caret-down"));
						Actions action = new Actions(driver);
						action.moveToElement(element).build().perform();
						element = driver.findElement(By.linkText("Log out"));
						element.click();
						CommonUtils.waitForLoad(driver);
						System.out.println(email);
					} catch (Exception e) {
						System.out.println("Cannot re-signin mail: " + email);
					}

					driver.switchTo().window(tabList.get(0));
					CommonUtils.waitForLoad(driver);
					driver.close();
					driver.switchTo().window(tabList.get(1));
					return;
				}
			}
		}
	}
}