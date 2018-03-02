package tool.auto.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public class GenerateMailsList {

	public static void main(String[] args) {
		String inputMail = "myhearwillgoonceledion99999999";
		Set<String> mailsList = new LinkedHashSet<String>();
		String temp = inputMail;
		String correctMail;
		
		for (int i = 0; i < inputMail.length(); i++) {
			
			if (i > 0) {
				temp = inputMail.substring(0, i) + "." + inputMail.substring(i, inputMail.length());
			} 
			
			for (int j = 1; j < temp.length(); j++) {
				
				correctMail = temp.substring(0, j) + "." + temp.substring(j, temp.length());
				if (!correctMail.contains("..")) {
					mailsList.add(correctMail + "@gmail.com");
				}
			}
		}
		
		writeToFile(mailsList);
		 for (String string : mailsList) {
			System.out.println(string);
		}
		
		System.out.println("========= Generated " + mailsList.size() + " email =========");
	}
	
	private static void writeToFile(Set<String> mailsList) {
		
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;

		try {

			StringBuilder fileContent = new StringBuilder();
			Path filePath = Paths.get("src", "main", "resources", "generated_mail1.csv");
			File generatedMailFile = new File(filePath.toString());

			for (String mail : mailsList) {
				fileContent.append(mail + "\n");
			}

			fileWriter = new FileWriter(generatedMailFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(fileContent.toString());
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException ex) {
				System.out.println("IOException: " + ex);
			}
		}	
	}
}
