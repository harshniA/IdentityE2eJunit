
package com.identity.testUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import com.identity.commonUtils.CommonUtils;

public abstract class SeleniumBaseTest extends CommonUtils {

	protected static WebDriver driver;
	public static Logger logger = null;
	private static Properties prop;
	private static Properties or;
	public static WebDriver getDriver() {
		return driver;
	}
	public static Properties getProp() {
		return prop;
	}
	public static void info(String info) {
		logger.info(info);
	}


	public void loadDriverAndProperties() {
		loadProperties();
		String browser = getProp().getProperty("browser");
		driver = getDriverInstance(false, browser);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(getProp().getProperty("implicit_time")),
				TimeUnit.SECONDS);
	}

	public static void loadProperties() {
		try {
			if (prop == null) {
				prop = new Properties();
				prop.load(new FileInputStream("src/main/resources/application.properties"));
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public WebDriver getDriverInstance(boolean incognitoMode, String browser) {
		DesiredCapabilities capability;
		WebDriver newDriver = null;
		String downloadFilepath = System.getProperty("user.dir") + "/src/main/resources/downloads";
		ChromeOptions options = new ChromeOptions();
		System.out.println("*********system browser name - "+    browser) ;
		
		if(null==browser){
			//browser = "chrome_mac";
			browser = "firefox";
		}
		switch (browser) {
		case "chrome_mac":
			if (!incognitoMode) {
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", downloadFilepath);
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("test-type");
				options.addArguments("--start-maximized");
				capability = DesiredCapabilities.chrome();
				capability.setCapability("nativeEvents", false);
				capability.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/src/main/resources/chromeForMac/chromedriver");
				newDriver = new ChromeDriver(capability);
			} else {
				options.addArguments("test-type");
				options.addArguments("--incognito");
				options.addArguments("--start-maximized");
				capability = DesiredCapabilities.chrome();
				capability.setCapability("nativeEvents", false);
				capability.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/src/main/resources/chromeForMac/chromedriver");
				newDriver = new ChromeDriver(capability);

			}
			break;
		
		case "chrome_pc":
			if (!incognitoMode) {
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", downloadFilepath);
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("test-type");
				options.addArguments("--start-maximized");
				capability = DesiredCapabilities.chrome();
				capability.setCapability("nativeEvents", false);
				capability.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/src/main/resources/chromedriver/chromedriver.exe");
				newDriver = new ChromeDriver(capability);
			} else {
				options.addArguments("test-type");
				options.addArguments("--incognito");
				options.addArguments("--start-maximized");
				capability = DesiredCapabilities.chrome();
				capability.setCapability("nativeEvents", false);
				capability.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/src/main/resources/chromedriver/chromedriver.exe");
				newDriver = new ChromeDriver(capability);

			}
			break;
		default:
			info("no browser provided. Shutting down the test");
			System.exit(1);
			break;
		}
		return newDriver;
	}
	
}
