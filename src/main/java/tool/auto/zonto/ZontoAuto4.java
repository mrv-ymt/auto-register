package tool.auto.zonto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import tool.auto.common.CommonUtils;

public class ZontoAuto4 {
	
	private static final String REF_LINK = "https://zonto.world/z133867";

	private static WebDriver driver = null;
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{

		int count = 0;
		String email;
		List<String> emailsList = CommonUtils.getEmailsList("email_list4.txt");
		inputNamesList = CommonUtils.getInputNames();
		driver = CommonUtils.createWebDriver(true);

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
				System.out.println("============= ERROR ==============" + e);
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
		dropdown.selectByVisibleText("Thailand");
		
		element = driver.findElement(By.xpath("//*[@id='index-page_registration']/div[2]/div/div[1]/div[3]/span[2]"));
		element.click();
		element = driver.findElement(By.xpath("//*[@id='welcome-body']/span/span/span[1]/input"));
		element.sendKeys("bangkok");
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

		CommonUtils.confirmMail(driver, email, "Confirm email", "team@zonto.world", 2);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Close tabs that is used to confirm mail except main tab
		List<String> tabList = new ArrayList<String>();
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
	}
}