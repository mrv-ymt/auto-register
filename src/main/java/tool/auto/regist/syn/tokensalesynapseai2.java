package tool.auto.regist.syn;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;


public class tokensalesynapseai2 {
	
	private static final String EMAIL = "myhearwillgoonceledion99999999@gmail.com";
	private static final String EMAIL_PASS = "Dragon0104146890";
	private static final String REF_LINK = "https://tokensale.synapse.ai/r/146846";
	
	private static final String GECKO_DRIVER = "D:\\Store\\geckodriver-v0.19.1-win64\\geckodriver.exe";

	private static WebDriver driver = null;
	static ClassLoader classLoader = tokensalesynapseai2.class.getClassLoader();
	static List<String> inputNamesList;
	
	public static void main(String[] arg) throws IOException{
		int indexBegin = 0;
		int count = 0;
		
		String email;
		List<String> emailsList = getEmailsList();
		inputNamesList = getInputNames();
		System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		driver = new FirefoxDriver(firefoxOptions);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Go to web		
		for (int i = indexBegin; i < emailsList.size() ; i++) {
			try {
				System.out.println("======================");
				count = i + 1;
				System.out.println("START" + count);
				email = emailsList.get(i);
				fillRegistForm(email);
				waitForLoad(driver);
				confirmMail(email);
				System.out.println("END" + count);
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
		waitForLoad(driver);
		while (!driver.getCurrentUrl().equals("https://tokensale.synapse.ai/")) {
			TimeUnit.SECONDS.sleep(1);
		}
		element = driver.findElement(By.className("btn-success"));
		element.click();
		waitForLoad(driver);
		TimeUnit.SECONDS.sleep(3);

		element = driver.findElement(By.name("user[country]"));
		Select dropdown = new Select(element);
		dropdown.selectByIndex((int) (Math.random() * 100 + 1));

		element = driver.findElement(By.name("user[first_name]"));
		element.clear();
		element.sendKeys(getRandomName());

		element = driver.findElement(By.name("user[last_name]"));
		element.clear();
		element.sendKeys(getRandomName());

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
		System.out.println("Input Capcha");

		while (!driver.getCurrentUrl().equals("https://tokensale.synapse.ai/unconfirmed")) {
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}

	private static void confirmMail(String email) throws InterruptedException {

		System.out.println("Confirming mail");

		// Login gmail
		driver.get("https://accounts.google.com/signin/v2/identifier?continue="
				+ "https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
		waitForLoad(driver);
		TimeUnit.SECONDS.sleep(1);
		if (!"https://mail.google.com/mail/u/0/#inbox".equals(driver.getCurrentUrl())) {
			WebElement gmail = driver.findElement(By.id("identifierId"));
			gmail.sendKeys(EMAIL);
			driver.findElement(By.id("identifierNext")).click();
			waitForLoad(driver);
			TimeUnit.MILLISECONDS.sleep(500);
			WebElement gmailPass = driver.findElement(By.name("password"));
			gmailPass.sendKeys(EMAIL_PASS);
			driver.findElement(By.id("passwordNext")).click();
		}
		waitForLoad(driver);

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
					waitForLoad(driver);
					listATag = driver.findElements(By.linkText("Confirm my account"));
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();", listATag.get(listATag.size() - 1));
					waitForLoad(driver);
					
					try {

						// Go to tab 2
						tabList = new ArrayList<String>();
						tabList.addAll(driver.getWindowHandles());
						driver.switchTo().window(tabList.get(1));
						waitForLoad(driver);
						wait.until(ExpectedConditions
								.visibilityOfElementLocated(By.linkText("Click here to be redirected to sign in.")));
	
						// Re-signin
						driver.get("https://tokensale.synapse.ai/users/sign_in");
						waitForLoad(driver);
						TimeUnit.SECONDS.sleep(2);
						element = driver.findElement(By.name("user[email]"));
						element.clear();
						element.sendKeys(email);
	
						element = driver.findElement(By.name("user[password]"));
						element.clear();
						element.sendKeys(email);
	
						element = driver.findElement(By.name("commit"));
						element.click();
						waitForLoad(driver);

						element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log Out")));
						element.click();
						waitForLoad(driver);
						System.out.println(email);
					} catch (Exception e) {
						System.out.println("Cannot re-signin mail: " + email);
					}

					driver.switchTo().window(tabList.get(0));
					waitForLoad(driver);
					driver.close();
					driver.switchTo().window(tabList.get(1));
					return;
				}
			}
		}
	}
		
	
	
	public static boolean existsElement(String name, int typeId) {
		try {
			if (typeId == 1) {
				driver.findElement(By.className(name));
			} else if (typeId == 2){
				driver.findElement(By.linkText(name));
			} else {
				driver.findElement(By.id(name));
			}
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	
	// Get list mail
	public static List<String> getListMail() throws IOException{
		List<String> listMew = new ArrayList<String>();
		String fileName = "c:/mail.txt";
		try(Stream<String> stream = Files.lines(Paths.get(fileName),StandardCharsets.UTF_8)){
			stream.forEach(line ->{
				listMew.add(line);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMew;		
	}	
	
	private static List<String> getEmailsList() {

		List<String> emailsList = new ArrayList<String>();
		Reader reader = null;
		CSVReader csvReader = null;
		try {

			// Get file from resources folder
			Path inputEmailsPath = Paths.get("src", "main", "resources", "email_list.csv");
			reader = Files.newBufferedReader(inputEmailsPath);
			csvReader = new CSVReader(reader);

			// Reading Records One by One in a String array
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				emailsList.add(nextRecord[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}

				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return emailsList;
	}
	
	private static List<String> getInputNames() {

		List<String> inputNamesList = new ArrayList<String>();
		Reader reader = null;
		CSVReader csvReader = null;
		try {
			Path inputNamesPath = Paths.get("src", "main", "resources", "input_name.csv");
			reader = Files.newBufferedReader(inputNamesPath);
			csvReader = new CSVReader(reader);

			// Reading Records One by One in a String array
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				inputNamesList.add(nextRecord[0].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}

				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return inputNamesList;
	}
	
	private static String getRandomName() {
		Random rand = new Random();
		int nameIndex = rand.nextInt(inputNamesList.size());
		return inputNamesList.get(nameIndex);
	}
	
	static void waitForLoad(WebDriver driver) {
		new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
	}
}