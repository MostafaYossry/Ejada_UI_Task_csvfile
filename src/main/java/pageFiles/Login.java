package pageFiles;


import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class Login {
   
	WebDriver driver;

// Find all page elements using FindBy and useful locators
	@FindBy(xpath = "//div[@class='login_credentials']/h4")
	WebElement AcceptedUsernames_Label;

	@FindBy(id = "user-name")
	WebElement UserName;

	@FindBy(id = "password")
	WebElement PassWord;

	@FindBy(id = "login-button")
	WebElement LoginButton;

	@FindBy(xpath = "//div[@class='login-box']/form/div[3]/h3")
	WebElement ErrorMSG;

	public  WebDriver getDriver() {
        return driver;
    }

// use same created driver over all pages
	public Login(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


// Create methods to use the page elements
    public void AcceptedUsernamesLabel( String Actual_label) throws IOException {
    	Assert.assertTrue(AcceptedUsernames_Label.getText().contains(Actual_label));
		}

    public void enterUsername( String username) throws IOException {
		UserName.sendKeys(username);
		}

    public void enterPassword(String password) throws IOException {
		PassWord.sendKeys(password);
		}
    
    
    public void clickLoginButton() throws IOException {
		LoginButton.click();
		}
    
    public void CheckErrorMSG( String ActualMSG) throws IOException {
    	Assert.assertTrue(ErrorMSG.getText().contains(ActualMSG));
		}
}