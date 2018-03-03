package tool.auto.winium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

public class sampleTest {

	private static WiniumDriver driver;

	public static void main(String[] args) throws InterruptedException, IOException {

		setupEnvironment();
		Thread.sleep(1000);
		WebElement window = driver.findElementByClassName("CalcFrame");
		WebElement menuItem = window.findElement(By.id("MenuBar")).findElement(By.name("View"));
		menuItem.click();
		driver.findElementByName("Scientific").click();

		window.findElement(By.id("MenuBar")).findElement(By.name("View")).click();
		driver.findElementByName("History").click();

		window.findElement(By.id("MenuBar")).findElement(By.name("View")).click();
		driver.findElementByName("History").click();

		window.findElement(By.id("MenuBar")).findElement(By.name("View")).click();
		driver.findElementByName("Standard").click();

		driver.findElementByName("4").click();
		driver.findElementByName("Add").click();
		driver.findElementByName("5").click();
		driver.findElementByName("Equals").click();
		driver.close();
	}

	public static WiniumDriver setupEnvironment() throws IOException {

		Path filePath = Paths.get("src", "main", "resources","service-tool", "Winium.Desktop.Driver.exe");
		String appPath = "C:/windows/system32/calc.exe";

		DesktopOptions options = new DesktopOptions(); // Initiate Winium Desktop Options
		options.setApplicationPath(appPath); // Set application path
		File drivePath = new File(filePath.toString()); // Set winium driver path

		WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(drivePath).usingPort(9999)
				.withVerbose(true).withSilent(false).buildDesktopService();
		service.start(); // Build and Start a Winium Driver service
		driver = new WiniumDriver(service, options); // Start a winium driver

		return driver;
	}
}