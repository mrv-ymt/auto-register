package tool.auto.cmt;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.auto.common.CommonUtils;

public class KeltaAuto2 {
	
	private static final String MEW = "https://www.myetherwallet.com/";

	private static WebDriver driver = null;
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException, InterruptedException{

		List<String> private1 = CommonUtils.getEmailsList("private1.txt");
		List<String> private2 = CommonUtils.getEmailsList("private2.txt");
		List<String> private3 = CommonUtils.getEmailsList("private4.txt");
		inputNamesList = CommonUtils.getInputNames();
		driver = CommonUtils.createWebDriver(false);
		String privateKey;
		disablePopup();
		for (int i = 0; i < private1.size() ; i++) {
			for (int j = 0; j < private2.size() ; j++) {
				for (int k = 0; k < private3.size() ; k++) {
					privateKey = private1.get(i) + private2.get(j) + private3.get(k);

					if (privateKey.length() == 64) {
						fillRegistForm(privateKey);
					}
				}
			}
		}
		System.out.println("============= DONE ==============");
	}
	
	private static boolean fillRegistForm(String privateKey) throws InterruptedException {	

		boolean isSuccess = true;
		WebElement element;
		String mewWallet = null;

		driver.get("https://www.myetherwallet.com/");
		CommonUtils.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 200);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("icon-close")));
		element.click();
		
		element = driver.findElement(By.linkText("Send Ether & Tokens"));
		element.click();
		CommonUtils.waitForLoad(driver);
		
		element = driver.findElement(By.name("298"));
		element.click();

		element = driver.findElement(By.id("aria6"));
		element.sendKeys(privateKey);

		element = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Unlock")));
		element.click();
		CommonUtils.waitForLoad(driver);

		element = driver.findElement(By.xpath("/html/body/section[1]/div/main/article[2]/section/wallet-balance-drtv/aside/div[1]/ul[1]/span"));
		mewWallet = element.getText();

		driver.get("https://ethplorer.io/address/" + mewWallet);
		CommonUtils.waitForLoad(driver);
		
		List<WebElement> listElement = driver.findElements(By.linkText("CyberMiles Token"));
		if (listElement != null && listElement.size() > 0) {
			System.out.println(privateKey);
		}

		return isSuccess;
	}

	private static void disablePopup() throws InterruptedException {
		driver.get(MEW);
		CommonUtils.waitForLoad(driver);
		// Disable popup
		WebElement element;
		element = driver.findElement(By.linkText("MyEtherWallet is not a Bank"));
		element.click();
		element = driver.findElement(By.linkText("MyEtherWallet is an Interface"));
		element.click();
		element = driver.findElement(By.linkText("WTF is a Blockchain?"));
		element.click();
		element = driver.findElement(By.linkText("But...why?"));
		element.click();
		element = driver.findElement(By.linkText("What's the Point of MEW then?"));
		element.click();
		element = driver.findElement(By.linkText("How To Protect Yourself & Your Funds"));
		element.click();
		element = driver.findElement(By.linkText("How To Protect Yourself from Scams"));
		element.click();
		element = driver.findElement(By.linkText("How To Protect Yourself from Loss"));
		element.click();
		element = driver.findElement(By.linkText("One more click & you're done! 🤘"));
		element.click();
		element = driver.findElement(By.linkText("OMG, please just let me send FFS."));
		element.click();
	}
}