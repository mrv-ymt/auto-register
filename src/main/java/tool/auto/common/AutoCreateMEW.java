package tool.auto.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
			Set<String> mewsList = new LinkedHashSet<String>();

			// Disable popup
			driver.get(MEW);
			TimeUnit.SECONDS.sleep((long) 1.5);
			disablePopup();

			String privateKey;
			String mewWallet = null;
			Actions actions = new Actions(driver);
			WebDriverWait wait = new WebDriverWait(driver, 200);

			for (int j = 0; j < 2; j++) {
				// Login MEW
				driver.get(MEW);
				CommonUtils.waitForLoad(driver);
				TimeUnit.SECONDS.sleep((long) 1.5);
				
				element = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.className("icon-close")));
				element.click();
				
				element = driver.findElement(By.name("password"));
				element.clear();
				element.sendKeys(PASS);
				clickButton("a", "Create New Wallet");
				clickButton("span", "Download");
				TimeUnit.SECONDS.sleep(1);
				
				element = driver.findElement(By.className("btn-danger"));
				actions.moveToElement(element).click().perform();
				
				TimeUnit.SECONDS.sleep(1);
				privateKey = driver.findElement(By.tagName("textarea")).getText();
				
				element = driver.findElement(By.className("btn-sm"));
				actions.moveToElement(element).click().perform();
				
				TimeUnit.SECONDS.sleep(1);
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
				mewsList.add(mewWallet + "," + privateKey);
			}
			writeToFile(mewsList);
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
			e.printStackTrace();
		}
	}
	/**
	 * Write mewsList to file
	 * 
	 * @param mewsList 
	 */
	public static void writeToFile(Set<String> mewsList) {

		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;

		try {
			String currentLine;
			StringBuilder fileContent = new StringBuilder();

			Path filePath = Paths.get("src", "main", "resources", "mews.csv");
			File logFile = new File(filePath.toString());

			// If the log file existed then read content of file, after that append new content into that one.
			if (logFile.length() > 0) {
				fileReader = new FileReader(logFile);
				bufferedReader = new BufferedReader(fileReader);

				while ((currentLine = bufferedReader.readLine()) != null) {
					if (currentLine != null) {
						fileContent.append(currentLine + "\n");
					}
				}
			}

			for (String mewInfo : mewsList) {
				fileContent.append(mewInfo + "\n");
			}

			fileWriter = new FileWriter(logFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(fileContent.toString());
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
			}
		}
	}
}