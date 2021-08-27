package com.identity.test;


import com.identity.VO.VehicleDetailsVO;
import com.identity.pages.HomePage;
import com.identity.pages.VehicleResultsPage;
import com.identity.service.FileService;
import com.identity.testUtils.BaseTest;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

@RunWith(DataProviderRunner.class)
public class VehicleRegistrationDetailTest extends BaseTest {
    private static String browser;
    HomePage homePage = null;
    HashMap<String, VehicleDetailsVO>  vehilceDetailsVOMap = null;
    private static Logger logger = LoggerFactory.getLogger(VehicleRegistrationDetailTest.class);

    @DataProvider
    public static Object[] dataProvider() throws IOException {
        //First Load the properties and Driver
        new VehicleRegistrationDetailTest().loadDriverAndProperties();
        //Load the Registration numbers from the input files into the data provider Object Array
        String inputFolderPath = getProp().getProperty("input_folder_path");
        return FileService.loadInputData(inputFolderPath).toArray();
    }

    @Before
    public void gotoHomePage() {
        try {
            String outputFolderPath = getProp().getProperty("output_folder_path");
            //Load the output details from all the files present in the given folders
            vehilceDetailsVOMap = FileService.loadOutputData(outputFolderPath);
            // navigate to the Home Page in the UI
            homePage = navigateToHomePage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    @UseDataProvider("dataProvider")
    public void enterRegistrationDetails(String registrationNumber) throws Exception {
        //For each of the Registration numbers coming from data provider, click Free car check button, and assert the details
        VehicleDetailsVO vehicleDetailsVOFromOutputFile = vehilceDetailsVOMap.get(registrationNumber.replaceAll("\\s+",""));
        VehicleResultsPage vehicleResultsPage = homePage.enterRegistrationAndClickFreeCarCheckButton(registrationNumber);
        VehicleDetailsVO vehicleDetailsVOFromUIPage = vehicleResultsPage.returnVehicleDetails();
        //If the registration number details are found proceed to the if block
        if(StringUtils.isNotBlank(vehicleDetailsVOFromUIPage.getModel())) {
            logger.info("values are found in the UI for the registration number -" + registrationNumber);
            logger.info("values from the UI are : make = " + vehicleDetailsVOFromUIPage.getMake() + ", color = "
                    + vehicleDetailsVOFromUIPage.getColor() + "Model = " + vehicleDetailsVOFromUIPage.getModel() +
                    "Year = " + vehicleDetailsVOFromUIPage.getYear());
            //asserting the vehicle make
            Assert.assertEquals("values from UI and outputFile for make are not equal", vehicleDetailsVOFromUIPage.getMake(), vehicleDetailsVOFromOutputFile.getMake());
            //asserting the vehicle colour
            Assert.assertEquals("values from UI and outputFile for color are not equal", vehicleDetailsVOFromUIPage.getColor(), vehicleDetailsVOFromOutputFile.getColor());
            //asserting the vehicle Model
            Assert.assertEquals("values from UI and outputFile for Model are not equal", vehicleDetailsVOFromUIPage.getModel(), vehicleDetailsVOFromOutputFile.getModel());
            //asserting the vehicle colour
            Assert.assertEquals("values from UI and outputFile for Year are not equal", vehicleDetailsVOFromUIPage.getYear(), vehicleDetailsVOFromOutputFile.getYear());
            homePage = vehicleResultsPage.clickCheckAnotherVehicleButton();
        } else {
            //If the registration number details are not found then fail the test case
            logger.info("** values are not found in the UI for the registration number ** -" + registrationNumber);
            Assert.fail("** values are not found in the UI for the registration number ** -" + registrationNumber);
        }
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
