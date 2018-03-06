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

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonUtils {

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
		Path inputNamesPath = Paths.get("src", "main", "resources", "input_name.txt");
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
}