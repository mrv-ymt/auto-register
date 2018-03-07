package tool.auto.regist.syn;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.auto.common.CommonUtils;

public class SynAutoRegister {
	
	private static final String EMAIL = "canyouopenyoucomputerplease123@gmail.com";
	private static final String EMAIL_PASS = "Dragon0104146890";
	private static final String REF_LINK = "https://tokensale.synapse.ai/r/186687";

	private static WebDriver driver = null;
	private static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{

		int indexBegin = 0;
		int count = 0;
		String email = null;
		List<String> emailsList = CommonUtils.getEmailsList("email_list.txt");
		inputNamesList = CommonUtils.getInputNames();

		Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", filePath.toString());
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		driver = new FirefoxDriver(firefoxOptions);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		
//		Path filePath = Paths.get("src", "main", "resources","service-tool", "chromedriver.exe");
//		System.setProperty("webdriver.chrome.driver", filePath.toString());
//		ChromeOptions options = new ChromeOptions();
//		options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
//		driver = new ChromeDriver(options);
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Go to web		
		for (int i = indexBegin; i < emailsList.size() ; i++) {
			try {
				System.out.println("======================");
				count = i + 1;
				System.out.println("START" + count);
				email = emailsList.get(i);

				if (fillRegistForm(email)) {
					CommonUtils.waitForLoad(driver);
					confirmMail(email);
				} else {
					System.out.println(email + " is failure");
				}
				System.out.println("END" + count);
			} catch (UnhandledAlertException f) {
				System.out.println(count + "==> Error mail: " + email);
				break;
			} catch (Exception e) {
				System.out.println("============= EROR ==============" + e);
				System.out.println(count);
			}
		}
		System.out.println("============= DONE ==============");
		System.out.println(count);
	}
	
	private static boolean fillRegistForm(String email) throws InterruptedException {	

		boolean isSuccess = true;
		WebElement element;
		driver.get(REF_LINK);
		CommonUtils.waitForLoad(driver);
		while (!driver.getCurrentUrl().equals("https://tokensale.synapse.ai/")) {
			TimeUnit.SECONDS.sleep(1);
		}
		element = driver.findElement(By.className("btn-success"));
		element.click();
		CommonUtils.waitForLoad(driver);
		TimeUnit.SECONDS.sleep(3);

		element = driver.findElement(By.name("user[country]"));
		Select dropdown = new Select(element);
		dropdown.selectByIndex((int) (Math.random() * 100 + 1));

		element = driver.findElement(By.name("user[first_name]"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));

		element = driver.findElement(By.name("user[last_name]"));
		element.clear();
		element.sendKeys(CommonUtils.getRandomName(inputNamesList));

		element = driver.findElement(By.name("user[email]"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("user[password]"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("user[password_confirmation]"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.id("user_accredited"));
		element.click();

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		TimeUnit.SECONDS.sleep(1);
		element = driver.findElement(By.xpath("//*[@id=\"new_user\"]/div[7]/div/div/iframe"));
		driver.switchTo().frame(element);
		element = driver.findElement(By.id("recaptcha-anchor"));
		element.click();
		System.out.println("Input Capcha");

		WebDriverWait wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions
				.attributeContains(element, "aria-checked", "true"));
		driver.switchTo().defaultContent();
		element = driver.findElement(By.name("commit"));
		element.click();
		
		while (!driver.getCurrentUrl().equals("https://tokensale.synapse.ai/unconfirmed")) {
			TimeUnit.MILLISECONDS.sleep(100);
			if (CommonUtils.existsElement("error_explanation", 3, driver)) {
				isSuccess = false;
				break;
			}
		}
		return isSuccess;
	}

	private static void confirmMail(String email) throws InterruptedException {

		System.out.println("Confirming mail");

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
		TimeUnit.SECONDS.sleep(1);

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
				if ("dan@synapse.ai".equals(j.getAttribute("email"))) {

					// Open synapse mail
					j.click();
					CommonUtils.waitForLoad(driver);
					((JavascriptExecutor) driver).executeScript("window.onbeforeunload = function(e){};");

					listATag = driver.findElements(By.linkText("Confirm my account"));
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();", listATag.get(listATag.size() - 1));
					CommonUtils.waitForLoad(driver);
					
					try {

						// Go to tab 2
						tabList = new ArrayList<String>();
						tabList.addAll(driver.getWindowHandles());
						driver.switchTo().window(tabList.get(1));
						CommonUtils.waitForLoad(driver);
						wait.until(ExpectedConditions
								.visibilityOfElementLocated(By.linkText("Click here to be redirected to sign in.")));
	
						// Re-signin
						driver.get("https://tokensale.synapse.ai/users/sign_in");
						CommonUtils.waitForLoad(driver);
						TimeUnit.SECONDS.sleep(2);
						element = driver.findElement(By.name("user[email]"));
						element.clear();
						element.sendKeys(email);
	
						element = driver.findElement(By.name("user[password]"));
						element.clear();
						element.sendKeys(email);
	
						element = driver.findElement(By.name("commit"));
						element.click();
						CommonUtils.waitForLoad(driver);

						element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log Out")));
						element.click();
						CommonUtils.waitForLoad(driver);
						System.out.println(email);
					} catch (Exception e) {
						System.out.println("Cannot re-signin mail: " + email);
					}

					driver.switchTo().window(tabList.get(0));
					CommonUtils.waitForLoad(driver);
					((JavascriptExecutor) driver).executeScript("window.onbeforeunload = function(e){};");
					driver.close();
					driver.switchTo().window(tabList.get(1));
					return;
				}
			}
		}
	}
}