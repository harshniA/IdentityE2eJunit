package com.identity.pages;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.identity.VO.VehicleDetailsVO;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class VehicleResultsPage extends Page {


    @FindBy(how = How.XPATH, using = "//dt[contains(text(),'Registration')]/../dd")
    private WebElement registrationNumber;

    @FindBy(how = How.XPATH, using = "//dt[contains(text(),'Make')]/../dd")
    private WebElement make;

    @FindBy(how = How.XPATH, using = "//dt[contains(text(),'Model')]/../dd")
    private WebElement model;

    @FindBy(how = How.XPATH, using = "//dt[contains(text(),'Colour')]/../dd")
    private WebElement colour;

    @FindBy(how = How.XPATH, using = "//dt[contains(text(),'Year')]/../dd")
    private WebElement year;

    @FindBy(how = How.XPATH, using = "//a[contains(text(),'Check Another Vehicle')]")
    private WebElement checkAnotherVehicleButton;

    @FindBy(how = How.XPATH, using = "//a[contains(text(),'Try Again')]")
    private WebElement tryAgainButton;


    public VehicleResultsPage(WebDriver driver) {
        super(driver);
    }

    public VehicleDetailsVO returnVehicleDetails() {
        webdriverWait.until(ExpectedConditions.visibilityOf(registrationNumber));
        VehicleDetailsVO vehicleDetailsVOFromPage = new VehicleDetailsVO();
        vehicleDetailsVOFromPage.setRegistration(registrationNumber.getText());
        vehicleDetailsVOFromPage.setMake(make.getText());
        vehicleDetailsVOFromPage.setColor(colour.getText());
        vehicleDetailsVOFromPage.setModel(model.getText());
        vehicleDetailsVOFromPage.setYear(year.getText());
        return vehicleDetailsVOFromPage;

    }

    public HomePage clickCheckAnotherVehicleButton() throws Exception {
        TimeUnit.SECONDS.sleep(getVeryShortTime());
        scrollToElement(checkAnotherVehicleButton);
        clickElementWithRetry(checkAnotherVehicleButton);
        TimeUnit.SECONDS.sleep(getVeryShortTime());
        HomePage homePage = PageFactory.initElements(_driver,
                HomePage.class);
        return homePage;
    }

}
