package BrellaFormValidation;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class FormValidationTest {

    WebDriver driver;
    WebDriver wait;
    

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://brella-react-git-temp-qa-form-brella.vercel.app");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @DataProvider(name = "formData")
    public Object[][] getFormData() throws IOException {
    	List<String[]> data = ExcelUtils.readExcel(System.getProperty("user.dir")+"\\TestData\\TestData.xlsx");
        Object[][] dataArray = new Object[data.size()][data.get(0).length];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i] = data.get(i);
        }
        return dataArray;
    }
    
    @Test(priority=1)
    public void testFormFieldPresense() {
    	// Locate elements
    	Assert.assertTrue(driver.findElement(By.id("form name_name")).isDisplayed(), "Name field not found"); 
    	Assert.assertTrue(driver.findElement(By.id("form name_email")).isDisplayed(), "Email field not found"); 
    	Assert.assertTrue(driver.findElement(By.className("ant-switch-handle")).isDisplayed(), "Switch button not found"); 
    	Assert.assertTrue(driver.findElement(By.className("ant-input-number-input")).isDisplayed(), "InputNumber field not found"); 
    	Assert.assertTrue(driver.findElement(By.id("form name_year")).isDisplayed(), "Year field not found"); 
    	Assert.assertTrue(driver.findElement(By.id("submit")).isDisplayed(), "Save button not found"); 
    }

    @Test(dataProvider = "formData",priority=2)
    public void testFormSubmission(String name, String email, String switchValue, String number, String year, String expectedMessage) {
        // Locate elements
        WebElement nameField = driver.findElement(By.id("form name_name"));
        WebElement emailField = driver.findElement(By.id("form name_email"));
        WebElement switchButton = driver.findElement(By.className("ant-switch-handle"));
        WebElement numberDropdown = driver.findElement(By.className("ant-input-number-input"));
        WebElement yearCalendar = driver.findElement(By.id("form name_year"));
        WebElement saveButton = driver.findElement(By.id("submit"));

        // Fill out the form
        nameField.clear();
        nameField.sendKeys(name);
        // Verify that the save button is enabled after email entered
       Assert.assertFalse(saveButton.isEnabled(), "Save button should be disabled");

        emailField.clear();
        emailField.sendKeys(email);
        
        // Verify that the save button is enabled after email entered
        Assert.assertTrue(saveButton.isEnabled(), "Save button should be enabled");


        if (switchValue.equalsIgnoreCase("true")) {
            if (!switchButton.isSelected()) {
                switchButton.click(); 
            }
        } else {
            if (switchButton.isSelected()) {
                switchButton.click(); 
            }
        }

        numberDropdown.sendKeys(number); 
        yearCalendar.sendKeys(year); 

        // Click the save button
        saveButton.click();

        // Wait for the pop-up to appear using class name
        WebElement message = driver.findElement(By.className("ant-message"));

        // Retrieve the text using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String messageText = (String) js.executeScript("return arguments[0].textContent;", message);
        System.out.println("Expected: " + expectedMessage.trim()+ " Actual: " + messageText);
        // Verify the message text
        Assert.assertEquals(messageText.trim(), expectedMessage.trim(), "Message text should be as expected");
   
    }
    
    @AfterMethod
    public void refreshPage() {
        // Refresh the page after each test method
        driver.navigate().refresh();
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
