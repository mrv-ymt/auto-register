package tool.auto.zonto;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.auto.common.CommonUtils;


public class ZontoAuto3 {
	
	private static final String EMAIL = "pleasetellmywhyplease123456789";
	private static final String EMAIL_PASS = "Dragon0104146890";
	private static final String REF_LINK = "https://zonto.world/z133910";

	private static WebDriver driver = null;
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{
		
		int count = 0;
		
		String email;
		List<String> emailsList = CommonUtils.getEmailsList("email_list3.txt");
		inputNamesList = CommonUtils.getInputNames();
		Path filePath1 = Paths.get("src", "main", "resources", "service-tool", "chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", filePath1.toString());
		ChromeOptions ChromeOptions = new org.openqa.selenium.chrome.ChromeOptions();
		ChromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		driver = new ChromeDriver(ChromeOptions);
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// Go to web		
		for (int i = 0; i < emailsList.size() ; i++) {
			try {
				count = i;
				email = emailsList.get(i);
				fillRegistForm(email);
				confirmMail(driver, email);
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

		element = driver.findElement(By.name("name"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));
		
		element = driver.findElement(By.name("surname"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));

		element = driver.findElement(By.id("email"));
		element.clear();
		element.sendKeys(email);
		element = driver.findElement(By.xpath("//*[@id='index-page_registration']/div[1]/div/div[2]/div/div"));
		element.click();
		TimeUnit.SECONDS.sleep(1);
		  
		element = driver.findElement(By.id("day-of-birth"));
		Select dropdown = new Select(element);
		dropdown.selectByIndex((int) (Math.random() * 20 + 1));
		
		element = driver.findElement(By.id("month-of-birth"));
		dropdown = new Select(element);
		dropdown.selectByIndex((int)(Math.random() * 11 + 1));
		
		element = driver.findElement(By.id("year-of-birth"));
		dropdown = new Select(element);
		dropdown.selectByIndex((int)(Math.random() * 10 + 8));
		
		element = driver.findElement(By.id("country"));
		dropdown = new Select(element);
		dropdown.selectByVisibleText("Singapore");
		
		element = driver.findElement(By.xpath("//*[@id='index-page_registration']/div[2]/div/div[1]/div[3]/span[2]"));
		element.click();
		element = driver.findElement(By.xpath("//*[@id='welcome-body']/span/span/span[1]/input"));
		element.sendKeys("Singapore");
		TimeUnit.SECONDS.sleep(1);
		element = driver.findElement(By.xpath("//*[@id='welcome-body']/span/span/span[2]"));
		element.click();
		
		element = driver.findElement(By.id("language"));
		dropdown = new Select(element);
		dropdown.selectByIndex(3);
		element = driver.findElement(By.name("registration_password"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("registration_password_confirmation"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.xpath("//*[@id='index-page_registration']/div[2]/div/label"));
		element.click();
		element.submit();
		CommonUtils.waitForLoad(driver);
		TimeUnit.SECONDS.sleep(2);
	}

	private static void confirmMail(WebDriver driver, String email) throws InterruptedException {

		System.out.println("Confirming mail");
		WebElement element;
		
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
				element = driver.findElement(By.name("password"));
				element.sendKeys(EMAIL_PASS);
				
				WebDriverWait wait = new WebDriverWait(driver, 200);
				element = wait.until(ExpectedConditions
						.elementToBeClickable(By.id("passwordNext")));
				element.click();
			}
			CommonUtils.waitForLoad(driver);
			TimeUnit.SECONDS.sleep(1);

			int count = 0;
			String confirmPath;
			List<String> tabList = null;
			List<WebElement> unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
			List<WebElement> confirmPathsList;
			JavascriptExecutor js = (JavascriptExecutor) driver;

			while (unReadMailList.size() == 0 && count < 3) {
				driver.get("https://mail.google.com");
				TimeUnit.SECONDS.sleep(2);
				unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
				count++;
			}
			for (WebElement unReadMail : unReadMailList) {
				if (unReadMail.isDisplayed() && ("team@zonto.world".equals(unReadMail.getAttribute("email")))) {

					// Read confirm mail
					unReadMail.click();
					CommonUtils.waitForLoad(driver);
					TimeUnit.SECONDS.sleep(3);
					confirmPathsList = driver.findElements(By.tagName("a"));
					for (WebElement elm : confirmPathsList) {
						if (elm.getText().indexOf("Confirm email") != -1) {
							
							confirmPath = elm.getAttribute("href");
							// driver.findElement(By.linkText(confirmPath)).sendKeys(selectLinkOpeninNewTab);
							
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
						TimeUnit.SECONDS.sleep(2);
						js.executeScript("document.getElementById('logout-form').submit();");
						CommonUtils.waitForLoad(driver);
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