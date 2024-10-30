package stepsDef;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.FileReader;
import java.util.HashMap;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import pageFiles.Login;
import configuration.ConfigurationFile;

public class LoginSD {

// create objects from other used classes
	ConfigurationFile 	configurationFile = new ConfigurationFile();
	private Login 		login 			  = new Login(driver);

// declare the used variable over this class 
	public static WebDriver driver;
    public 		String TestDataFile = configurationFile.TestDataFilePath;
    private 	List<Map<String, String>> csvData;
    
// Block of code to run once the class is used
    @Before
// read csv file then save it in 2-D List of strings
    public void loadDataFromCSV() throws IOException, CsvException {
        csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(TestDataFile))) {
            List<String[]> allRows = reader.readAll();
            String[] headers = allRows.get(0); // First row is the header

            for (int i = 1; i <= 4; i++) { // Limiting to 4 rows, as specified
                String[] row = allRows.get(i);
                Map<String, String> data = new HashMap<>();
                data.put(headers[0], row[0]);
                data.put(headers[1], row[1]);
                data.put(headers[2], row[2]);
                csvData.add(data);
                }
            }
        }

// Convert CSV data to a DataTable for Scenario Outline
    public DataTable getDataTableFromCSV() {
        List<List<String>> dataTable = new ArrayList<>();
        dataTable.add(List.of("username", "password", "MSG")); // Header
        for (Map<String, String> row : csvData) {
            dataTable.add(List.of(row.get("Username"), row.get("Password"), row.get("MSG")));
        }
        return DataTable.create(dataTable);
    }

 // method to return the driver to be use same driver in each application page
    public static WebDriver getDriver() {
        return driver;
    }

 // Block of code to run once the class is used
 // I here initiate the driver based on the selection in Configuration File
    @Before
    public void setUp() {
		String Drivertype = configurationFile.Driverpath;
		if (Drivertype.contains("chromedriver")){
			System.setProperty("webdriver.chrome.driver", Drivertype );
			driver = new ChromeDriver();
			}
		else if (Drivertype.contains("geckodriver")){
			System.setProperty("webdriver.gecko.driver", Drivertype);
			driver = new FirefoxDriver();
			}
		driver.manage().window().maximize();
    }

    @Given("I connected to the Landing page using label {string}")
	public void i_connected_to_the_landing_page_using_label(String Label) throws IOException {
        driver.get(configurationFile.Landing_URL);
//pass the used driver to the following application page
       login = new Login(getDriver() );
        login.AcceptedUsernamesLabel(Label);
        }

	@When("I login with Username, and Password then assert the MSG from CSV file")
	public void i_login_with_username_and_password_then_assert_the_msg_from_csv_file() throws IOException, InterruptedException {
        DataTable CSVDATA = getDataTableFromCSV();
        for (int i=0; i<4; i++) {
    	String username	= CSVDATA.cell(i+1, 0);
    	String password = CSVDATA.cell(i+1, 1);
    	String msg = CSVDATA.cell(i+1, 2);
    	
        login = new Login(getDriver() );
        login.enterUsername(username);
		login.enterPassword(password);
		login.clickLoginButton();
		login.CheckErrorMSG(msg);
		
// I here wait some seconds for you to notice the TC is finished the close the driver
		TimeUnit.SECONDS.sleep(3);
		driver.close();
		setUp();
// I open new session for the next loop
		driver.get(configurationFile.Landing_URL);
		}

	}

// Block of code to run once the class is used after finishing the test
	@After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            }
        }
    }
