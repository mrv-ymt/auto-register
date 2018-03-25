package tool.auto.kelta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import tool.auto.common.CommonUtils;

public class KeltaAuto {
	
	private static final String REF_LINK = "https://www.kelta.com/referral/?ref=qcq2c";

	private static WebDriver driver = null;
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{

		int count = 0;
		String email;
		List<String> emailsList = CommonUtils.getEmailsList("email_list.txt");
		inputNamesList = CommonUtils.getInputNames();
		driver = CommonUtils.createWebDriver(false);
		List<String> mewsList = CommonUtils.getMewsList();


		for (int i = 0; i < emailsList.size() ; i++) {
			try {
				count = i + 1;
				System.out.println("====== START" + count + "======="); 
				email = emailsList.get(i);
				if(fillRegistForm(email, mewsList.get(i))) {
					confirmMail(driver, email);
				} else {
					System.out.println("FAILURE");
				}
			} catch (WebDriverException e) {
				System.out.println("============= ERROR ==============" + e);
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
	
	private static boolean fillRegistForm(String email, String ethAdd) throws InterruptedException {	

		boolean isSuccess = true;
		WebElement element;
		driver.get(REF_LINK);
		CommonUtils.waitForLoad(driver, 2000);
		
		if (CommonUtils.existsElement("/html/body/div[3]/div[5]/div/div/a", 3, driver)) {
			try {
				driver.findElement(By.linkText("HIDE")).click();
			} catch(ElementNotInteractableException e) {
				System.out.println("============= ERROR ==============" + e.getMessage());
			}
		}

		element = driver.findElement(By.name("email"));
		element.clear();
		element.sendKeys(email);

		element = driver.findElement(By.name("name"));
		element.clear();
		element.sendKeys(ethAdd);

		element = driver.findElement(By.name("agree"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		
		element = driver.findElement(By.name("submit"));
		element.click();

		CommonUtils.waitForLoad(driver, 2000);
		return isSuccess;
	}

	private static void confirmMail(WebDriver driver, String email) throws InterruptedException {

		List<String> tabList = new ArrayList<String>();
		tabList.addAll(driver.getWindowHandles());
		CommonUtils.confirmMail(driver, email, "Profile link", "noreply@kelta.com", 1, tabList.get(0));

		while(tabList.size() > 1) {
			driver.switchTo().window(tabList.get(1));
			CommonUtils.waitForLoad(driver, 1000);
			
			try {
				driver.findElement(By.className("tlg")).click();
				CommonUtils.waitForLoad(driver, 1000);
				driver.findElement(By.className("next")).click();
				CommonUtils.waitForLoad(driver, 1000);
				driver.findElement(By.className("next")).click();
				CommonUtils.waitForLoad(driver, 1000);
				driver.findElement(By.className("next")).click();
				CommonUtils.waitForLoad(driver, 1000);
				driver.findElement(By.className("next")).click();
				CommonUtils.waitForLoad(driver, 1000);
				driver.findElement(By.className("next")).click();
				CommonUtils.waitForLoad(driver, 2000);
			} catch(WebDriverException e) {
				System.out.println("============= ERROR ==============" + e);
			}
		
			// driver.close();
			// driver.switchTo().window(tabList.get(0));
			//  (JavascriptExecutor) driver.executeScript("window.onbeforeunload = function(e){};");
			// tabList.remove(1);
		}
	}
}