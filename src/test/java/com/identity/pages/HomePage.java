package com.identity.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends Page {
	

	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Free Car Check')]")
	 private WebElement freeCarCheckButton;

	@FindBy(how = How.XPATH, using = "//*[@id='vrm-input']")
	private WebElement RegistrationTextBox;

	 public HomePage(WebDriver driver) {
		super(driver);
	 }
	 
	 public void navigateTo(String url) {
	        _driver.get(url);
	 }

	 
	 public VehicleResultsPage enterRegistrationAndClickFreeCarCheckButton(String registrationNumber) throws Exception{
		 RegistrationTextBox.clear();
		 RegistrationTextBox.sendKeys(registrationNumber);
		 TimeUnit.SECONDS.sleep(getVeryShortTime());
		 clickElementWithRetry(freeCarCheckButton);
		 TimeUnit.SECONDS.sleep(getVeryShortTime());
		 VehicleResultsPage vehicleResultsPage = PageFactory.initElements(_driver,
				 VehicleResultsPage.class);
		 return vehicleResultsPage;
	 }
	 
	
	 
	 

}
