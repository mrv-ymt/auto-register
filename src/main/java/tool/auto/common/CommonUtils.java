package tool.auto.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonUtils {
	
	private static final String EMAIL_PASS = "Dragon0104146890";

	/**
	 * Get list of email from file
	 * 
	 * @return List<String>
	 */
	public static List<String> getEmailsList() {

		List<String> emailsList = new ArrayList<String>();
		Path inputNamesPath = Paths.get("src", "main", "resources", "email_list.txt");
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(inputNamesPath.toString());
			br = new BufferedReader(fr);

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				emailsList.add(sCurrentLine.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return emailsList;
	}
	
	/**
	 * Get list of email from file
	 * 
	 * @return List<String>
	 */
	public static List<String> getEmailsList(String fileName) {

		List<String> emailsList = new ArrayList<String>();
		Path inputNamesPath = Paths.get("src", "main", "resources", fileName);
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(inputNamesPath.toString());
			br = new BufferedReader(fr);

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				emailsList.add(sCurrentLine.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return emailsList;
	}

	/**
	 * Get list of name from input_name.csv file
	 * 
	 * @return List<String>
	 */
	public static List<String> getInputNames() {

		List<String> inputNamesList = new ArrayList<String>();
		Path inputNamesPath = Paths.get("src", "main", "resources", "input-name", "input_name.txt");
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(inputNamesPath.toString());
			br = new BufferedReader(fr);

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				inputNamesList.add(sCurrentLine.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return inputNamesList;
	}
	
	/**
	 * Get list of name from input_name.csv file
	 * 
	 * @return List<String>
	 */
	public static List<String> getMewsList() {

		List<String> mewsList = new ArrayList<String>();
		Path inputNamesPath = Paths.get("src", "main", "resources", "mew", "mew-list.txt");
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(inputNamesPath.toString());
			br = new BufferedReader(fr);

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				mewsList.add(sCurrentLine.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return mewsList;
	}
	
	/**
	 * Get list of name from input_name.csv file
	 * 
	 * @return List<String>
	 */
	public static List<String> getMewsList(String fileName) {

		List<String> mewsList = new ArrayList<String>();
		Path inputNamesPath = Paths.get("src", "main", "resources", "mew", fileName);
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(inputNamesPath.toString());
			br = new BufferedReader(fr);

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				mewsList.add(sCurrentLine.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return mewsList;
	}
	
	/**
	 * Get random name from list of input name
	 * 
	 * @param inputNamesList
	 * @return name;
	 */
	public static String getRandomName(List<String> inputNamesList) {
		Random rand = new Random();
		int nameIndex = rand.nextInt(inputNamesList.size());
		return inputNamesList.get(nameIndex);
	}
	
	/**
	 * Get random name from list of input name
	 * 
	 * @param inputNamesList
	 * @return name;
	 */
	public static String getRandomPhoneNum(int length) {

		String numChar = "0123456789";
		StringBuilder phoneNum = new StringBuilder("0");
		Random rand = new Random();
		char charAt;

		for (int i = 0; i < length; i++) {
			charAt = numChar.charAt(rand.nextInt(numChar.length()));
			while (i == 0 && charAt == '0') {
				charAt = numChar.charAt(rand.nextInt(numChar.length()));
			}
			phoneNum.append(charAt);
		}

		return phoneNum.toString();
	}
	
	/**
	 * Get random username
	 * 
	 * @param inputNamesList
	 * @return name;
	 */
	public static String getRandomUsername(List<String> inputNamesList) {
		Random rand = new Random();
		int nameIndex = rand.nextInt(inputNamesList.size());
		String name = inputNamesList.get(nameIndex);
		
		String numChar = "0123456789";
		String num = "";
		char charAt;

		for (int i = 0; i < 6; i++) {
			charAt = numChar.charAt(rand.nextInt(numChar.length()));
			num += charAt;
		}
		return name + num;
	}

	/**
	 * Use wait driver to wait until page loading complete
	 * 
	 * @param driver
	 */
	public static void waitForLoad(WebDriver driver) {
		new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
	}

	/**
	 * Use wait driver to wait until page loading complete with Sleep time
	 * 
	 * @param driver
	 * @param milisecond
	 * @throws InterruptedException 
	 */
	public static void waitForLoad(WebDriver driver, long milisecond) throws InterruptedException {
		new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
		TimeUnit.MILLISECONDS.sleep(milisecond);
	}
	
	/**
	 * Check element exists in current page
	 * 
	 * @param name
	 * @param typeId
	 * @param driver
	 * @return boolean
	 */
	public static boolean existsElement(String name, int typeId, WebDriver driver) {
		try {
			if (typeId == 1) {
				driver.findElement(By.className(name));
			} else if (typeId == 2){
				driver.findElement(By.linkText(name));
			} else if (typeId == 3){
				driver.findElement(By.xpath(name));
			} else {
				driver.findElement(By.id(name));
			}
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	// Get list mail
	public static List<String> listMail(String gmailInput) {
		List<String> listMail = new ArrayList<String>();
		int length = gmailInput.length();
		String gmail = gmailInput;
		int count = 0;
		String s = ".";
		String print = "";
		for (int k = 1; k <= length; k++) {
			for (int j = k; j <= length - 12; j++) {
				for (int i = k + 1; i <= gmail.length() - 11; i++) {
					print = gmail.substring(0, i) + s + gmail.substring(i);
					if (j > k && print.indexOf("..") == -1) {
						listMail.add(print);
					}
				}
				count++;
				gmail = gmail.substring(0, j + count) + s + gmail.substring(j + count);
			}
			count = 0;
			gmail = gmailInput;
		}
		return listMail;
	}

	/**
	 * Write error messages to error.log file
	 * 
	 * @param errorMsg 
	 */
	public static void writeErrorMessages(String errorMsg) {

		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;

		try {
			String currentLine;
			StringBuilder fileContent = new StringBuilder();

			Path filePath = Paths.get("src", "main", "resources", "error_mail.txt");
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

			fileContent.append(errorMsg + "\n");

			fileWriter = new FileWriter(logFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(fileContent.toString());
		} catch (IOException e) {
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

	/**
	 * Create Web Driver
	 * 
	 * @param isChrome 
	 * @return driver
	 */
	public static WebDriver createWebDriver(boolean isChrome) {

		WebDriver driver;
		if (isChrome) {
			Path filePath = Paths.get("src", "main", "resources", "service-tool", "chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", filePath.toString());
			ChromeOptions ChromeOptions = new ChromeOptions();
			ChromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			driver = new ChromeDriver(ChromeOptions);
		} else {
			Path filePath = Paths.get("src", "main", "resources","service-tool", "geckodriver.exe");
			System.setProperty("webdriver.gecko.driver", filePath.toString());
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			driver = new FirefoxDriver(firefoxOptions);
		}

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}
	
	public static void confirmMail(WebDriver driver, String email, String defineConfirmPath, String mailSender,
			int mailType) throws InterruptedException {

		int count = 0;
		String confirmPath;
		WebElement element;
		List<WebElement> unReadMailList;
		List<WebElement> confirmPathsList;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		System.out.println("Confirm mail === START");

		try {
			driver.get("https://accounts.google.com/signin/v2/identifier?continue="
					+ "https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
			CommonUtils.waitForLoad(driver, 1000);
			if (!"https://mail.google.com/mail/u/0/#inbox".equals(driver.getCurrentUrl())) {

				// Login gmail
				element = driver.findElement(By.id("identifierId"));
				element.sendKeys(email);
				driver.findElement(By.id("identifierNext")).click();
				CommonUtils.waitForLoad(driver, 500);
				element = driver.findElement(By.name("password"));
				element.sendKeys(EMAIL_PASS);

				WebDriverWait wait = new WebDriverWait(driver, 200);
				element = wait.until(ExpectedConditions.elementToBeClickable(By.id("passwordNext")));
				element.click();
			}
			CommonUtils.waitForLoad(driver, 1000);

			unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
			while (unReadMailList.size() == 0 && count < 3) {
				driver.get("https://mail.google.com");
				CommonUtils.waitForLoad(driver, 1000);
				unReadMailList = driver.findElements(By.xpath("//*[@class='zF']"));
				count++;
			}
			for (WebElement unReadMail : unReadMailList) {
				if (unReadMail.isDisplayed() && (mailSender.equals(unReadMail.getAttribute("email")))) {

					// Read confirm mail
					unReadMail.click();
					CommonUtils.waitForLoad(driver, 1000);

					if (mailType == 1) {
						List<WebElement> listATag = driver.findElements(By.linkText(defineConfirmPath));
						
						if (listATag.size() > 0) {
							js.executeScript("arguments[0].click();", listATag.get(listATag.size() - 1));
							CommonUtils.waitForLoad(driver, 1000);
						} else {
							confirmMail(driver, email, defineConfirmPath, mailSender, mailType);
						}
					} else {
						confirmPathsList = driver.findElements(By.tagName("a"));
						for (WebElement elm : confirmPathsList) {
							if (elm.getText().indexOf(defineConfirmPath) != -1) {

								confirmPath = elm.getAttribute("href");
								js.executeScript("window.open('" + confirmPath + "','_blank');");
								TimeUnit.SECONDS.sleep(1);
								break;
							}
						}
					}
					break;
				}
			}
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			confirmMail(driver, email, defineConfirmPath, mailSender, mailType);
		}
		System.out.println("Confirm mail === END");
	}
}