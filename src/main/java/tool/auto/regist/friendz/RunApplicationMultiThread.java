package tool.auto.regist.friendz;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.opencsv.CSVReader;

public class RunApplicationMultiThread {

	private static final String GECKO_DRIVER = "D:\\Soft\\geckodriver-v0.19.1-win64\\geckodriver.exe";
	private static final int MAX_NUM_MAIL = 10;

	public static void main(String[] arg) throws InterruptedException {

		List<String> emailsList = getEmailsList();
		List<String> inputNamesList = getInputNames();

		System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);
		List<String> refUrlsList = new ArrayList<String>();
		refUrlsList.add("https://steward.friendz.io/go?r=NDkxNzY3");
		//refUrlsList.add("https://steward.friendz.io/go?r=NDkxOTAy");

		int fromIndex;
		int toIndex;
		int numOfEmail = emailsList.size();
		AutoRegisterRunnable autoRegister;

		for (int i = 0; i < refUrlsList.size(); i++) {
			fromIndex = i * MAX_NUM_MAIL;
			toIndex = i * MAX_NUM_MAIL + (MAX_NUM_MAIL - 1);

			if (fromIndex >= numOfEmail) {
				break;
			}
			if (toIndex >= numOfEmail) {
				toIndex = numOfEmail;
			}

			autoRegister = new AutoRegisterRunnable(refUrlsList.get(i), fromIndex, toIndex, emailsList, inputNamesList);
			autoRegister.start();
			TimeUnit.SECONDS.sleep(5);
		}
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
}