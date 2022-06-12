package com.amazan.auto;

import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductSearch {

	
	
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		String insertProductDetails =" insert into product_details(product_title,product_price,product_description) values (?,?,?)";
		
		String testInsert =" insert into product_details(product_title,product_price) values (?,?)";
		String baseUrl = " http://amazon.in/";
		System.setProperty("webdriver.chrome.driver","./resources/chromedriver101.exe");

		FileReader reader=new FileReader("./src/config.properties");  
	    Properties props=new Properties();  
	    props.load(reader);  
	    reader.close();
		//To read the property file from location
		
	    DbConnection Dbc = new DbConnection(props.getProperty("url"),props.getProperty("userid"),props.getProperty("password"));
		Connection con = Dbc.getConnection();
	    
	   
		
		
		
		WebDriver driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		//Navigate to Application given URL
		driver.get(baseUrl);

		
		WebElement searchbar = driver.findElement(By.id("twotabsearchtextbox"));
		WebElement searchBtn = driver.findElement(By.id("nav-search-submit-button"));
		
		String mainWindow=driver.getWindowHandle();
		
		
		String[] products ={"Apple Watch Series 7","Samsung Galaxy Watch4","amazfit gts 2"};
	
		
		int count =0;
		for(String item: products)
		{
		count++;
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(item);
		driver.findElement(By.id("nav-search-submit-button")).click();
		
		Thread.sleep(3000);
		List<WebElement> searchResult = driver.findElements(By.xpath("//a[@class='a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal']"));
	
		 
	 
		 
		 
		searchResult.get(0).click();
		
		Set<String> winHandles = driver.getWindowHandles();
			
		WebDriverWait wait = new WebDriverWait(driver, 10);
	
		
		 //Loop through all handles
		 for(String handle: winHandles)
		 {
			 if(!handle.equals(mainWindow))
			 {
			 driver.switchTo().window(handle);
				
				WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
				
				String product_title = driver.findElement(By.id("productTitle")).getText();
				String product_price = driver.findElement(By.xpath("//div[@class='a-section a-spacing-none aok-align-center']//span[@class='a-price-whole']")).getText();
				String product_description = driver.findElement(By.id("feature-bullets")).getText();
				
				
				
				PreparedStatement stmt = con.prepareStatement(insertProductDetails);
			    stmt.setString(1, product_title);
			    stmt.setString(2, product_price);
			    stmt.setString(3, product_description);
				
			    
			    stmt.executeUpdate();
			    stmt.clearBatch();
			 
			 			 
			 driver.close();
			 driver.switchTo().window(mainWindow);
			 System.out.println(driver.getWindowHandle()+ "  "+mainWindow);
			 Thread.sleep(3000);
			 driver.findElement(By.id("twotabsearchtextbox")).clear();
			 }
		 
		
		 
		 System.out.println(count);
		}
		
     }
		con.close();
	}
}


