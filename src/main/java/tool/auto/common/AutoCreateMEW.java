package tool.auto.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AutoCreateMEW {
	private static final String PASS = "Dragon01689933430";
	private static final String MEW = "https://www.myetherwallet.com/";
	private static ChromeDriver driver;

	public static void main(String[] args) {
		int dem = 0;
		try {
			Path filePath1 = Paths.get("src", "main", "resources", "service-tool", "chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", filePath1.toString());
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement element;

			// Disable popup
			driver.get(MEW);
			TimeUnit.SECONDS.sleep((long) 1.5);
			disablePopup();

			String privateKey;
			String mewWallet = null;

			for (int j = 0; j < 20; j++) {
				// Login MEW
				driver.get(MEW);
				TimeUnit.SECONDS.sleep((long) 1.5);
				element = driver.findElement(By.name("password"));
				element.clear();
				element.sendKeys(PASS);
				clickButton("a", "Create New Wallet");
				clickButton("span", "Download");
				clickButton("span", "I understand. Continue.");
				privateKey = driver.findElement(By.tagName("textarea")).getText();
				clickButton("span", "Save Your Address.");
				element = driver.findElement(By.name("222"));
				element.click();
				driver.findElement(By.id("aria6")).sendKeys(privateKey);
				TimeUnit.SECONDS.sleep(1);
				clickButton("a", "Unlock");
				List<WebElement> tagList = driver.findElements(By.tagName("input"));
				for (WebElement k : tagList) {
					if (k.getAttribute("value").indexOf("0x") != -1) {
						mewWallet = k.getAttribute("value");
						break;
					}
				}
				System.out.println(privateKey + "-" + mewWallet);
			}
			System.out.println("============= DONE ==============");
			dem++;
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(dem);
		}
	}

	private static void disablePopup() throws InterruptedException {
		driver.get(MEW);
		TimeUnit.SECONDS.sleep((long) 1.5);
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

	private static void clickButton(String tag, String text) {
		List<WebElement> tagList = driver.findElements(By.tagName("a"));
		for (WebElement k : tagList) {
			if (k.getText().indexOf(text) != -1) {
				k.click();
				break;
			}
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}