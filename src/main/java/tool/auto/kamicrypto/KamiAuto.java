package tool.auto.kamicrypto;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.auto.common.CommonUtils;

public class KamiAuto {
	
	private static final String REF_LINK = "https://cryptokami.com/?ref=125640";
	private static final String PASSWORD = "Dragon559270";

	private static WebDriver driver = null;
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{

		int count = 0;
		String email;
		List<String> emailsList = CommonUtils.getEmailsList("email_list.txt");
		inputNamesList = CommonUtils.getInputNames();
		driver = CommonUtils.createWebDriver(false);

		for (int i = 0; i < emailsList.size() ; i++) {
			try {
				count = i + 1;
				System.out.println("====== START" + count + "======="); 
				email = emailsList.get(i);
				if(fillRegistForm(email)) {
					confirmMail(driver, email);
				} else {
					System.out.println("FAILURE");
				}
			} catch (WebDriverException e) {
				if (e.toString().indexOf("ConnectException") != -1) {
					break;
				}
			} catch (Exception e) {
				System.out.println("============= ERROR ==============" + e);
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
		CommonUtils.waitForLoad(driver, 1000);
		WebDriverWait wait = new WebDriverWait(driver, 200);

		if (CommonUtils.existsElement("onesignal-popover-cancel-button", 4, driver)) {
			driver.findElement(By.id("onesignal-popover-cancel-button")).click();
		}

		TimeUnit.MILLISECONDS.sleep(100);
		driver.findElement(By.linkText("REGISTER")).click();

		CommonUtils.waitForLoad(driver, 500);
		
		element = driver.findElement(By.id("EMAIL"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.id("PASSWORD1"));
		element.clear();
		element.sendKeys(PASSWORD);

		element = driver.findElement(By.id("PASSWORD2"));
		element.clear();
		element.sendKeys(PASSWORD);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		element = driver.findElement(By.xpath("//*[@id='bonus50-full-signup-form']/div[5]/div/div/iframe"));
		driver.switchTo().frame(element);
		element = driver.findElement(By.id("recaptcha-anchor"));
		element.click();
		System.out.println("Input Capcha");

		wait.until(ExpectedConditions
				.attributeContains(element, "aria-checked", "true"));
		driver.switchTo().defaultContent();

		element = driver.findElement(By.id("PASSWORD2"));
		element.submit();

		while(!CommonUtils.existsElement("//*[@id=\"signup-bonus50\"]/div/div/div[2]/h4", 3, driver)) {
			if (CommonUtils.existsElement("error-response", 1, driver)) {
				isSuccess = false;
				break;
			} else {
				TimeUnit.MILLISECONDS.sleep(200);
				element.submit();
			}
		}

		CommonUtils.waitForLoad(driver, 1000);
		return isSuccess;
	}

	private static void confirmMail(WebDriver driver, String email) throws InterruptedException {

		CommonUtils.confirmMail(driver, email, "CONFIRM EMAIL NOW", "contact@cryptokami.com", 1);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Close tabs that is used to confirm mail except main tab
		List<String> tabList = new ArrayList<String>();
		tabList.addAll(driver.getWindowHandles());
		while(tabList.size() > 1) {
			driver.switchTo().window(tabList.get(1));
			CommonUtils.waitForLoad(driver, 1000);
			driver.close();
			driver.switchTo().window(tabList.get(0));
			js.executeScript("window.onbeforeunload = function(e){};");
			tabList.remove(1);
		}
	}
}