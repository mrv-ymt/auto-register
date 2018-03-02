package tool.auto.common;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;

public class CommonUtils {

	/**
	 * Get list of email from file
	 * 
	 * @return List<String>
	 */
	public static List<String> getEmailsList() {

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
	
	/**
	 * Get list of email from file
	 * 
	 * @return List<String>
	 */
	public static List<String> getEmailsList(String fileName) {

		List<String> emailsList = new ArrayList<String>();
		Reader reader = null;
		CSVReader csvReader = null;
		try {

			// Get file from resources folder
			Path inputEmailsPath = Paths.get("src", "main", "resources", fileName);
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

	/**
	 * Get list of name from input_name.csv file
	 * 
	 * @return List<String>
	 */
	public static List<String> getInputNames() {

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
	 * Use wait driver to wait until page loading complete
	 * 
	 * @param driver
	 */
	public static void waitForLoad(WebDriver driver) {
		new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
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

}